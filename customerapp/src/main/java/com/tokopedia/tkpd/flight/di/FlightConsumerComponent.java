package com.tokopedia.tkpd.flight.di;

import com.tokopedia.di.SessionComponent;
import com.tokopedia.tkpd.flight.FlightGetProfileInfoData;

import dagger.Component;

/**
 * @author by alvarisi on 1/24/18.
 */
@FlightConsumerScope
@Component(dependencies = SessionComponent.class)
public interface FlightConsumerComponent {
    void inject(FlightGetProfileInfoData flightGetProfileInfoData);
}
