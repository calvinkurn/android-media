package com.tokopedia.abstraction.common.utils.network;

import android.content.Context;
import android.net.Uri;

import com.tokopedia.abstraction.constant.AbstractionBaseURL;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class URLGenerator {

    private static final String SEAMLESS_LOGIN = "seamless?";

    public static String generateURLSessionLogin(String url, String fcmTokenId, String uid) {
        return getBaseUrl() + SEAMLESS_LOGIN
                + "token=" + fcmTokenId
                + "&os_type=1"
                + "&uid=" + uid
                + "&url=" + url;
    }

    public static String generateURLSessionLoginV4(String url, Context context) {
        /*Uri uri = Uri.parse(url);
        String path = uri.getLastPathSegment();
        String queryStart = uri.getQuery();
        String urlFinal = getBaseUrl() + SEAMLESS_LOGIN
                + "token=" + GCMHandler.getRegistrationId(context)
                + "&os_type=1"
                + "&uid=" + SessionHandler.getLoginID(context)
                + "&url=" + url;
        return urlFinal;*/
        return "";

    }

    public static String getBaseUrl() {
        return AbstractionBaseURL.JS_DOMAIN;
    }

}
