package com.tokopedia.core.util;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import static com.tokopedia.core.util.TkpdWebView.getQuery;
import static com.tokopedia.core.util.TkpdWebView.getWebviewHeaders;

/**
 * @author by milhamj on 05-Feb-18.
 */

public class TkpdAuthWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            TkpdWebView tkpdWebView = (TkpdWebView) view;
            tkpdWebView.loadAuthUrl(url);
        } catch (Exception e) {
            view.loadUrl(url, getWebviewHeaders(url));
        }
        return true;
    }
}
