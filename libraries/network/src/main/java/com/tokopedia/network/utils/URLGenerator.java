package com.tokopedia.network.utils;

import com.tokopedia.url.TokopediaUrl;

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

    public static String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getJS();
    }

}
