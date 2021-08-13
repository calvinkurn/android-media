package com.tokopedia.webview;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.authentication.AuthConstant;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.authentication.AuthKey;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.utils.URLGenerator;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.view.DarkModeUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static com.tokopedia.abstraction.common.utils.network.AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE;

/**
 * Created by nisie on 11/30/16.
 */
public class TkpdWebView extends WebView {

    private static final String PARAM_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String HEADER_TKPD_SESSION_ID = "tkpd-sessionid";
    private static final String HEADER_TKPD_SESSION_ID2 = "Tkpd-SessionId";
    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";
    private static final String HEADER_DARK_MODE = "x-dark-mode";
    private static final String HEADER_TKPD_USER_AGENT = "tkpd-useragent";
    private RemoteConfig remoteConfig;
    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";

    private @Nullable
    TkpdWebView.WebviewScrollListener scrollListener = null;

    public TkpdWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TkpdWebView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TkpdWebView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface WebviewScrollListener {
        void onTopReached();

        void onEndReached();

        void onHasScrolled();
    }

    private void init(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
        //set custom tracking, helpful for GA
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CUSTOMER_USER_AGENT_IN_WEBVIEW, true)) {
            WebSettings webSettings = getSettings();
            String userAgent = String.format("%s - Android %s", "Tokopedia Webview", GlobalConfig.VERSION_NAME);
            webSettings.setUserAgentString(userAgent);
        }
        SplitCompat.installActivity(context);
    }

    public void setWebViewScrollListener(@Nullable TkpdWebView.WebviewScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    /**
     * load url with the header for identification and security
     * isUseFlag=true will add custom query parameter
     */
    public void loadAuthUrl(@NonNull String url, @Nullable UserSessionInterface userSession, boolean isUseFlag) {
        url = WebViewHelper.appendGAClientIdAsQueryParam(url, getContext());

        String urlToLoad;
        if (isUseFlag) {
            urlToLoad = generateUri(url);
        } else {
            urlToLoad = url;
        }
        if (userSession == null) {
            loadUrl(urlToLoad);
        } else {
            String path = Uri.parse(url).getPath();
            if (path == null) {
                path = "";
            }
            Map<String, String> header = AuthHelper.getDefaultHeaderMapOld(
                    path,
                    getQuery(Uri.parse(url).getQuery()),
                    "GET",
                    AuthConstant.CONTENT_TYPE,
                    AuthKey.KEY_WSV4,
                    AuthConstant.DATE_FORMAT,
                    userSession.getUserId(),
                    userSession);
            String deviceId = userSession.getDeviceId();
            header.put(HEADER_TKPD_SESSION_ID, deviceId);
            header.put(HEADER_TKPD_SESSION_ID2, deviceId);
            header.put(HEADER_TKPD_USER_AGENT, DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE);
            header.put(HEADER_TKPD_USER_ID, userSession.getUserId());
            header.put(HEADER_DARK_MODE, String.valueOf(DarkModeUtil.isDarkMode(getContext())));
            FingerprintModel fingerprintModel = FingerprintModelGenerator.generateFingerprintModel(getContext().getApplicationContext());
            String hash = fingerprintModel.getFingerprintHash();
            header.put(
                    KEY_FINGERPRINT_DATA,
                    hash
            );
            header.put(
                    KEY_FINGERPRINT_HASH,
                    AuthHelper.Companion.getMD5Hash(hash + "+" + userSession.getUserId())
            );
            loadUrl(urlToLoad, header);
        }
    }

    /**
     * load url with the header for identification and security
     */
    public void loadAuthUrl(@NonNull String url, @Nullable UserSessionInterface userSession) {
        loadAuthUrl(url, userSession, false);
    }

    public void loadAuthUrlWithFlags(@NonNull String url, @Nullable UserSessionInterface userSession) {
        loadAuthUrl(url, userSession, true);
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

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        TkpdWebView.WebviewScrollListener listener = scrollListener;
        if (listener != null) {
            int height = (int) Math.floor(this.getContentHeight() * this.getScale());
            int webViewHeight = this.getMeasuredHeight();
            if (this.getScrollY() == 0) {
                listener.onTopReached();
            } else if (this.getScrollY() + webViewHeight >= height) {
                listener.onEndReached();
            } else if (this.getScaleY() > 0) {
                listener.onHasScrolled();
            }
        }
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

    private boolean isSeamlessUrl(String uri) {
        return uri.startsWith(URLGenerator.getBaseUrl());
    }

    @Override
    public void loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders) {
        if (WebViewHelper.isUrlValid(url)) {
            super.loadUrl(url, additionalHttpHeaders);
        } else {
            if (!GlobalConfig.DEBUG)
                FirebaseCrashlytics.getInstance().log(
                        getContext().getString(R.string.error_message_url_invalid_crashlytics) + url);

            super.loadUrl(url);
        }
    }
}