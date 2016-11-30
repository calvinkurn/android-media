package com.tokopedia.tkpd;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.InstabugHelper;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends MainApplication {

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        InstabugHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        InstabugHelper.setDebug(BuildConfig.DEBUG);
        GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        super.onCreate();
    }
}
