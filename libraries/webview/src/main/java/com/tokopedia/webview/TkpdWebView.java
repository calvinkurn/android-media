package com.tokopedia.webview;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.utils.URLGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by nisie on 11/30/16.
 */
public class TkpdWebView extends WebView {

    private static final String PARAM_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";

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

    public void loadAuthUrlWithFlags(String url, String userId, String accessToken) {
        if (TextUtils.isEmpty(userId)) {
            loadUrl(generateUri(url));
        } else {
            loadUrl(generateUri(url), AuthUtil.generateHeadersWithBearer(
                    Uri.parse(url).getPath(),
                    getQuery(Uri.parse(url).getQuery()),
                    "GET",
                    AuthUtil.KEY.KEY_WSV4, userId, accessToken));
        }
    }

    /**
     * use loadAuthUrl(String url, String userId, String accessToken) instead.
     */
    @Deprecated
    public void loadAuthUrl(String url, String userId) {
        if (TextUtils.isEmpty(userId)) {
            loadUrl(url);
        } else {
            loadUrl(url, AuthUtil.generateHeaders(
                    Uri.parse(url).getPath(),
                    getQuery(Uri.parse(url).getQuery()),
                    "GET",
                    AuthUtil.KEY.KEY_WSV4, userId));
        }
    }

    public void loadAuthUrl(String url, String userId, String accessToken) {
        if (TextUtils.isEmpty(userId)) {
            loadUrl(url);
        } else {
            loadUrl(url, AuthUtil.generateHeadersWithBearer(
                    Uri.parse(url).getPath(),
                    getQuery(Uri.parse(url).getQuery()),
                    "GET",
                    AuthUtil.KEY.KEY_WSV4, userId, accessToken));
        }
    }

    private String getQuery(String query) {
        return query != null ? query : "";
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