package com.tokopedia.abstraction.base.app;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.tokopedia.abstraction.utils.GlobalConfig;

public abstract class BaseMainApplication extends android.support.multidex.MultiDexApplication {

    private static Context context;

    public synchronized static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        initStetho();

        initDbFlow();
    }

    protected void initDbFlow(){

    }

    public void initStetho() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Stetho.initializeWithDefaults(context);
        }
    }

}