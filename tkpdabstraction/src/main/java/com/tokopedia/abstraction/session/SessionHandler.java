package com.tokopedia.abstraction.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.tokopedia.abstraction.MainApplication;
import com.tokopedia.abstraction.analytics.TrackingUtils;
import com.tokopedia.abstraction.utils.LocalCacheHandler;

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


    private Context context;


    public SessionHandler(Context context) {
        this.context = context;
    }

    public static String getTempLoginSession(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString("temp_login_id", "");

    }

    /**
     * @param context Non Null context
     * @param isLogin flag to determine user is login or not
     * @param user_id valid user id
     */
    @SuppressWarnings("unused")
    public static void setIsLogin(Context context, boolean isLogin, int user_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, user_id + "");
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();
        TrackingUtils.eventPushUserID();
    }

    public static String getLoginID(Context context) {
        String u_id;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, "");
        return u_id;
    }

    public static String getGTMLoginID(Context context) {
        String u_id;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(GTM_LOGIN_ID, "");
        if (TextUtils.isEmpty(u_id)) {
            if (!TextUtils.isEmpty(SessionHandler.getLoginID(context))) {
                SessionHandler.setGTMLoginID(context, SessionHandler.getLoginID(context));
                return SessionHandler.getLoginID(context);
            } else {
                return "";
            }

        } else {
            return u_id;
        }
    }

    public static void setGTMLoginID(Context context, String userID) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(GTM_LOGIN_ID, userID).apply();
    }

    public static void setShopDomain(Context context, String domain) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(SHOP_DOMAIN, domain).apply();
    }

    public static String getShopDomain(Context context) {
        String domain = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        domain = sharedPrefs.getString(SHOP_DOMAIN, "");
        return domain;
    }

    public void setShopId(String shopId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, SHOP_ID, shopId);
        editor.apply();
    }

    public static String getShopID(Context context) {
        String shop_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        shop_id = sharedPrefs.getString(SHOP_ID, "0");
        return shop_id;
    }

    public static String getLoginName(Context context) {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    public static boolean isGoldMerchant(Context context) {
        Boolean isGoldMerchant = false;
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        int isGM = sharedPrefs.getInt(IS_GOLD_MERCHANT, -1);
        isGoldMerchant = (isGM != (-1) && isGM != 0);
        return isGoldMerchant;
    }

    public static void setGoldMerchant(Context context, int goldMerchant) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        Editor edit = sharedPrefs.edit();
        edit.putInt(IS_GOLD_MERCHANT, goldMerchant);
        edit.apply();
    }

    /**
     * replacement of isLogin for v$ Login
     *
     * @param context
     * @return
     */
    public static boolean isV4Login(Context context) {
        String u_id = null;
        boolean isLogin = false;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        isLogin = sharedPrefs.getBoolean(IS_LOGIN, false);
        return isLogin && u_id != null;
    }

    public static boolean isV2Login(Context context) {
        String u_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        return u_id != null;
    }

    /**
     * login json currently give user id even in security question
     *
     * @param context Non Null context
     * @return always false
     */
//	public static Boolean isLogin (Context context) {
//		String u_id = null;
//		 SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
//		 u_id = sharedPrefs.getString(LOGIN_ID, null);
////		 if (u_id == null) {
//			 return false;
////		 }
////		 return true;
//	}
    public static void setUserAvatarUri(Context context, String avatar_uri) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(USER_AVATAR_URI, avatar_uri);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public static String getUserAvatarUri(Context context) {
        String avatar_uri = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        avatar_uri = sharedPrefs.getString(USER_AVATAR_URI, null);
        if (avatar_uri == null) {
            return null;
        }
        return avatar_uri;
    }

    @SuppressWarnings("unused")
    public static void setGridPref(Context context, int Pref) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putInt("STATE", Pref);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public static int getGridPref(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        return sharedPrefs.getInt("STATE", 1);
    }

    public static void deleteRegisterNext(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static boolean isRegisterNextEnter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE_REAL, false);
    }

    public static boolean isFirstTimeUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME_USER, true);
    }

    public static boolean setFirstTimeUser(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER, isFirstTime).commit();
    }

    public static boolean isMsisdnVerified() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        return cache.getBoolean(IS_MSISDN_VERIFIED, false);
    }

    public static void setIsMSISDNVerified(boolean verified) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putBoolean(IS_MSISDN_VERIFIED, verified);
        cache.applyEditor();
    }

    public static String getPhoneNumber() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        return cache.getString(PHONE_NUMBER, "");
    }

    public static void setPhoneNumber(String userPhone) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putString(PHONE_NUMBER, userPhone);
        cache.applyEditor();
    }

    public static String getTempPhoneNumber(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_PHONE_NUMBER, "");
    }

    public static String getTempLoginName(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_NAME, "");
    }

    public static String getAccessToken() {
        SharedPreferences sharedPrefs = MainApplication.getAppContext().getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public static String getRefreshTokenIV(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, KEY_IV);
    }

    public static boolean isFirstTimeAskedPermissionStorage(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_FIRST_TIME_STORAGE, true);
    }

    public static boolean isUserSeller(Context context) {
        return !SessionHandler.getShopID(context).isEmpty() && !SessionHandler.getShopID(context).equals("0");
    }

    public static String getUUID(Context context) {
        return new LocalCacheHandler(context, LOGIN_UUID_KEY)
                .getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    public void setTempLoginSession(String u_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString("temp_login_id", u_id);
        editor.apply();
    }

    public boolean isV4Login() {
        return isV4Login(context);
    }

    public String getLoginID() {
        return getLoginID(context);
    }

    public String getShopID() {
        String shop_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        shop_id = sharedPrefs.getString(SHOP_ID, "0");
        return shop_id;
    }

    public String getLoginName() {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    @SuppressWarnings("unused")
    public void setLoginName(String u_name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(FULL_NAME, u_name);
        editor.apply();
    }

    public void setGoldMerchant(int goldMerchant) {
        SharedPreferences sharedPrefs = this.context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        Editor edit = sharedPrefs.edit();
        edit.putInt(IS_GOLD_MERCHANT, goldMerchant);
        edit.apply();
    }

    public void setTempPhoneNumber(String userPhone) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_PHONE_NUMBER, userPhone);
        editor.apply();
    }

    public void setTempLoginName(String userPhone) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_NAME, userPhone);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        setToken(accessToken, tokenType);
        saveToSharedPref(editor, REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, ACCESS_TOKEN, accessToken);
        saveToSharedPref(editor, TOKEN_TYPE, tokenType);
        editor.apply();
    }

    public void setWalletRefreshToken(String walletRefreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, WALLET_REFRESH_TOKEN, walletRefreshToken);
        editor.apply();
    }

    private void saveToSharedPref(Editor editor, String key, String value) {
        if (value != null) {
            editor.putString(key, value);
        }
    }

    public String getAccessToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public String getWalletRefreshToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(WALLET_REFRESH_TOKEN, "");
    }

    public String getTokenType(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TOKEN_TYPE, "");
    }

    public String getUUID() {
        return new LocalCacheHandler(context, LOGIN_UUID_KEY)
                .getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    public interface onLogoutListener {
        void onLogout(Boolean success);
    }
}
