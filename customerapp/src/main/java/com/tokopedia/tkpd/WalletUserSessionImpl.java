package com.tokopedia.tkpd;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tokocash.WalletUserSession;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class WalletUserSessionImpl implements WalletUserSession {

    private SessionHandler sessionHandler;

    public WalletUserSessionImpl(Context context) {
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

    @Override
    public String getPhoneNumber() {
        return sessionHandler.getPhoneNumber();
    }

    @Override
    public boolean isMsisdnVerified() {
        return sessionHandler.isMsisdnVerified();
    }

    @Override
    public void setMsisdnVerified(boolean verified) {
        sessionHandler.setIsMSISDNVerified(verified);
    }
}
