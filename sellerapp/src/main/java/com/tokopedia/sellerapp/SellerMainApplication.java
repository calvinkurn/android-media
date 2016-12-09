package com.tokopedia.sellerapp;

import android.support.v7.app.AppCompatActivity;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreListener;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends MainApplication implements TkpdCoreListener {

    public static final int SELLER_APPLICATION = 2;

    @Override
    public int getApplicationType() {
        return SELLER_APPLICATION;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        com.tokopedia.core.util.GlobalConfig.APPLICATION_TYPE = SELLER_APPLICATION;
        initializeDatabase();
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
    }

    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariableSeller(activity);
    }
}
