package com.tokopedia.core.session.presenter;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public interface Login {
    String TAG = "MNORMANSYAH";
    String messageTAG = "Login : ";

    String LOGIN_CACHE_KEY = "LOGIN_ID";
    String LOGIN_UUID_KEY = "LOGIN_UUID";
    String USER_EMAIL = "user_email";
    String USER_PASSWORD = "user_password";
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";
    String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
    String GO_TO_INDEX_KEY = "login";
    String PROVIDER_LIST = "provider";
    String PROVIDER_CACHE_KEY = "provider_cache";
    String BACKGROUND_CACHE_KEY = "background_cache_key";

    int EmailType = 0;
    int FacebookType = 1;
    int GooglePlusType = 2;
    int WebViewType = 3;

    String LOGIN_VIEW_MODEL_TAG = "LoginViewModel";
    String LOGIN_PROVIDER_TAG = "LoginProvider";

    String APP_TYPE = "app_type";
    String BIRTHDAY = "birthday";
    String EMAIL = "email";
    String GENDER = "gender";
    String GOOGLE_ID = "id";
    String NAME = "name";
    String PICTURE = "picture";

    String EDUCATION = "education";
    String FACEBOOK_TOKEN = "fb_token";
    String FACEBOOK_ID = "id";
    String INTERESTS = "interests";
    String WORK = "work";

    //------ACCOUNTS------
    String USER_NAME = "username";
    String PASSWORD = "password";
    String USER_ID = "user_id";
    String GRANT_TYPE = "grant_type";
    String SOCIAL_ID = "social_id";
    String SOCIAL_TYPE = "social_type";
    String EMAIL_ACCOUNTS = "email";
    String PICTURE_ACCOUNTS = "picture";
    String FULL_NAME = "full_name";
    String BIRTHDATE = "birthdate";
    String GENDER_ACCOUNTS = "gender";
    String CODE = "code";
    String REDIRECT_URI = "redirect_uri";
    String MSISDN = "msisdn";

    String GRANT_PASSWORD = "password";
    String GRANT_SDK = "extension";
    String GRANT_WEBVIEW = "authorization_code";

    //------________------

}
