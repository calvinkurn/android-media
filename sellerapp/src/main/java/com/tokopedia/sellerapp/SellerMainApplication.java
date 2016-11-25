package com.tokopedia.sellerapp;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.InstabugHelper;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends MainApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        InstabugHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        InstabugHelper.setDebug(BuildConfig.DEBUG);
        initializeDatabase();
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
    }
}
