package com.tokopedia.sellerapp.gmsubscribe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.TkpdWebView;


/**
 * Created by sebastianuskh on 11/11/16.
 */

public class GMSubscribeWebView extends TkpdWebView {

    private static final String GOLD_TOKOPEDIA_COM = "http://gold.tokopedia.com";
    private static final String M_TOKOPEDIA_COM = "m.tokopedia.com";
    private static final String TAG = "GMSubscribeWebView";
    public static final String GM_TOPPAY_THANKS_PL = "gm-toppay-thanks.pl";
    private GMSubscribeWebViewListener listener;
    private ProgressBar progressBar;

    public GMSubscribeWebView(Context context) {
        super(context);
    }

    public GMSubscribeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GMSubscribeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GMSubscribeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initGMSubscribeWebView(GMSubscribeWebViewListener listener, ProgressBar progressBar){
        this.listener = listener;
        this.progressBar = progressBar;
        progressBar.setIndeterminate(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(true);
        setWebViewClient(new GMSubscribeWebViewClient());
    }

    public void goToGoldTokopedia(Context context) {

        loadAuthUrlWithFlags(URLGenerator.generateURLSessionLogin(GOLD_TOKOPEDIA_COM, context), null);
    }


    private class GMSubscribeWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading: " + url);
            String host = Uri.parse(url).getHost();
            String lastPath = Uri.parse(url).getLastPathSegment();

            if (host.equals(M_TOKOPEDIA_COM) && lastPath == null) {
                listener.goToHome();
            }

            if (lastPath != null && lastPath.equals(GM_TOPPAY_THANKS_PL)){
                listener.thankYouPageCount();
            }

            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            showProgress(false);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            showProgress(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            showProgress(false);
        }
    }

    private void showProgress(boolean showProgress) {
        int visibility = showProgress? View.VISIBLE : View.GONE;
        progressBar.setVisibility(visibility);
    }


    public interface GMSubscribeWebViewListener{
        void goToHome();
        void thankYouPageCount();
    }


}
