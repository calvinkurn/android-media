package com.tokopedia.imagepicker.picker.instagram.util;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramConstant {
    public static final String CLIENT_ID = "50dd2baace254c3dadcb9c876167baef";
    public static final String CLIENT_SECRET = "ac319756c2364c5ea70c53142f800d63";
    public static final String CALLBACK_URL = "https://www.tokopedia.com";

    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String CODE_KEY = "code";
    public static final String COOKIES_KEY = "cookies";
    public static final String SESSIONID = "sessionid";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String NEXT_MAX_ID_KEY = "next_max_id";
    public static final String PER_PAGE_MEDIA = "21";
    public static final String INSTAGRAM_CACHE_KEY = "instagram_cache_key";
    public static final String URL_API_INSTAGRAM = "https://api.instagram.com";
    public static final java.lang.String URL_LOGIN_INSTAGRAM = "https://api.instagram.com/oauth/authorize/?client_id="
            + CLIENT_ID + "&redirect_uri=" + CALLBACK_URL + "&response_type=code&scope=basic+public_content";
    public static final int SPAN_COUNT = 3;
    public static final int SPAN_LOOK_UP = 1;
    public static final String URL_PATH_GET_LIST_MEDIA = "/v1/users/self/media/recent";
    public static final String EXTRA_CODE_LOGIN = "extra_code_login";
}
