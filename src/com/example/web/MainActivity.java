package com.example.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private WebView webView;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button intentButton = (Button) findViewById(R.id.intent);

		Button webviewButton = (Button) findViewById(R.id.webviewButton);
		init();

		intentButton.setOnClickListener(this);
		webviewButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 使用内置系统浏览器打开网页
		case R.id.intent:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("http://www.baidu.com");
			intent.setData(uri);
			// Intent intent =new Intent(Intent.ACTION_VIEW,uri)
			startActivity(intent);
			break;
		case R.id.webviewButton:
			// webview默认使用系统浏览器打开网页,setWebViewClient让网页在webview中打开
			webView.loadUrl("http://www.baidu.com");
			break;
		default:
			break;
		}

	}

	private void init() {
		webView = (WebView) findViewById(R.id.webView);
		// WebView加载本地资源
		// webView.loadUrl("file:///android_asset/example.html");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(0);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// WebView加载页面优先使用缓存加载
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// WebViewClient帮助WebView去处理一些页面控制和请求通知
				// 返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
				view.loadUrl(url);
				return true;
			}

		});

		//打开网页时添加进度条
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// newProgress 1-100之间的整数
				if (newProgress == 100) {
					// 网页加载完毕，关闭ProgressDialog
					closeDialog();
				} else {
					// 网页正在加载,打开ProgressDialog
					openDialog(newProgress);
				}
			}

			private void closeDialog() {
			
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}

			private void openDialog(int newProgress) {

				if (dialog == null) {
					dialog = new ProgressDialog(MainActivity.this);
					dialog.setTitle("正在加载");
					dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					dialog.setProgress(newProgress);
					dialog.show();

				} else {
					dialog.setProgress(newProgress);
				}

			}
		});

	}

	// 改写物理按键——返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Toast.makeText(this, webView.getUrl(),
			// Toast.LENGTH_SHORT).show();
			if (webView.canGoBack()) {
				webView.goBack();// 返回上一页面
				return true;
			} else {
				System.exit(0);// 退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
