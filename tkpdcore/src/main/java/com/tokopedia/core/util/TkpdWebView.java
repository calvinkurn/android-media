package com.tokopedia.core.util;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

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

    @Override
    public void loadUrl(String url) {
        super.loadUrl(generateUri(url));
    }

    public void loadAuthUrl(String url) {
        super.loadUrl(generateUri(url),
                AuthUtil.generateHeaders(
                        Uri.parse(url).getPath(),
                        Uri.parse(generateUri(url)).getQuery(),
                        "GET",
                        AuthUtil.KEY.KEY_WSV4));
    }

    private String generateUri(String uri) {
        String url = String.valueOf(uri);
        url += "?flag_app%3D1%26device%3Dandroid%26utm_source%3Dandroid%26app_version%3D" + GlobalConfig.VERSION_CODE;
        return url;
    }
}