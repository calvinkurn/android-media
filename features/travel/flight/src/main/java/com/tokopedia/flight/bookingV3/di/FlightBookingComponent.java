package com.tokopedia.flight.bookingV3.di;

import com.tokopedia.common.travel.di.CommonTravelModule;
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity;
import com.tokopedia.flight.bookingV3.presentation.fragment.FlightBookingFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

import dagger.Component;

/**
 * Created by alvarisi on 11/8/17.
 */

@FlightBookingScope
@Component(modules = {FlightBookingModule.class, CommonTravelModule.class, FlightBookingViewModelModule.class}, dependencies = FlightComponent.class)
public interface FlightBookingComponent {

    void inject(FlightBookingActivity flightBookingActivity);

    void inject(FlightBookingFragment flightBookingFragment);

}
