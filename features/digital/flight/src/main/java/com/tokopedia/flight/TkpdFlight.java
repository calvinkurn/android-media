package com.tokopedia.flight;

import android.content.Context;
import android.content.Intent;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdFlightGeneratedDatabaseHolder;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;

/**
 * Created by nakama on 11/12/17.
 */

public class TkpdFlight{

    public static void initDatabase(Context applicationContext){
        // should only initDatabase once per application, if the config is null, initialize it.
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(TkpdFlightGeneratedDatabaseHolder.class);
    }

    public static void goToFlightActivity(Context context){
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        context.startActivity(intent);
    }

}
