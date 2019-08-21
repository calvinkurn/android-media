package com.tokopedia.abstraction.base.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication {

    private BaseAppComponent baseAppComponent;

    public BaseAppComponent reinitBaseAppComponent(AppModule appModule){
        return baseAppComponent = DaggerBaseAppComponent.builder()
                .appModule(appModule).build();
    }

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        return baseAppComponent;
    }

    public boolean checkAppSignature(){return false;}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SplitCompat.install(this);
    }
}