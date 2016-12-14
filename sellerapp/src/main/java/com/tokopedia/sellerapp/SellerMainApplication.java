package com.tokopedia.sellerapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreListener;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.util.GlobalConfig;
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

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariableSeller(activity);
    }
}
