package com.tokopedia.core.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.webview.WebViewHelper;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.core2.R;

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
    private static final String USER_AGENT_STRING = "Tokopedia Android";
    private RemoteConfig remoteConfig;

    public TkpdWebView(Context context) {
        super(context);
        init(context);
    }

    public TkpdWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TkpdWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);

        //set custom tracking, helpful for GA
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CUSTOMER_USER_AGENT_IN_WEBVIEW, true)) {
            WebSettings webSettings = getSettings();
            String userAgent = String.format("%s - Android %s", "Tokopedia Webview", GlobalConfig.VERSION_NAME);
            webSettings.setUserAgentString(userAgent);
        }
    }

    public void loadUrlWithFlags(String url) {
        loadUrl(generateUri(url));
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (WebViewHelper.isUrlValid(url)) {
            super.loadUrl(url, additionalHttpHeaders);
        } else {
            if (!GlobalConfig.DEBUG)
                Crashlytics.log(getContext().getString(R.string.error_message_url_invalid_crashlytics) + url);

            super.loadUrl(url);
        }
    }

    public void loadAuthUrl(String url) {
        url = WebViewHelper.appendGAClientIdAsQueryParam(url, getContext());
        loadUrl(url, getWebviewHeaders(url));
    }

    public void loadOtherUrl(String url) {
        url = WebViewHelper.appendGAClientIdAsQueryParam(url, getContext());
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