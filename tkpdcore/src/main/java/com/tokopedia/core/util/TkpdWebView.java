package com.tokopedia.core.util;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core2.R;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.webview.WebViewHelper;

import java.util.Map;

/**
 * Created by nisie on 11/30/16.
 * refer {@link com.tokopedia.abstraction.base.view.webview.TkpdWebView}
 */
@Deprecated
public class TkpdWebView extends WebView {

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

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (WebViewHelper.isUrlValid(url)) {
            super.loadUrl(url, additionalHttpHeaders);
        } else {
            if (!GlobalConfig.DEBUG)
                FirebaseCrashlytics.getInstance().log(getContext().getString(R.string.error_message_url_invalid_crashlytics) + url);

            super.loadUrl(url);
        }
    }
}