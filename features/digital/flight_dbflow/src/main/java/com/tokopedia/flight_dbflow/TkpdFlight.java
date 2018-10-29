package com.tokopedia.flight_dbflow;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdFlightGeneratedDatabaseHolder;

/**
 * Created by Rizky on 25/10/18.
 */
public class TkpdFlight {

    public static void initDatabase(Context applicationContext) {
        // should only initDatabase once per application, if the config is null, initialize it.
        try {
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }

        FlowManager.initModule(TkpdFlightGeneratedDatabaseHolder.class);
    }

}
