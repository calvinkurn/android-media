package com.tokopedia.tkpd.briepay;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.briepay.presenter.BRIePayConnectorView;
import com.tokopedia.tkpd.briepay.presenter.BRIePayImp;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.apache.http.util.EncodingUtils;

/**
 * Created by Default05 on 1/5/2016.
 */
public class BRIePayDialog extends DialogFragment {

    private WebView webViewOauth;
    private Context context;
    private String url;
    private String token;
    private String gateway;
    private String refback;
    private String keysTrxEcomm;
    private String step;
    private DialogFragment fragment;
    private Activity activity;
    private ProgressBar loadingBar;
    private BRIePayImp brIePayImp;

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
        Bundle bundle = this.getArguments();
        url = bundle.getString("url");
        token = bundle.getString("token");
        gateway = bundle.getString("gateway");
        refback = bundle.getString("refback");
        keysTrxEcomm = bundle.getString("keysTrxEcomm");
        step = bundle.getString("step");
        brIePayImp = new BRIePayImp(((BRIePayConnectorView)context));
        CommonUtils.dumper("EBRI "+keysTrxEcomm+" "+step+" "+refback+" "+gateway+" "+token);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("eBRI URL ACCESSED: " + url+" callback "+TkpdUrl.EBRI_CALLBACK);
            if (url.contains(TkpdUrl.EBRI_CALLBACK)) {
                Uri uri = Uri.parse(url);
                String id = uri.getQueryParameter("tid");
                CommonUtils.dumper("eBRI callback after finish" + id);

                brIePayImp.verifyeBRIPayment(id);

                destroyFragment();
                return false;
            } else if(url.contains("login.pl")){
                CommonUtils.dumper("eBRI wrong callback ");
                destroyFragment();
                return false;
            }else {
                return true;
            }
        }

        private void destroyFragment(){
            fragment.dismiss();
            webViewOauth.stopLoading();
            webViewOauth.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url)  {
            System.out.println("eBRI URL FINISHED : " + url);
            webViewOauth.setVisibility(View.VISIBLE);
            CommonUtils.dumper("FINISH LOADING!");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("eBRI URL STARTED : " + url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            CommonUtils.dumper("eBRI Error certificate "+error.getCertificate().getIssuedBy());
            handler.proceed();

        }

    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            activity.setProgress(progress * 1000);
            loadingBar.setProgress(progress);
        }
    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);
        String postData = "token=" + token + "&gateway=" +gateway
                + "&refback=" + refback + "&keysTrxEcomm=" + keysTrxEcomm
                + "&step=" + step;

        // set the web client
        webViewOauth.setWebViewClient(new MyWebViewClient());
        webViewOauth.setWebChromeClient(new MyWebChrome());

        webViewOauth.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        CommonUtils.dumper("EBRI postdata "+postData+ " URL "+url);
        webViewOauth.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                                brIePayImp.canceleBRIPayment(fragment);
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
        View v = inflater.inflate(R.layout.fragment_webview_oath, container,
                false);
        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
        loadingBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        loadingBar.setVisibility(View.VISIBLE);
        webViewOauth.setVisibility(View.INVISIBLE);
        return v;
    }

}
