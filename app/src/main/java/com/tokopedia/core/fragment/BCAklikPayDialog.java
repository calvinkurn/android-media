package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
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
import com.tokopedia.core.R;

import org.apache.http.util.EncodingUtils;

public class BCAklikPayDialog extends DialogFragment {
	private WebView webViewOauth;
	private Context context;
	private String url;
	private String bca_code;
	private String bca_amt;
	private String currency;
	private String miscFee;
	private String bca_date;
	private String signature;
	private String callback;
	private String payment_id;
	private String payType;
	public KlikpayInterface klikpayinterface;
	private DialogFragment fragment;
	private Activity activity;
	private ProgressBar loadingBar;

	public interface KlikpayInterface {
		public void passIDKlikPay(String id);

		public void CancelKlikPay(DialogFragment fragment);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = this;
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Black_NoTitleBar);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		context = getActivity();
		klikpayinterface = (KlikpayInterface) context;
		Bundle bundle = this.getArguments();
		url = bundle.getString("url");
		bca_code = bundle.getString("bca_code");
		bca_amt = bundle.getString("bca_amt");
		currency = bundle.getString("currency");
		miscFee = bundle.getString("miscFee");
		bca_date = bundle.getString("bca_date");
		signature = bundle.getString("signature");
		callback = bundle.getString("callback");
		payment_id = bundle.getString("payment_id");
		payType = bundle.getString("payType");
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// check if the login was successful and the access token returned
			// this test depend of your API

			System.out.println("URL ACCESSED: " + url);
			view.loadUrl(url);
			if (url.contains(callback)) {
				Uri uri = Uri.parse(url);
				klikpayinterface.passIDKlikPay(callback);
				dismiss();
				return false;
			} else {
				return true;
			}
			// BaseActivity.logEvent(Consts.EVENT_CALLBACK + "Login Failed",
			// true);
		}
		
		 @Override  
		    public void onPageFinished(WebView view, String url)  {  
			 webViewOauth.setVisibility(View.VISIBLE);
			 CommonUtils.dumper("FINISH LOADING!");
		    }

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			super.onReceivedSslError(view, handler, error);
            handler.cancel();
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
		String postData = "klikPayCode=" + bca_code + "&totalAmount=" + bca_amt
				+ "&currency=" + currency + "&miscFee=" + miscFee
				+ "&transactionDate=" + bca_date + "&signature=" + signature
				+ "&callback=" + callback + "&transactionNo=" + payment_id
				+ "&payType=" + payType;

		webViewOauth.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
		CommonUtils.dumper(postData);
		// set the web client
		webViewOauth.setWebViewClient(new MyWebViewClient());
		webViewOauth.setWebChromeClient(new MyWebChrome());
		webViewOauth.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						klikpayinterface.CancelKlikPay(fragment);
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
