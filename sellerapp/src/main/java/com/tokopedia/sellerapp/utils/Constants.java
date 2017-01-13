package com.tokopedia.sellerapp.utils;

/**
 * Created by normansyahputa on 12/21/16.
 */

public class Constants {
    public static String BASE_URL = "https://ws.tokopedia.com";
    public static final String GET_INBOX_MESSAGE_API = BASE_URL + "inbox-message/get_inbox_message.pl";
    public static final String GET_INBOX_TALK_API = BASE_URL + "inbox-talk/get_inbox-talk.pl";
    public static final String GET_LOGIN_API = BASE_URL + "session/login.pl";
    public static final Integer CONNECTION_TIMEOUT_REAL = 120;
    public static final Integer CONNECTION_TIMEOUT_TEST = 1;
    public static Integer CONNECTION_TIMEOUT = CONNECTION_TIMEOUT_REAL;

    public static final int SUCCESS  = 1;
    public static final int FAILURE = 0;
    public static final String APP_TYPE_FACEBOOK  = "1";
    public static final String APP_TYPE_GOOGLE_PLUS  = "2";
    public static final String OS_TYPE_ANDROID  = "1";
    public static final int REQUEST_CODE_ASK_ACCOUNT_PERMISSION = 101;
}
