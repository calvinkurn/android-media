package com.tokopedia.sellerapp;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.HockeyAppHelper;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends MainApplication {

    public static final int SELLER_APPLICATION = 2;

    @Override
    protected int getApplicationType() {
        return SELLER_APPLICATION;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
    }
}
