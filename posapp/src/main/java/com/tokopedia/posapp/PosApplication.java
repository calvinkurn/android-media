package com.tokopedia.posapp;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.facebook.soloader.SoLoader;
import com.tokopedia.posapp.common.PosUrl;

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
        // TODO: 3/26/18 remove tkpdbaseurl
        TkpdBaseURL.POS_DOMAIN = PosAppBaseUrl.POS_DOMAIN;
        PosUrl.POS_DOMAIN = PosAppBaseUrl.POS_DOMAIN;
    }

    private void initReact() {
        SoLoader.init(this, false);
    }
}
