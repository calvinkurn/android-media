package com.tokopedia.core.app;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.CoreNetworkApplication;

/**
 * this code is bridging for old code and latest codes.
 */
public abstract class MainRouterApplication extends CoreNetworkApplication implements TkpdCoreRouter {
    GCMHandler gcmHandler;

    @Override
    public GCMHandler legacyGCMHandler() {
        if(gcmHandler==null){
            return gcmHandler = new GCMHandler(this);
        }else {
            return gcmHandler;
        }
    }
}
