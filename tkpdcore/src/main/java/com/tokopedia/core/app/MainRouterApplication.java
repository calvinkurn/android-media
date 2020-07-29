package com.tokopedia.core.app;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.CoreNetworkApplication;

/**
 * this code is bridging for old code and latest codes.
 */
public abstract class MainRouterApplication extends CoreNetworkApplication implements TkpdCoreRouter {
    GCMHandler gcmHandler;
    SessionHandler sessionHandler;

    @Override
    public SessionHandler legacySessionHandler() {
        if(sessionHandler == null) {
            final com.tokopedia.core.util.SessionHandler mSessionHandler
                    = new com.tokopedia.core.util.SessionHandler(this);
            return sessionHandler = new SessionHandler(this) {

                @Override
                public String getLoginID() {
                    return mSessionHandler.getLoginID();
                }

                @Override
                public String getRefreshToken() {
                    return mSessionHandler.getAuthRefreshToken();
                }
            };
        }else{
            return sessionHandler;
        }
    }

    @Override
    public GCMHandler legacyGCMHandler() {
        if(gcmHandler==null){
            return gcmHandler = new GCMHandler(this);
        }else {
            return gcmHandler;
        }
    }
}
