package com.tokopedia.tkpd;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by zulfikarrahman on 12/4/17.
 */

public class UserSessionImpl implements UserSession {

    private SessionHandler sessionHandler;

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
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
    public boolean isLoggedIn() {
        return sessionHandler.isV4Login();
    }
}
