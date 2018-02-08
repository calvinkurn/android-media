package com.tokopedia.posapp;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.facebook.soloader.SoLoader;

/**
 * Created by okasurya on 7/30/17.
 */

public class PosApplication extends PosRouterApplication {
    @Override
    public void onCreate() {
        setGlobalConfiguration();
        generatePosAppBaseUrl();
        generatePosAppConstant();
        initializeDatabase();
        initReact();
        super.onCreate();
    }

    private void generatePosAppConstant() {
        PosConstants.KEY_PAYMENT = PosAppConstants.KEY_PAYMENT;
    }

    private void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    private void setGlobalConfiguration() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.POS_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_POS_APP;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
    }

    private void generatePosAppBaseUrl() {
        TkpdBaseURL.BASE_DOMAIN = PosAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = PosAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.TOME_DOMAIN = PosAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.CLOVER_DOMAIN = PosAppBaseUrl.BASE_CLOVER_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = PosAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = PosAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = PosAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = PosAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = PosAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = PosAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = PosAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = PosAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = PosAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = PosAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = PosAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = PosAppBaseUrl.BASE_MOBILE_DOMAIN;
        TkpdBaseURL.BASE_CONTACT_US = PosAppBaseUrl.BASE_WEB_DOMAIN + "contact-us";
        TkpdBaseURL.RIDE_DOMAIN = PosAppBaseUrl.BASE_RIDE_DOMAIN;
        TkpdBaseURL.TOKO_CASH_DOMAIN = PosAppBaseUrl.BASE_TOKO_CASH_DOMAIN;
        TkpdBaseURL.BASE_ACTION = PosAppBaseUrl.BASE_DOMAIN + "v4/action/";
        TkpdBaseURL.DIGITAL_API_DOMAIN = PosAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN = PosAppBaseUrl.BASE_DIGITAL_WEBSITE_DOMAIN;
        TkpdBaseURL.GRAPHQL_DOMAIN = PosAppBaseUrl.GRAPHQL_DOMAIN;
        TkpdBaseURL.SCROOGE_DOMAIN = PosAppBaseUrl.SCROOGE_DOMAIN;
        TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN = PosAppBaseUrl.SCROOGE_CREDIT_CARD_DOMAIN;
        TkpdBaseURL.PAYMENT_DOMAIN = PosAppBaseUrl.PAYMENT_DOMAIN;
        TkpdBaseURL.POS_DOMAIN = PosAppBaseUrl.POS_DOMAIN;
    }

    private void initReact() {
        SoLoader.init(this, false);
    }
}
