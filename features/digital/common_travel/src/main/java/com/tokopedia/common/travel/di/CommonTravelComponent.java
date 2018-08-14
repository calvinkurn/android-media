package com.tokopedia.common.travel.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.common.travel.presentation.TravelPassengerBookingFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@CommonTravelScope
@Component(modules = CommonTravelModule.class, dependencies = BaseAppComponent.class)
public interface CommonTravelComponent {

    void inject(TravelPassengerBookingFragment travelPassengerBookingFragment);
}
