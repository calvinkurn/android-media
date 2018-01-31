package com.tokopedia.sellerapp;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by zulfikarrahman on 12/4/17.
 */

public class UserSessionImpl implements UserSession {

    private SessionHandler sessionHandler;
    private Context context;

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
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
        return GCMHandler.getRegistrationId(context);
    }

    @Override
    public boolean isLoggedIn() {
        return sessionHandler.isV4Login();
    }

    @Override
    public String getShopID() {
        return sessionHandler.getShopID();
    }
}
