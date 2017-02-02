package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tokopedia.core.util.GlobalConfig;

import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.sellerapp.daggerModules.AppModule;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication {

    private BaseComponent component;

    public static final int SELLER_APPLICATION = 2;

    @Override
    public int getApplicationType() {
        return SELLER_APPLICATION;
    }

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        initializeDatabase();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate();
        //inject components
        setComponent();
        component.inject(this);
    }

    public void setComponent() {
        component = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public void setComponent(BaseComponent component) {
        this.component = component;
    }

    public BaseComponent getComponent() {
        return component;
    }

    public static SellerMainApplication get(Context context){
        return (SellerMainApplication) context.getApplicationContext();
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
    }
}
