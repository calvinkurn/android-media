package com.tokopedia.analytics;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdAnalyticsGeneratedDatabaseHolder;

/**
 * @author okasurya on 5/16/18.
 */
public class Analytics {
    public static void initDB(Context applicationContext) {
        // should only initDatabase once per application, if the config is null, initialize it.
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(TkpdAnalyticsGeneratedDatabaseHolder.class);
    }
}
