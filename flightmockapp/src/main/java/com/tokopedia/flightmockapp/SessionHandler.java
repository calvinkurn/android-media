package com.tokopedia.flightmockapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

import java.util.Arrays;

public class SessionHandler {
    private static final String SAVE_REAL = "SAVE_REAL";
    private static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    public static final String DONT_REMIND_LATER = "DONT_REMIND_LATER";
    public static final String CACHE_PROMOTION_PRODUCT = "CACHE_PROMOTION_PRODUCT";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String TEMP_PHONE_NUMBER = "TEMP_PHONE_NUMBER";
    private static final String TEMP_NAME = "TEMP_NAME";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String GTM_LOGIN_ID = "GTM_LOGIN_ID";
    private static final String SHOP_ID = "SHOP_ID";
    private static final String STATE_BROWSE = "STATE_BROWSE";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String USER_AVATAR_URI = "USER_AVATAR_URI";
    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String IS_FIRST_TIME_USER = "IS_FIRST_TIME";
    private static final String MSISDN_SESSION = "MSISDN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String WALLET_REFRESH_TOKEN = "WALLET_REFRESH_TOKEN";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String IS_FIRST_TIME_STORAGE = "IS_FIRST_TIME_STORAGE";
    private static final String LOGIN_UUID_KEY = "LOGIN_UUID";
    private static final String UUID_KEY = "uuid";
    private static final String DEFAULT_UUID_VALUE = "";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String USER_DATA = "USER_DATA";
    private static final String KEY_IV = "tokopedia1234567";


    public static String getAccessToken() {
        SharedPreferences sharedPrefs = BaseMainApplication.getAppContext().getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

}
