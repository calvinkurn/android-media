package com.tokopedia.tkpd;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by zulfikarrahman on 12/4/17.
 */

public class UserSessionImpl implements UserSession {

    private final Context context;

    public UserSessionImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getAccessToken() {
        return SessionHandler.getAccessToken();
    }

    @Override
    public String getFreshToken() {
        return SessionHandler.getRefreshToken(context);
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(context);
    }
}
