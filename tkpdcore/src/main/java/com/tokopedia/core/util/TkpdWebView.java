package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.view.webview.WebViewHelper;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 11/30/16.
 * refer {@link com.tokopedia.abstraction.base.view.webview.TkpdWebView}
 */
@Deprecated
public class TkpdWebView extends WebView {

    private static final String PARAM_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String ERROR_MESSAGE = "Url tidak valid";
    private static final String CRASHLYTICS_ERROR_MESSAGE = "Invalid webview url - ";

    public TkpdWebView(Context context) {
        super(context);
    }

    public TkpdWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TkpdWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadUrlWithFlags(String url) {
        loadUrl(generateUri(url));
    }

    @Override
    public void loadUrl(String url) {
        if(WebViewHelper.validateUrl(url)){
            loadAuthUrl(url);
        }else {
            Crashlytics crashlytics = Crashlytics.getInstance();
            if(crashlytics != null)
                crashlytics.log(CRASHLYTICS_ERROR_MESSAGE + url);

            NetworkErrorHelper.showRedSnackbar(getRootView(), ERROR_MESSAGE);
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if(WebViewHelper.validateUrl(url)){
            super.loadUrl(url, additionalHttpHeaders);
        }else {
            Crashlytics crashlytics = Crashlytics.getInstance();
            if(crashlytics != null)
                crashlytics.log(CRASHLYTICS_ERROR_MESSAGE + url);

            NetworkErrorHelper.showRedSnackbar(getRootView(), ERROR_MESSAGE);
        }
    }

    public void loadAuthUrl(String url) {

        loadUrl(url, getWebviewHeaders(url));
    }

    public void loadOtherUrl(String url) {
        loadUrl(url, new HashMap<String, String>());
    }

    public static Map<String, String> getWebviewHeaders(String url) {
        return AuthUtil.generateWebviewHeaders(
                Uri.parse(url).getPath(),
                getQuery(Uri.parse(url).getQuery()),
                "GET",
                AuthUtil.KEY.KEY_WSV4);
    }

    private static String getQuery(String query) {
        return query != null ? query : "";
    }

    public void loadAuthUrlWithFlags(String url) {
        loadAuthUrl(generateUri(url));
    }

    private String generateUri(String uri) {
        String url = String.valueOf(uri);
        String flagApp = AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP + "=1";
        String device = AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE + "=android";
        String utmSource = AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE + "=android";
        String appVersion = AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION + "=" + GlobalConfig.VERSION_CODE;
        String osVersion = AuthUtil.WEBVIEW_FLAG_PARAM_OS_VERSION + "=" + Build.VERSION.RELEASE;
        String flags = flagApp
                + "&" + device
                + "&" + utmSource
                + "&" + appVersion
                + "&" + osVersion;

        try {
            if (!TextUtils.isEmpty(uri) && Uri.parse(uri).getQuery() == null) {
                url += "?" + URLEncoder.encode(flags, FORMAT_UTF_8);
            } else if (!TextUtils.isEmpty(uri) &&
                    isSeamlessUrl(uri)
                    && (Uri.parse(uri).getQueryParameter(PARAM_URL)) != null
                    && Uri.parse(Uri.parse(uri).getQueryParameter(PARAM_URL)).getQuery() == null) {
                url += "?" + URLEncoder.encode(flags, FORMAT_UTF_8);
            } else {
                flags = "&" + flags;
                url += URLEncoder.encode(flags, FORMAT_UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }

    private boolean isSeamlessUrl(String uri) {
        return uri.startsWith(URLGenerator.getBaseUrl());
    }
}