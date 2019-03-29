package com.tokopedia.flight;

import android.app.Application;

import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.domain.FlightRepository;

public class FlightComponentInstance {
    public static FlightComponent getFlightComponent(Application application) {
        return new FlightComponent() {
            @Override
            public FlightRepository flightRepository() {
                return null;
            }
        };
    }
}
