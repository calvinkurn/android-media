package com.tokopedia.sessioncommon.data;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by nisie on 10/12/18.
 */
public class SessionCommonUrl {
    public static String BASE_WS_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static final String PATH_GET_TOKEN = "token";
    public static final String PATH_MAKE_LOGIN = "v4/session/make_login.pl";
    public static final String PATH_GET_INFO = "info";

}
