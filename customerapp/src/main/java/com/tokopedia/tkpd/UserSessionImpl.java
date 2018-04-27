package com.tokopedia.tkpd;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by zulfikarrahman on 12/4/17.
 */

public class UserSessionImpl implements UserSession {

    private SessionHandler sessionHandler;
    private FCMCacheManager fcmCacheManager;
    private Context context;

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
        this.fcmCacheManager = new FCMCacheManager(context);
        this.context = context;
    }

    @Override
    public String getAccessToken() {
        return sessionHandler.getAuthAccessToken();
    }

    @Override
    public String getFreshToken() {
        return sessionHandler.getAuthRefreshToken();
    }

    @Override
    public String getUserId() {
        return sessionHandler.getLoginID();
    }

    @Override
    public String getDeviceId() {
        return fcmCacheManager.getRegistrationId();
    }

    @Override
    public boolean isLoggedIn() {
        return sessionHandler.isV4Login();
    }

    @Override
    public String getShopId() {
        return sessionHandler.getShopID();
    }

    @Override
    public boolean hasShop() {
        return sessionHandler.isUserHasShop();
    }

    @Override
    public String getName() {
        return sessionHandler.getLoginName();
    }

    @Override
    public String getProfilePicture() {
        return sessionHandler.getProfilePicture();
    }

    @Override
    public boolean isMsisdnVerified() {
        return sessionHandler.isMsisdnAlreadyVerified();
    }

    @Override
    public boolean isHasPassword() {
        return sessionHandler.isHasPassword();
    }

    @Override
    public String getTemporaryUserId() {
        return sessionHandler.getTempLoginSession();
    }

    @Override
    public void setUUID(String uuid) {
        sessionHandler.setUUID(uuid);
    }

    @Override
    public void setLoginSession(boolean isLogin, String userId, String fullName, String shopId,
                                boolean msisdnVerified, String shopName) {
        sessionHandler.setLoginSession(isLogin, userId, fullName, shopId, msisdnVerified, shopName);
    }

    @Override
    public String getTempEmail() {
        return sessionHandler.getTempEmail();
    }

    @Override
    public void setEmail(String email) {
        sessionHandler.setEmail(email);
    }

    @Override
    public void setGoldMerchant(int shopIsGold) {
        sessionHandler.setGoldMerchant(shopIsGold);

    }

    @Override
    public String getTempPhoneNumber() {
        return sessionHandler.getTempPhoneNumber(context);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        sessionHandler.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setTempLoginName(String fullName) {
        sessionHandler.setTempLoginName(fullName);

    }

    @Override
    public void setTempLoginSession(String userId) {
        sessionHandler.setTempLoginSession(userId);
    }
}
