package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.tokopedia.user.session.Constants.ACCESS_TOKEN;
import static com.tokopedia.user.session.Constants.AUTOFILL_USER_DATA;
import static com.tokopedia.user.session.Constants.EMAIL;
import static com.tokopedia.user.session.Constants.FULL_NAME;
import static com.tokopedia.user.session.Constants.GCM_ID;
import static com.tokopedia.user.session.Constants.GCM_STORAGE;
import static com.tokopedia.user.session.Constants.GC_TOKEN;
import static com.tokopedia.user.session.Constants.GTM_LOGIN_ID;
import static com.tokopedia.user.session.Constants.HAS_PASSWORD;
import static com.tokopedia.user.session.Constants.HAS_SHOWN_SALDO_INTRO_PAGE;
import static com.tokopedia.user.session.Constants.HAS_SHOWN_SALDO_WARNING;
import static com.tokopedia.user.session.Constants.IS_AFFILIATE;
import static com.tokopedia.user.session.Constants.IS_FIRST_TIME_USER;
import static com.tokopedia.user.session.Constants.IS_FIRST_TIME_USER_NEW_ONBOARDING;
import static com.tokopedia.user.session.Constants.IS_GOLD_MERCHANT;
import static com.tokopedia.user.session.Constants.IS_LOGIN;
import static com.tokopedia.user.session.Constants.IS_MSISDN_VERIFIED;
import static com.tokopedia.user.session.Constants.IS_POWER_MERCHANT_IDLE;
import static com.tokopedia.user.session.Constants.LOGIN_ID;
import static com.tokopedia.user.session.Constants.LOGIN_METHOD;
import static com.tokopedia.user.session.Constants.LOGIN_SESSION;
import static com.tokopedia.user.session.Constants.LOGIN_UUID_KEY;
import static com.tokopedia.user.session.Constants.PHONE_NUMBER;
import static com.tokopedia.user.session.Constants.PROFILE_PICTURE;
import static com.tokopedia.user.session.Constants.REFRESH_TOKEN;
import static com.tokopedia.user.session.Constants.REFRESH_TOKEN_KEY;
import static com.tokopedia.user.session.Constants.SHOP_AVATAR;
import static com.tokopedia.user.session.Constants.SHOP_ID;
import static com.tokopedia.user.session.Constants.SHOP_NAME;
import static com.tokopedia.user.session.Constants.TEMP_EMAIL;
import static com.tokopedia.user.session.Constants.TEMP_NAME;
import static com.tokopedia.user.session.Constants.TEMP_PHONE_NUMBER;
import static com.tokopedia.user.session.Constants.TEMP_USER_ID;
import static com.tokopedia.user.session.Constants.TOKEN_TYPE;
import static com.tokopedia.user.session.Constants.TWITTER_ACCESS_TOKEN;
import static com.tokopedia.user.session.Constants.TWITTER_ACCESS_TOKEN_SECRET;
import static com.tokopedia.user.session.Constants.TWITTER_SHOULD_POST;
import static com.tokopedia.user.session.Constants.UUID_KEY;

/**
 * @author by milhamj on 04/04/18.
 * Please avoid using this class to get data. Use {@link UserSessionInterface} instead.
 */

public class UserSession extends MigratedUserSession implements UserSessionInterface {

    public static final String KEY_IV = "tokopedia1234567";
    private static final String DEFAULT_EMPTY_SHOP_ID = "0";
    private static final String DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1";


    public UserSession(Context context) {
        super(context);
    }

    public String getAccessToken() {
        return getAndTrimOldString(LOGIN_SESSION, ACCESS_TOKEN, "").trim();
    }

    public String getTokenType() {
        return getAndTrimOldString(LOGIN_SESSION, TOKEN_TYPE, "Bearer").trim();
    }

    public String getFreshToken() {
        return getAndTrimOldString(LOGIN_SESSION, REFRESH_TOKEN, "Bearer").trim();
    }

    public String getUserId() {
        return getAndTrimOldString(LOGIN_SESSION, LOGIN_ID, "");
    }

    public void setUserId(String userId) {
        setString(LOGIN_SESSION, LOGIN_ID, userId);
        setString(LOGIN_SESSION, GTM_LOGIN_ID, userId);
    }

    public boolean isLoggedIn() {
        String u_id = getAndTrimOldString(LOGIN_SESSION, LOGIN_ID, null);
        boolean isLogin = getAndTrimOldBoolean(LOGIN_SESSION, IS_LOGIN, false);
        return isLogin && u_id != null;
    }

