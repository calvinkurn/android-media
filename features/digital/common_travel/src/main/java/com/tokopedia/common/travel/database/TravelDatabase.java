package com.tokopedia.common.travel.database;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdTravelGeneratedDatabaseHolder;

/**
 * Created by nabillasabbaha on 15/08/18.
 */
public class TravelDatabase {

    public static void init(Context applicationContext) {
        try {
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(TkpdTravelGeneratedDatabaseHolder.class);
    }
}
