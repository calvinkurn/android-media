package com.tokopedia.core.util;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by nisie on 11/30/16.
 */
public class TkpdWebView extends WebView {

    public TkpdWebView(Context context) {
        super(context);
    }

    public TkpdWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TkpdWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadUrlWithFlags(String url){
        loadUrl(generateUri(url));
    }

    public void loadAuthUrl(String url) {
        loadUrl(generateUri(url),
                AuthUtil.generateHeaders(
                        Uri.parse(url).getPath(),
                        Uri.parse(generateUri(url)).getQuery(),
                        "GET",
                        AuthUtil.KEY.KEY_WSV4));
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
            url += "?" + URLEncoder.encode(flags, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}