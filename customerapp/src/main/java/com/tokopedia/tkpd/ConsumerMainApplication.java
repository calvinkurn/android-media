package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.tkpd.deeplink.DeepLinkReceiver;

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

        IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
    }
}
