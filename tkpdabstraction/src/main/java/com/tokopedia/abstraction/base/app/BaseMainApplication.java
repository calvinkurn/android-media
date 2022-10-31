package com.tokopedia.abstraction.base.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokochat.tokochat_config_common.util.TokoChatConnection;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication {

    private BaseAppComponent baseAppComponent;
    private TokoChatConnection tokoChatConnection;

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        initTokoChatConnection();
        return baseAppComponent;
    }

    public void setComponent(BaseAppComponent appComponent) {
        this.baseAppComponent = appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SplitCompat.install(this);
    }

    private void initTokoChatConnection() {
        if (tokoChatConnection == null) {
            tokoChatConnection = TokoChatConnection.INSTANCE;
            tokoChatConnection.init(getApplicationContext());
        }
    }

    public TokoChatConnection getTokoChatConnection() {
        return tokoChatConnection;
    }
}