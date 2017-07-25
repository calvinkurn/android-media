package com.tokopedia.tkpd;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.react.ReactSingleton;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.tkpd.deeplink.DeepLinkReceiver;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication {

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        generateConsumerAppBaseUrl();
        initializeDatabase();
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);

        ReactSingleton.init(this);
    }

    private void generateConsumerAppBaseUrl() {
        TkpdBaseURL.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.TOME_DOMAIN = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.CLOVER_DOMAIN = ConsumerAppBaseUrl.BASE_CLOVER_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = ConsumerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = ConsumerAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = ConsumerAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.RECHARGE_API_DOMAIN = ConsumerAppBaseUrl.BASE_RECHARGE_API_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = ConsumerAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = ConsumerAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = ConsumerAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = ConsumerAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = ConsumerAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = ConsumerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        TkpdBaseURL.BASE_CONTACT_US = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + "contact-us";
        TkpdBaseURL.RIDE_DOMAIN = ConsumerAppBaseUrl.BASE_RIDE_DOMAIN;
        TkpdBaseURL.TOKO_CASH_DOMAIN = ConsumerAppBaseUrl.BASE_TOKO_CASH_DOMAIN;
        TkpdBaseURL.BASE_ACTION = ConsumerAppBaseUrl.BASE_DOMAIN + "v4/action/";
        TkpdBaseURL.DIGITAL_API_DOMAIN = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        TkpdBaseURL.GRAPHQL_DOMAIN = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
    }

}
