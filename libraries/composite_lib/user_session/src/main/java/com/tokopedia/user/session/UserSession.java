package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author by milhamj on 04/04/18.
 * Please avoid using this class to get data. Use {@link UserSessionInterface} instead.
 */

public class UserSession implements UserSessionInterface {
    private static final String DEFAULT_EMPTY_SHOP_ID = "0";
    private static final String DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String GTM_LOGIN_ID = "GTM_LOGIN_ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    private static final String EMAIL = "EMAIL";
    private static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    private static final String IS_AFFILIATE = "is_affiliate";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String GC_TOKEN = "GC_TOKEN";

    private static final String TEMP_USER_ID = "temp_login_id";
    private static final String TEMP_EMAIL = "TEMP_EMAIL";
    private static final String TEMP_PHONE_NUMBER = "TEMP_PHONE_NUMBER";
    private static final String TEMP_NAME = "TEMP_NAME";

    private static final String GCM_STORAGE = "GCM_STORAGE";
    private static final String GCM_ID = "gcm_id";

    private static final String LOGIN_UUID_KEY = "LOGIN_UUID";
    private static final String UUID_KEY = "uuid";

    private static final String SHOP_ID = "SHOP_ID";
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String SHOP_AVATAR = "SHOP_AVATAR";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    private static final String IS_POWER_MERCHANT_IDLE = "IS_POWER_MERCHANT_IDLE";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String KEY_IV = "tokopedia1234567";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String IS_FIRST_TIME_USER = "IS_FIRST_TIME";
    private static final String IS_FIRST_TIME_USER_NEW_ONBOARDING = "IS_FIRST_TIME_NEW_ONBOARDING";
    private static final String HAS_PASSWORD = "HAS_PASSWORD";
    private static final String HAS_SHOWN_SALDO_WARNING = "HAS_SHOWN_SALDO_WARNING";
    private static final String HAS_SHOWN_SALDO_INTRO_PAGE = "HAS_SHOWN_SALDO_INTRO_PAGE";
    private static final String AUTOFILL_USER_DATA = "AUTOFILL_USER_DATA";
    private static final String LOGIN_METHOD = "LOGIN_METHOD";

    /**
     * Twitter Prefs
     */
    private static final String TWITTER_ACCESS_TOKEN = "TWITTER_ACCESS_TOKEN";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "TWITTER_ACCESS_TOKEN_SECRET";
    private static final String TWITTER_SHOULD_POST = "TWITTER_SHOULD_POST";

    private Context context;

    public UserSession(Context context) {
        this.context = context;
    }

    /**
     * GETTER METHOD
     */
    private static final String TWITTER_ACCESS_TOKEN = "TWITTER_ACCESS_TOKEN";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "TWITTER_ACCESS_TOKEN_SECRET";
    private static final String TWITTER_SHOULD_POST = "TWITTER_SHOULD_POST";

