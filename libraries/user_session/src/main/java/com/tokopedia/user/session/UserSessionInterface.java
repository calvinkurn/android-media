package com.tokopedia.user.session;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 9/25/18.
 */
public interface UserSessionInterface {

    String LOGIN_METHOD_EMAIL = "email";
    String LOGIN_METHOD_GOOGLE = "google";
    String LOGIN_METHOD_FACEBOOK = "facebook";
    String LOGIN_METHOD_PHONE = "phone";

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

    String getAutofillUserData();

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

    void setAutofillUserData(String autofillUserData);

    void setLoginMethod(@NotNull String loginMethod);
}
