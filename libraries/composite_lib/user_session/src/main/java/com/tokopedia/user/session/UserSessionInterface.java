package com.tokopedia.user.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author by nisie on 9/25/18.
 */
public interface UserSessionInterface {

    String LOGIN_METHOD_EMAIL = "email";
    String LOGIN_METHOD_GOOGLE = "google";
    String LOGIN_METHOD_FACEBOOK = "facebook";
    String LOGIN_METHOD_PHONE = "phone";
    String LOGIN_METHOD_EMAIL_SMART_LOCK = "email_smartlock";


    String getAccessToken();

    String getTokenType();

    String getFreshToken();

    String getUserId();

    boolean isLoggedIn();

    String getShopId();

    String getName();

    String getProfilePicture();

    String getTemporaryUserId();

    String getDeviceId();

    String getTempEmail();

    String getTempPhoneNumber();

    boolean isMsisdnVerified();

    boolean hasShownSaldoWithdrawalWarning();

    String getPhoneNumber();

    String getEmail();

    String getRefreshTokenIV();

    boolean isFirstTimeUser();

    boolean isGoldMerchant();

    String getShopName();

    boolean hasShop();

    boolean hasPassword();

    String getGCToken();

    String getShopAvatar();

    boolean isPowerMerchantIdle();

    String getAutofillUserData();

    @Nullable
    String getTwitterAccessToken();

    @Nullable
    String getTwitterAccessTokenSecret();

    boolean getTwitterShouldPost();

    /**
     * @return method name from this class
     */
    String getLoginMethod();

    /**
     * SETTER METHOD
     */

    void setUUID(String uuid);

    void setIsLogin(boolean isLogin);

    void setUserId(String userId);

    void setName(String fullName);

    void setEmail(String email);

    void setPhoneNumber(String phoneNumber);

    void setShopId(String shopId);

    void setShopName(String shopName);

    void setIsGoldMerchant(boolean isGoldMerchant);

    void setTempLoginName(String fullName);

    void setTempUserId(String userId);

    void setIsAffiliateStatus(boolean isAffiliate);

    void setTempPhoneNumber(String userPhone);

    void setTempLoginEmail(String email);

    void setToken(String accessToken, String tokenType);

    void clearToken();

    void logoutSession();

    void setFirstTimeUserOnboarding(boolean isFirstTime);

    void setFirstTimeUser(boolean isFirstTime);

    void setToken(String accessToken, String tokenType, String refreshToken);

    void setRefreshToken(String refreshToken);

    void setLoginSession(boolean isLogin, String userId, String fullName, String shopId,
                         boolean isMsisdnVerified, String shopName, String email, boolean
                                 shopIsGold, String phoneNumber);

    void setIsMSISDNVerified(boolean isMsisdnVerified);

    void setHasPassword(boolean hasPassword);

    void setProfilePicture(String profilePicture);

    void setSaldoWithdrawalWaring(boolean value);

    void setSaldoIntroPageStatus(boolean value);

    void setGCToken(String gcToken);

    void setShopAvatar(String shopAvatar);

    void setIsPowerMerchantIdle(boolean powerMerchantIdle);

    void setTwitterAccessTokenAndSecret(@NotNull String accessToken, @NotNull String accessTokenSecret);

    void setTwitterShouldPost(boolean shouldPost);

    void setAutofillUserData(String autofillUserData);

    void setLoginMethod(@NotNull String loginMethod);

    void setIsShopOfficialStore(boolean isShopOfficialStore);

    boolean isShopOfficialStore();

    void setDeviceId(String deviceId);

    void setFcmTimestamp();

    long getFcmTimestamp();

    String getGTMLoginID();

    String getAndroidId();

    String getAdsId();

    boolean isAffiliate();

    boolean hasShownSaldoIntroScreen();

    boolean isShopOwner();

    void setIsShopOwner(boolean isShopOwner);

    boolean isShopAdmin();

    void setIsShopAdmin(boolean isShopAdmin);

    boolean isLocationAdmin();

    void setIsLocationAdmin(boolean isLocationAdmin);

    boolean isMultiLocationShop();

    void setIsMultiLocationShop(boolean isMultiLocationShop);
}
