package com.tokopedia.core.util;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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

    public void loadAuthUrl(String url) {
        loadUrl(url,
                AuthUtil.generateHeaders(
                        Uri.parse(url).getPath(),
                        getQuery(Uri.parse(url).getQuery()),
                        "GET",
                        AuthUtil.KEY.KEY_WSV4));
    }

    private String getQuery(String query) {
        return query != null ? query : "";
    }

    public void loadAuthUrlWithFlags(String url) {
        loadAuthUrl(generateUri(url));
    }

    private String generateUri(String uri) {
        String url = String.valueOf(uri);
        String flag_app = "flag_app=1";
        String device = "device=android";
        String utm_source = "utm_source=android";
        String app_version = "app_version=" + GlobalConfig.VERSION_CODE;
        String flags = flag_app
                + "&" + device
                + "&" + utm_source
                + "&" + app_version;

        try {
            if (Uri.parse(uri).getQuery() == null) {
                url += "?" + URLEncoder.encode(flags, FORMAT_UTF_8);
            } else if (isSeamlessUrl(uri)
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