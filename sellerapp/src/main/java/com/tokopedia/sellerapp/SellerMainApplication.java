package com.tokopedia.sellerapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tokopedia.core.util.HockeyAppHelper;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication {

    public static final int SELLER_APPLICATION = 2;

    @Override
    public int getApplicationType() {
        return SELLER_APPLICATION;
    }

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        com.tokopedia.core.util.GlobalConfig.APPLICATION_TYPE = SELLER_APPLICATION;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
}
