package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
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
        generateConsumerAppBaseUrl();
        super.onCreate();
    }

    private void generateConsumerAppBaseUrl() {
        TkpdBaseURL.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
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
    }

}