    public String getShopId() {
        String shopId = getAndTrimOldString(LOGIN_SESSION, SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId) || TextUtils.isEmpty(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    public void setShopId(String shopId) {
        setString(LOGIN_SESSION, SHOP_ID, shopId);
    }

    @Override
    public String getShopName() {
        return getAndTrimOldString(LOGIN_SESSION, SHOP_NAME, "");
    }

    public void setShopName(String shopName) {
        setString(LOGIN_SESSION, SHOP_ID, shopName);
    }

    @Override
    public boolean hasShop() {
        return !TextUtils.isEmpty(getShopId()) && !DEFAULT_EMPTY_SHOP_ID.equals(getShopId());
    }

    @Override
    @Nullable
    public String getGCToken() {
        return getAndTrimOldString(LOGIN_SESSION, GC_TOKEN, "");
    }

    @Override
    public void setGCToken(String gcToken) {
        setString(LOGIN_SESSION, GC_TOKEN, gcToken);
    }

    @Override
    public boolean isGoldMerchant() {
        return getAndTrimOldBoolean(LOGIN_SESSION, IS_GOLD_MERCHANT, false);
    }

    public String getName() {
        return getAndTrimOldString(LOGIN_SESSION, FULL_NAME, "");
    }

    public void setName(String fullName) {
        setString(LOGIN_SESSION, FULL_NAME, fullName);
    }

    public String getProfilePicture() {
        return getAndTrimOldString(LOGIN_SESSION, PROFILE_PICTURE, "");
    }

    @Override
    public void setProfilePicture(String profilePicture) {
        setString(LOGIN_SESSION, PROFILE_PICTURE, profilePicture);
    }

    public String getTemporaryUserId() {
        return getAndTrimOldString(LOGIN_SESSION, TEMP_USER_ID, "");
    }

    /**
     * Saved from FCMCacheManager
     *
     * @return gcm id / device id
     */
    public String getDeviceId() {
        return getAndTrimOldString(GCM_STORAGE, GCM_ID, "");
    }

    public String getTempEmail() {
        return getAndTrimOldString(LOGIN_SESSION, TEMP_EMAIL, "");
    }

    public String getTempPhoneNumber() {
        return getAndTrimOldString(LOGIN_SESSION, TEMP_PHONE_NUMBER, "");
    }

    @Override
    public void setTempPhoneNumber(String userPhone) {
        setString(LOGIN_SESSION, TEMP_PHONE_NUMBER, userPhone);

    }

    public boolean isMsisdnVerified() {
        return getAndTrimOldBoolean(LOGIN_SESSION, IS_MSISDN_VERIFIED, false);
    }

    public boolean isAffiliate() {
        return getAndTrimOldBoolean(LOGIN_SESSION, IS_AFFILIATE, false);
    }

    public boolean hasShownSaldoWithdrawalWarning() {
        return getAndTrimOldBoolean(LOGIN_SESSION, HAS_SHOWN_SALDO_WARNING, false);
    }

    public boolean hasShownSaldoIntroScreen() {
        return getAndTrimOldBoolean(LOGIN_SESSION, HAS_SHOWN_SALDO_INTRO_PAGE, false);
    }

    @Override
    public boolean isFirstTimeUser() {
        return getAndTrimOldBoolean(LOGIN_SESSION, IS_FIRST_TIME_USER_NEW_ONBOARDING, true);
    }

    @Override
    public void setFirstTimeUser(boolean isFirstTime) {
        setBoolean(LOGIN_SESSION, IS_FIRST_TIME_USER, isFirstTime);
    }

    public boolean hasPassword() {
        return getAndTrimOldBoolean(LOGIN_SESSION, HAS_PASSWORD, true);
    }

    @Override
    public boolean isPowerMerchantIdle() {
        return getAndTrimOldBoolean(LOGIN_SESSION, IS_POWER_MERCHANT_IDLE, false);
    }

    @Override
    public boolean getTwitterShouldPost() {
        return getAndTrimOldBoolean(LOGIN_SESSION, TWITTER_SHOULD_POST, false);
    }

    @Override
    public void setTwitterShouldPost(boolean shouldPost) {
        setBoolean(LOGIN_SESSION, TWITTER_SHOULD_POST, shouldPost);
    }

    public void setIsLogin(boolean isLogin) {
        setBoolean(LOGIN_SESSION, IS_LOGIN, isLogin);
    }

    public void setIsGoldMerchant(boolean isGoldMerchant) {
        setBoolean(LOGIN_SESSION, IS_GOLD_MERCHANT, isGoldMerchant);
    }

    public void setTempLoginName(String fullName) {
        setString(LOGIN_SESSION, TEMP_NAME, fullName);
    }

    public void setTempUserId(String userId) {
        setString(LOGIN_SESSION, TEMP_USER_ID, userId);
    }

    public void clearToken() {
        setToken(null, null);
    }

    public void setToken(String accessToken, String tokenType) {
        setString(LOGIN_SESSION, TOKEN_TYPE, tokenType);
        setString(LOGIN_SESSION, ACCESS_TOKEN, accessToken);
    }

    @Override
    public void setFirstTimeUserOnboarding(boolean isFirstTime) {
        setBoolean(LOGIN_SESSION, IS_FIRST_TIME_USER_NEW_ONBOARDING, isFirstTime);
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        setString(LOGIN_SESSION, ACCESS_TOKEN, accessToken);
        setString(LOGIN_SESSION, TOKEN_TYPE, tokenType);
        setString(LOGIN_SESSION, REFRESH_TOKEN, refreshToken);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        setString(LOGIN_SESSION, REFRESH_TOKEN, refreshToken);
    }

    @Override
    public void setIsMSISDNVerified(boolean isMsisdnVerified) {
        setBoolean(LOGIN_SESSION, IS_MSISDN_VERIFIED, isMsisdnVerified);
    }

    @Override
    public void setIsAffiliateStatus(boolean isAffiliate) {
        setBoolean(LOGIN_SESSION, IS_AFFILIATE, isAffiliate);
    }

    @Override
    public void setTempLoginEmail(String email) {
        setString(LOGIN_SESSION, TEMP_EMAIL, email);
    }

    @Override
    public void setHasPassword(boolean hasPassword) {
        setBoolean(LOGIN_SESSION, HAS_PASSWORD, hasPassword);
    }

    @Override
    public void setSaldoWithdrawalWaring(boolean value) {
        setBoolean(LOGIN_SESSION, HAS_SHOWN_SALDO_WARNING, value);
    }

    @Override
    public void setSaldoIntroPageStatus(boolean value) {
        setBoolean(LOGIN_SESSION, HAS_SHOWN_SALDO_INTRO_PAGE, value);
    }

    @Override
    public void setIsPowerMerchantIdle(boolean powerMerchantIdle) {
        setBoolean(LOGIN_SESSION, IS_POWER_MERCHANT_IDLE, powerMerchantIdle);
    }

    @Override
    public void setTwitterAccessTokenAndSecret(@NotNull String accessToken, @NotNull String accessTokenSecret) {
        setString(LOGIN_SESSION, TWITTER_ACCESS_TOKEN, accessToken);
        setString(LOGIN_SESSION, TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret);
    }

    @Nullable
    @Override
    public String getTwitterAccessToken() {

        return getAndTrimOldString(LOGIN_SESSION, TWITTER_ACCESS_TOKEN, null);
    }

    public String getPhoneNumber() {
        return getAndTrimOldString(LOGIN_SESSION, PHONE_NUMBER, "");
    }

    public void setPhoneNumber(String phoneNumber) {
        setString(LOGIN_SESSION, PHONE_NUMBER, phoneNumber);
    }

    public String getEmail() {
        return getAndTrimOldString(LOGIN_SESSION, EMAIL, "");
    }

    public void setEmail(String email) {
        setString(LOGIN_SESSION, EMAIL, email);
    }

    @Override
    public String getShopAvatar() {
        return getAndTrimOldString(LOGIN_SESSION, SHOP_AVATAR, "");
    }

    @Override
    public void setShopAvatar(String shopAvatar) {
        setString(LOGIN_SESSION, SHOP_AVATAR, shopAvatar);
    }

    @Override
    public String getAutofillUserData() {
        return getAndTrimOldString(LOGIN_SESSION, AUTOFILL_USER_DATA, "");
    }

    @Override
    public void setAutofillUserData(String autofillUserData) {
        setString(LOGIN_SESSION, AUTOFILL_USER_DATA, autofillUserData);
    }

    @Override
    public String getLoginMethod() {
        return getAndTrimOldString(LOGIN_SESSION, LOGIN_METHOD, "");
    }

    @Override
    public void setLoginMethod(@NotNull String loginMethod) {
        setString(LOGIN_SESSION, LOGIN_METHOD, loginMethod);
    }

    @Nullable
    @Override
    public String getTwitterAccessTokenSecret() {
        return getAndTrimOldString(LOGIN_SESSION, TWITTER_ACCESS_TOKEN_SECRET, null);
    }

    public String getRefreshTokenIV() {
        return getAndTrimOldString(LOGIN_SESSION, REFRESH_TOKEN_KEY, KEY_IV);
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
}
