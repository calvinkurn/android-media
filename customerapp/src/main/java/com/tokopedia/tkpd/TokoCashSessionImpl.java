package com.tokopedia.tkpd;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tokocash.network.TokoCashSession;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class TokoCashSessionImpl implements TokoCashSession {

    private SessionHandler sessionHandler;

    public TokoCashSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
    }

    @Override
    public void setTokenWallet(String token) {
        sessionHandler.setTokenTokoCash(token);
    }

    @Override
    public String getTokenWallet() {
        return sessionHandler.getAccessTokenTokoCash();
    }
}
