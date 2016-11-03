package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;

public class InvoiceDialog extends DialogFragment {
	private WebView webViewOauth;
	private Context context;
	private String url;
	private DialogFragment fragment;
	private Activity activity;
	private ProgressBar loadingBar;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = this;
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Black);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		context = getActivity();
		Bundle bundle = this.getArguments();
		url = bundle.getString("url");
	}
	
	
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// check if the login was successful and the access token returned
			// this test depend of your API

			CommonUtils.dumper(url);
			view.loadUrl(url);
			return true;
			// BaseActivity.logEvent(Consts.EVENT_CALLBACK + "Login Failed",
			// true);
		}
		
		 @Override  
		    public void onPageFinished(WebView view, String url)  {  
			 webViewOauth.setVisibility(View.VISIBLE);
			 CommonUtils.dumper("FINISH LOADING!");
		    }

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			super.onReceivedSslError(view, handler, error);
			handler.cancel(); // Ignore SSL certificate errors
		}

	}

	private class MyWebChrome extends WebChromeClient {
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			MessageLevel level = consoleMessage.messageLevel();
			//if (level.ordinal() == 3) { // Error ordinal
			//	webViewOauth.stopLoading();
			//	webViewOauth.loadUrl(url);
			//}
			//CommonUtils.dumper(consoleMessage.message());
			//CommonUtils.UniversalToast(context, consoleMessage.message());

			return false;
		}

		@Override
		public void onProgressChanged(WebView view, int progress) {
			// Activities and WebViews measure progress with different scales.
			// The progress meter will automatically disappear when we reach
			// 100%
			activity.setProgress(progress * 1000);
			loadingBar.setProgress(progress);
		}

	}

	@Override
	public void onViewCreated(View arg0, Bundle arg1) {
		super.onViewCreated(arg0, arg1);


		// set the web client
		webViewOauth.setWebViewClient(new MyWebViewClient());
		webViewOauth.setWebChromeClient(new MyWebChrome());
		webViewOauth.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						dismiss();
						return true;
					}

				}
				return false;
			}
		});
		// activates JavaScript (just in case)
		WebSettings webSettings = webViewOauth.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		CommonUtils.dumper(url);
		webViewOauth.loadUrl(url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Retrieve the webview
		View v = inflater.inflate(R.layout.fragment_webview_oath, container,
				false);
		webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
		loadingBar = (ProgressBar) v.findViewById(R.id.progress_bar);
		loadingBar.setVisibility(View.VISIBLE);
		webViewOauth.setVisibility(View.INVISIBLE);
		return v;
	}

}
