package com.tokopedia.flightmockapp;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdFlightGeneratedDatabaseHolder;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.utils.GlobalConfig;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.common.di.component.DaggerFlightComponent;
import com.tokopedia.flight.common.di.component.FlightComponent;

/**
 * Created by User on 10/24/2017.
 */

public class FlightMainApplication extends BaseMainApplication implements FlightModuleRouter{

    private FlightComponent flightComponent;

    @Override
    public void onCreate() {
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        super.onCreate();
        initDBFlow();
    }

    private void initDBFlow() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdFlightGeneratedDatabaseHolder.class)
                .build());
    }

    @Override
    public FlightComponent getFlightComponent() {
        if (flightComponent == null) {
            flightComponent = DaggerFlightComponent.builder().baseAppComponent(getBaseAppComponent()).build();
        }
        return flightComponent;
    }
}
