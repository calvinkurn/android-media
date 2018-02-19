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

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
        this.fcmCacheManager = new FCMCacheManager(context);
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
}
