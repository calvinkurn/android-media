package com.tokopedia.otp.cotp.data;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by nisie on 4/25/18.
 */

public class SQLoginUrl {
    static final String PATH_MAKE_LOGIN = "v4/session/make_login.pl";

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getWS();
}
