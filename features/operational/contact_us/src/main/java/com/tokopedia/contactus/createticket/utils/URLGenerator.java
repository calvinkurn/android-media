package com.tokopedia.contactus.createticket.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.BuildConfig;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class URLGenerator {

    private static final String SEAMLESS_LOGIN = "seamless?";

    public static String generateURLContactUs(String url, @NonNull Context context) {
        UserSessionInterface userSession = new UserSession(context);
        return getBaseUrl(context) + SEAMLESS_LOGIN
                + "token=" + GCMHandler.getRegistrationId(context)
                + "&os_type=1"
                + "&uid=" + userSession.getUserId()
                + "&url=" + url;
    }

    public static String getBaseUrl(@NonNull Context context) {
        String baseUrl = TkpdBaseURL.JS_DOMAIN;
        if (BuildConfig.DEBUG) {
            SharedPreferences pref = context
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            if (pref.getString("DOMAIN_WS4", TokopediaUrl.Companion.getInstance().getWS()).contains("alpha")) {
                baseUrl = TkpdBaseURL.JS_ALPHA_DOMAIN;
            } else if (pref.getString("DOMAIN_WS4", TokopediaUrl.Companion.getInstance().getWS()).contains("staging")) {
                baseUrl = TkpdBaseURL.JS_STAGING_DOMAIN;
            }
        }
        return baseUrl;
    }
}