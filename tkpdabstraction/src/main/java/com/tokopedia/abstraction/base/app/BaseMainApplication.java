package com.tokopedia.abstraction.base.app;

import android.support.multidex.MultiDexApplication;

import android.support.v7.app.AppCompatDelegate;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BaseAppComponent baseAppComponent;

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        return baseAppComponent;
    }
}