package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author by milhamj on 04/04/18.
 */

public class UserSession {
    private static final String DEFAULT_EMPTY_SHOP_ID = "0";
    private static final String DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String SHOP_ID = "SHOP_ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PROFILE_PICTURE = "PROFILE_PICTURE";

    private Context context;

    public UserSession(Context context) {
        this.context = context;
    }

    public String getAccessToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public String getFreshToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public String getUserId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(LOGIN_ID, "");
    }

    public boolean isLoggedIn() {
        String u_id;
        boolean isLogin;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        isLogin = sharedPrefs.getBoolean(IS_LOGIN, false);
        return isLogin && u_id != null;
    }

    public String getShopId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        String shopId = sharedPrefs.getString(SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    public boolean hasShop() {
        return !TextUtils.isEmpty(getShopId()) && !DEFAULT_EMPTY_SHOP_ID.equals(getShopId());
    }

    public String getName() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(FULL_NAME, null);
    }

    public String getProfilePicture() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(PROFILE_PICTURE, "");
    }
}
