package com.tokopedia.core;

import android.content.Context;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * Created by User on 10/24/2017.
 */

public class CoreNetworkApplication extends BaseMainApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        CoreNetworkApplication.context = getApplicationContext();
    }

    public synchronized static Context getAppContext() {
        return CoreNetworkApplication.context;
    }
}