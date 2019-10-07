package com.tokopedia.abstraction.base.view.webview;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebSettings;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 11/30/16.
 */
@Deprecated
public class TkpdWebView extends WebView {

    private static final String PARAM_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String GET = "GET";

    private WebviewScrollListener scrollListener = null;

    public interface WebviewScrollListener {
        void onTopReached();

        void onEndReached();

        void onHasScrolled();

    }

    public TkpdWebView(Context context) {
        super(context);
        init();
    }

    public TkpdWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TkpdWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //set custom tracking, helpful for GA
        WebSettings webSettings = getSettings();
        String userAgent = String.format("%s - %s]","Tokopedia Webview", GlobalConfig.VERSION_NAME);
        webSettings.setUserAgentString(userAgent);
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
                    GET,
                    AuthUtil.KEY.KEY_WSV4, userId, accessToken));
        }
    }

    public void loadAuthUrl(String url, String userId, String accessToken) {
        if (TextUtils.isEmpty(userId)) {
            loadUrl(url);
        } else {
            loadUrl(url, AuthUtil.generateHeadersWithBearer(
                    Uri.parse(url).getPath(),
                    getQuery(Uri.parse(url).getQuery()),
                    GET,
                    AuthUtil.KEY.KEY_WSV4, userId, accessToken));
        }
    }

    public void loadAuthUrl(String url, String userId, String accessToken, HashMap<String,
            String> additionalHeaders) {
        if (TextUtils.isEmpty(userId)) {
            loadUrl(url);
        } else {
            Map<String, String> header = AuthUtil.generateHeadersWithBearer(
                    Uri.parse(url).getPath(),
                    getQuery(Uri.parse(url).getQuery()),
                    GET,
                    AuthUtil.KEY.KEY_WSV4, userId, accessToken);
            header.putAll(additionalHeaders);
            loadUrl(url, header);
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


    public void setWebviewScrollListener(WebviewScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int height = (int) Math.floor(this.getContentHeight() * this.getScale());
        int webViewHeight = this.getMeasuredHeight();
        if (this.getScrollY() == 0) {
            if (scrollListener != null) {
                scrollListener.onTopReached();
            }
        } else if (this.getScrollY() + webViewHeight >= height) {
            if (scrollListener != null) {
                scrollListener.onEndReached();
            }
        } else if (this.getScaleY() > 0) {
            if (scrollListener != null) {
                scrollListener.onHasScrolled();
            }
        }
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if(WebViewHelper.isUrlValid(url)){
            super.loadUrl(url, additionalHttpHeaders);
        }else {
            if(!GlobalConfig.DEBUG)
                Crashlytics.log(
                    getContext().getString(R.string.error_message_url_invalid_crashlytics) + url);

            super.loadUrl(url);
        }
    }
}