package com.tokopedia.core.network;

import android.content.Context;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.TkpdCoreRouter;

/**
 * Created by User on 10/24/2017.
 */

@Deprecated
public abstract class CoreNetworkApplication extends BaseMainApplication implements CoreNetworkRouter {

    protected static Context context;
    public static CoreNetworkApplication instanceCoreNetworkApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        instanceCoreNetworkApplication = this;
        CoreNetworkApplication.context = getApplicationContext();
    }

    public synchronized static Context getAppContext() {
        return CoreNetworkApplication.context;
    }

    public static synchronized CoreNetworkRouter getCoreNetworkRouter(){
        return instanceCoreNetworkApplication;
    }
}