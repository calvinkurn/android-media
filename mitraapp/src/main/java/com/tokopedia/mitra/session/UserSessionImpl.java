package com.tokopedia.mitra.session;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.util.SessionHandler;

public class UserSessionImpl implements UserSession {

    private SessionHandler sessionHandler;
    com.tokopedia.user.session.UserSession userSession;

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
        this.userSession = new com.tokopedia.user.session.UserSession(context);
    }

    @Override
    public String getAccessToken() {
        return userSession.getAccessToken();
    }

    @Override
    public String getFreshToken() {
        return userSession.getFreshToken();
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public String getDeviceId() {
        return userSession.getDeviceId();
    }

    @Override
    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    @Override
    public String getShopId() {
        return userSession.getShopId();
    }

    @Override
    public boolean hasShop() {
        return userSession.hasShop();
    }

    @Override
    public String getName() {
        return userSession.getName();
    }

    @Override
    public String getProfilePicture() {
        return userSession.getProfilePicture();
    }

    @Override
    public boolean isMsisdnVerified() {
        return userSession.isMsisdnVerified();
    }

    @Override
    public boolean isHasPassword() {
        return sessionHandler.isHasPassword();
    }

    @Override
    public String getPhoneNumber() {
        return userSession.getPhoneNumber();
    }
}