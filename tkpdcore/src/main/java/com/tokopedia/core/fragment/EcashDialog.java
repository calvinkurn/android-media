//package com.tokopedia.core.fragment;
//
//
//import android.app.Activity;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnKeyListener;
//import android.view.ViewGroup;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.tkpd.library.utils.CommonUtils;
//import com.tkpd.library.ui.utilities.TkpdProgressDialog;
//import com.tokopedia.core.R;
//import com.tokopedia.core.var.TkpdUrl;
//
//public class EcashDialog extends DialogFragment {
//	private WebView webViewOauth;
//	private Context context;
//	private String url;
//	public EcashInterface ecashinterface;
//	private DialogFragment fragment;
//	private TkpdProgressDialog progress;
//	private TkpdProgressDialog progressFinal;
//
//	public interface EcashInterface {
//    	public void passID(String id);
//    	public void CancelEcash(DialogFragment Fragment);
//    	public void errorOTP(DialogFragment Fragment);
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        progress = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
//        progressFinal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS, context.getResources().getString(R.string.msg_ecash_loading));
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
//        fragment = this;
//    }
//
//	@Override
//	public void onAttach(Activity activity){
//		super.onAttach(activity);
//		context = getActivity();
//		Bundle bundle = this.getArguments();
//		url = bundle.getString("url");
//		ecashinterface = (EcashInterface)context;
//	}
//
//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            //check if the login was successful and the access token returned
//            //this test depend of your API
//
//        	CommonUtils.dumper("URL ACCESSED: " + url);
//        	CommonUtils.dumper("URL CALLBACK: "+ TkpdUrl.ECASH_CALLBACK);
//        	CommonUtils.dumper("URL STATUS: "+url.contains(TkpdUrl.ECASH_CALLBACK));
//            if (url.contains(TkpdUrl.ECASH_CALLBACK)) {
//            	Uri uri = Uri.parse(url);
//            	String id = uri.getQueryParameter("id");
//            	ecashinterface.passID(id);
//            	progress.dismiss();
//            	progressFinal.dismiss();
//            	dismiss();
//                return false;
//            }else {
//            	view.loadUrl(url);
//            	return true;
//            }
//           // BaseActivity.logEvent(Consts.EVENT_CALLBACK + "Login Failed", true);
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        	CommonUtils.dumper("URL START: "+url);
//        	CommonUtils.dumper("FINISH LOADING!");
//        	if (!url.equals("https://mandiriecash.com/ecommgateway/doconfirmation.html")) {
//        		progress.showDialog();
//        	} else {
//        		progressFinal.showDialog();
//        	}
//
//        }
//
//        @Override
//	    public void onPageFinished(WebView view, String url)  {
//        	if (!url.equals("https://mandiriecash.com/ecommgateway/doconfirmation.html")) {
//        		progress.dismiss();
//        	} else {
//        		new Handler().postDelayed(new Runnable() {
//    			  @Override
//    			  public void run() {
//    				  if (progressFinal.isProgress()) {
//    					  progressFinal.dismiss();
//    					  ecashinterface.errorOTP(fragment);
//    				  }
//    			  }
//    			}, 20000);
//        	}
//        	webViewOauth.setVisibility(View.VISIBLE);
//        	CommonUtils.dumper("URL FINISHED: "+url);
//        	CommonUtils.dumper("FINISH LOADING!");
//	    }
//
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			super.onReceivedSslError(view, handler, error);
//			handler.cancel();
//        }
//
//    }
//
//
//
//
//    @Override
//    public void onViewCreated(View arg0, Bundle arg1) {
//        super.onViewCreated(arg0, arg1);
//        webViewOauth.loadUrl(url);
//        webViewOauth.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				 if(event.getAction() == KeyEvent.ACTION_DOWN){
//			            switch(keyCode)
//			            {
//			            case KeyEvent.KEYCODE_BACK:
//			                ecashinterface.CancelEcash(fragment);
//			                return true;
//			            }
//
//			        }
//			        return false;
//			}
//		});
// 		//set the web client
// 		webViewOauth.setWebViewClient(new MyWebViewClient());
// 		//activates JavaScript (just in case)
// 		WebSettings webSettings = webViewOauth.getSettings();
// 		webSettings.setJavaScriptEnabled(true);
// 		webSettings.setBuiltInZoomControls(true);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        //Retrieve the webview
//        View v = inflater.inflate(R.layout.fragment_webview_oath, container, false);
//        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
//        return v;
//    }
//
//
//
//}
