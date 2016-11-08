package com.tokopedia.core.loyaltysystem.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class URLGenerator {

    public static String generateURLLucky(String url, Context context) {
        Uri uri = Uri.parse(url);
        String path = uri.getLastPathSegment();
        String queryStart = uri.getQuery();
        String urlFinal = getBaseUrl() + "wvlogin?token="
                + PasswordGenerator.getAppId(context)
                + "&uid=" + SessionHandler.getLoginID(context)
                + "&url=" + url;
        Log.i("Loyalty System", url);
        return urlFinal;
    }

    public static String generateURLSessionLogin(String url, Context context) {
        Uri uri = Uri.parse(url);
        String path = uri.getLastPathSegment();
        String queryStart = uri.getQuery();
        String urlFinal = getBaseUrl() + "wvlogin?token="
                + PasswordGenerator.getAppId(context)
                + "&uid=" + SessionHandler.getLoginID(context)
                + "&url=" + url;
        return urlFinal;
    }

    public static String generateURLSessionLoginV4(String url, Context context) {
        Uri uri = Uri.parse(url);
        String path = uri.getLastPathSegment();
        String queryStart = uri.getQuery();
        String urlFinal = getBaseUrl() + "wvlogin?token="
                + GCMHandler.getRegistrationId(context)
                + "&uid=" + SessionHandler.getLoginID(context)
                + "&url=" + url;
        return urlFinal;
    }

    private static String getBaseUrl() {
        String baseUrl = TkpdBaseURL.JS_DOMAIN;
        if (BuildConfig.DEBUG) {
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("alpha")){
                baseUrl = TkpdBaseURL.JS_ALPHA_DOMAIN;
            } else if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("staging")) {
                baseUrl = TkpdBaseURL.JS_STAGING_DOMAIN;;
            }
        }
        return baseUrl;
    }
}
