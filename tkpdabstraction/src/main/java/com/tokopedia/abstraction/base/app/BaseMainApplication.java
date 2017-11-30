package com.tokopedia.abstraction.base.app;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.facebook.stetho.Stetho;
import com.tokopedia.abstraction.di.component.BaseAppComponent;
import com.tokopedia.abstraction.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.di.module.AppModule;
import com.tokopedia.abstraction.utils.GlobalConfig;

import android.support.multidex.MultiDexApplication;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication{
    protected static Context context;

    private BaseAppComponent baseAppComponent;

    public synchronized static Context getAppContext() {
        return context;
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initStetho();
    }

    public BaseAppComponent getBaseApplicationComponent() {
        return getBaseAppComponent();
    }

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        return baseAppComponent;
    }

    public void initStetho() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Stetho.initializeWithDefaults(context);
        }
    }
}
