package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tokopedia.core.network.constants.TkpdBaseURL;
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
        generateSellerAppBaseUrl();
        super.onCreate();
        //inject components
        setComponent();
        component.inject(this);
    }

    private void generateSellerAppBaseUrl() {
        TkpdBaseURL.BASE_DOMAIN = SellerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = SellerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.CLOVER_DOMAIN = SellerAppBaseUrl.BASE_CLOVER_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = SellerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = SellerAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = SellerAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.RECHARGE_API_DOMAIN = SellerAppBaseUrl.BASE_RECHARGE_API_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = SellerAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = SellerAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = SellerAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = SellerAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = SellerAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = SellerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.TOKOPEDIA_CART_DOMAIN  = SellerAppBaseUrl.TOKOPEDIA_CART_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = SellerAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = SellerAppBaseUrl.BASE_MOBILE_DOMAIN;
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