    public String getAccessToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "").trim();
    }

    public String getTokenType() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(TOKEN_TYPE, "Bearer").trim();
    }

    public String getFreshToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "").trim();
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
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId) || TextUtils.isEmpty(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    @Override
    public String getShopName() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(SHOP_NAME, "");
    }

    @Override
    public boolean hasShop() {
        return !TextUtils.isEmpty(getShopId()) && !DEFAULT_EMPTY_SHOP_ID.equals(getShopId());
    }

    @Override
    @Nullable
    public String getGCToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(GC_TOKEN, "");
    }

    @Override
    public boolean isGoldMerchant() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_GOLD_MERCHANT, false);
    }

    public String getName() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(FULL_NAME, "");
    }

    public String getProfilePicture() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(PROFILE_PICTURE, "");
    }

    public String getTemporaryUserId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_USER_ID, "");
    }

    /**
     * Saved from FCMCacheManager
     *
     * @return gcm id / device id
     */
    public String getDeviceId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(GCM_STORAGE, Context.MODE_PRIVATE);
        return sharedPrefs.getString(GCM_ID, "");
    }

    public String getTempEmail() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_EMAIL, "");
    }

    public String getTempPhoneNumber() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_PHONE_NUMBER, "");
    }

    public boolean isMsisdnVerified() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_MSISDN_VERIFIED, false);
    }

    public boolean isAffiliate() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_AFFILIATE, false);
    }

    public boolean hasShownSaldoWithdrawalWarning() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(HAS_SHOWN_SALDO_WARNING, false);
    }

    public boolean hasShownSaldoIntroScreen() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(HAS_SHOWN_SALDO_INTRO_PAGE, false);
    }

    public String getPhoneNumber() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(PHONE_NUMBER, "");
    }

    public String getEmail() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(EMAIL, "");
    }

    @Override
    public boolean isFirstTimeUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME_USER_NEW_ONBOARDING, true);
    }

    public boolean hasPassword() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(HAS_PASSWORD, true);
    }

    @Override
    public String getShopAvatar() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(SHOP_AVATAR, "");
    }

    @Override
    public boolean isPowerMerchantIdle() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_POWER_MERCHANT_IDLE, false);
    }

    @Override
    public String getAutofillUserData() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(AUTOFILL_USER_DATA, "");
    }

    @Override
    public String getLoginMethod() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(LOGIN_METHOD, "");
    }

    @Nullable
    @Override
    public String getTwitterAccessToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TWITTER_ACCESS_TOKEN, null);
    }

    @Nullable
    @Override
    public String getTwitterAccessTokenSecret() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TWITTER_ACCESS_TOKEN_SECRET, null);
    }

    @Override
    public boolean getTwitterShouldPost() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(TWITTER_SHOULD_POST, false);
    }

    /**
     * SETTER METHOD
     */

    public void setUUID(String uuid) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_UUID_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        String prevUUID = sharedPrefs.getString(UUID_KEY, "");
        String currUUID;
        if (prevUUID.equals("")) {
            currUUID = uuid;
        } else {
            currUUID = prevUUID + "*~*" + uuid;
        }
        editor.putString(UUID_KEY, currUUID);
        editor.apply();
    }

    public void setIsLogin(boolean isLogin) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, userId);
        editor.putString(GTM_LOGIN_ID, userId);

        editor.apply();
    }

    public void setName(String fullName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(FULL_NAME, fullName);
        editor.apply();
    }

    public void setEmail(String email) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void setPhoneNumber(String phoneNumber) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    public void setShopId(String shopId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(SHOP_ID, shopId);
        editor.apply();
    }

    public void setShopName(String shopName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(SHOP_NAME, shopName);
        editor.apply();
    }

    public void setIsGoldMerchant(boolean isGoldMerchant) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        editor.apply();
    }


    public void setTempLoginName(String fullName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_NAME, fullName);
        editor.apply();
    }

    public void setTempUserId(String userId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_USER_ID, userId);
        editor.apply();
    }

    public String getRefreshTokenIV() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, KEY_IV);
    }

    public void clearToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TOKEN_TYPE, null);
        editor.putString(ACCESS_TOKEN, null);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(TOKEN_TYPE, tokenType);
        editor.apply();
    }

    @Override
    public void setFirstTimeUser(boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER, isFirstTime).apply();
    }

    @Override
    public void setFirstTimeUserOnboarding(boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER_NEW_ONBOARDING, isFirstTime).apply();
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(TOKEN_TYPE, tokenType);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    @Override
    public void setLoginSession(boolean isLogin, String userId, String fullName,
                                String shopId, boolean isMsisdnVerified, String shopName,
                                String email, boolean isGoldMerchant, String phoneNumber) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(LOGIN_ID, userId);
        editor.putString(GTM_LOGIN_ID, userId);
        editor.putString(FULL_NAME, fullName);
        editor.putString(SHOP_ID, shopId);
        editor.putString(SHOP_NAME, shopName);
        editor.putString(EMAIL, email);
        editor.putBoolean(IS_MSISDN_VERIFIED, isMsisdnVerified);
        editor.putBoolean(HAS_SHOWN_SALDO_WARNING, false);
        editor.putBoolean(HAS_SHOWN_SALDO_INTRO_PAGE, false);
        editor.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        editor.putString(PHONE_NUMBER, phoneNumber);

        editor.apply();
    }

    @Override
    public void setIsMSISDNVerified(boolean isMsisdnVerified) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_MSISDN_VERIFIED, isMsisdnVerified);
        editor.apply();
    }

    @Override
    public void setIsAffiliateStatus(boolean isAffiliate) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_AFFILIATE, isAffiliate);
        editor.apply();
    }

    @Override
    public void setTempPhoneNumber(String userPhone) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_PHONE_NUMBER, userPhone);
        editor.apply();

    }

    @Override
    public void setTempLoginEmail(String email) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_EMAIL, email);
        editor.apply();
    }

    @Override
    public void setHasPassword(boolean hasPassword) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(HAS_PASSWORD, hasPassword);
        editor.apply();

    }

    @Override
    public void setSaldoWithdrawalWaring(boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(HAS_SHOWN_SALDO_WARNING, value);
        editor.apply();
    }

    @Override
    public void setSaldoIntroPageStatus(boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(HAS_SHOWN_SALDO_INTRO_PAGE, value);
        editor.apply();
    }

    @Override
    public void setProfilePicture(String profilePicture) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PROFILE_PICTURE, profilePicture);
        editor.apply();
    }

    @Override
    public void setGCToken(String gcToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(GC_TOKEN, gcToken);
        editor.apply();
    }

    @Override
    public void setShopAvatar(String shopAvatar) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(SHOP_AVATAR, shopAvatar);
        editor.apply();
    }

    @Override
    public void setIsPowerMerchantIdle(boolean powerMerchantIdle) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_POWER_MERCHANT_IDLE, powerMerchantIdle);
        editor.apply();
    }

    @Override
    public void setAutofillUserData(String autofillUserData) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(AUTOFILL_USER_DATA, autofillUserData);
        editor.apply();
    }

    @Override
    public void setLoginMethod(@NotNull String loginMethod) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_METHOD, loginMethod);
        editor.apply();
    }

    @Override
    public void setTwitterAccessTokenAndSecret(@NotNull String accessToken, @NotNull String accessTokenSecret) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TWITTER_ACCESS_TOKEN, accessToken);
        editor.putString(TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret);
        editor.apply();
    }

    @Override
    public void setTwitterShouldPost(boolean shouldPost) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(TWITTER_SHOULD_POST, shouldPost);
        editor.apply();
    }

    public void logoutSession() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, null);
        editor.putString(FULL_NAME, null);
        editor.putString(SHOP_ID, null);
        editor.putString(SHOP_NAME, null);
        editor.putBoolean(IS_LOGIN, false);
        editor.putBoolean(IS_MSISDN_VERIFIED, false);
        editor.putBoolean(HAS_SHOWN_SALDO_WARNING, false);
        editor.putBoolean(IS_AFFILIATE, false);
        editor.putString(PHONE_NUMBER, null);
        editor.putString(REFRESH_TOKEN, null);
        editor.putString(TOKEN_TYPE, null);
        editor.putString(ACCESS_TOKEN, null);
        editor.putString(PROFILE_PICTURE, null);
        editor.putString(GC_TOKEN, "");
        editor.putString(SHOP_AVATAR, "");
        editor.putBoolean(IS_POWER_MERCHANT_IDLE, false);
        editor.putString(TWITTER_ACCESS_TOKEN, null);
        editor.putString(TWITTER_ACCESS_TOKEN_SECRET, null);
        editor.putBoolean(TWITTER_SHOULD_POST, false);
        editor.putString(LOGIN_METHOD, "");
        editor.apply();
    }
}
