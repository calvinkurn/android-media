package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication {

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        super.onCreate();
    }
}
