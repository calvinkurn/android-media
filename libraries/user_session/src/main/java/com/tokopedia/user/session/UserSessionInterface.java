package com.tokopedia.user.session;

/**
 * @author by nisie on 9/25/18.
 */
public interface UserSessionInterface {

    String getAccessToken();

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

    String getPhoneNumber();

    String getEmail();

    String getRefreshTokenIV();

    boolean isFirstTimeUser();


    /**
     * SETTER METHOD
     */

    void setUUID(String uuid);

    void setIsLogin(boolean isLogin);

    void setUserId(String userId);

    void setName(String fullName);

    void setEmail(String email);

    void setIsMsisdnVerified(boolean isMsisdnVerified);

    void setPhoneNumber(String phoneNumber);

    void setShopId(String shopId);

    void setShopName(String shopName);

    void setIsGoldMerchant(boolean isGoldMerchant);

    void setTempLoginName(String fullName);

    void setTempUserId(String userId);

    void setToken(String accessToken, String tokenType);

    void clearToken();

    void logoutSession();

    void setFirstTimeUserOnboarding(boolean isFirstTime);

    void setFirstTimeUser(boolean isFirstTime);
}
