package com.tokopedia.abstraction.base.app;

import android.support.annotation.CallSuper;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.tokopedia.abstraction.di.component.BaseAppComponent;
import com.tokopedia.abstraction.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.di.module.AppModule;
import com.tokopedia.abstraction.utils.GlobalConfig;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication{

    private BaseAppComponent baseAppComponent;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
    }

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        return baseAppComponent;
    }

    private void initStetho() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Stetho.initializeWithDefaults(getApplicationContext());
        }
    }
}
