package com.tokopedia.core.webview.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.webview.listener.BaseCallbackListener;

/**
 * Created by Angga.Prasetiyo on 14/09/2015.
 */
public class BaseWebViewClient extends WebViewClient {
    private static final String TAG = BaseWebViewClient.class.getSimpleName();
    private WebViewCallback callback;

    public BaseWebViewClient(@NonNull WebViewCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return callback.onOverrideUrl(url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        callback.onSuccessResult(url);
        callback.onWebTitlePageCompleted(view.getTitle());
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.cancel();
        callback.onErrorResult(error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        callback.onProgressResult(url);
    }

    public interface WebViewCallback extends BaseCallbackListener<String, String, SslError> {
        boolean onOverrideUrl(String url);

        void onWebTitlePageCompleted(String title);
    }
}