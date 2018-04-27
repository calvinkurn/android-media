package com.tokopedia.abstraction.common.data.model.session;

/**
 * Created by nathan on 11/28/17.
 */

public interface UserSession {

    String getAccessToken();

    String getFreshToken();

    String getUserId();

    String getDeviceId();

    boolean isLoggedIn();

    String getShopId();

    boolean hasShop();

    String getName();

    String getProfilePicture();

    boolean isMsisdnVerified();

    boolean isHasPassword();

    String getTemporaryUserId();

    void setUUID(String uuid);

    void setLoginSession(boolean isLogin, String userId, String fullName, String shopId, boolean
            msisdnVerified, String shopName);

    String getTempEmail();

    void setEmail(String email);

    void setGoldMerchant(int shopIsGold);

    String getTempPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    void setTempLoginName(String fullName);

    void setTempLoginSession(String userId);
}
