package com.tokopedia.flight.bookingV2.di;

import com.tokopedia.common.travel.di.CommonTravelModule;
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity;
import com.tokopedia.flight.bookingV3.presentation.fragment.FlightBookingFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingNationalityFragment;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;

import org.jetbrains.annotations.NotNull;

import dagger.Component;

/**
 * Created by alvarisi on 11/8/17.
 */

@FlightBookingScope
@Component(modules = {FlightBookingModule.class, CommonTravelModule.class, FlightBookingViewModelModule.class}, dependencies = FlightComponent.class)
public interface FlightBookingComponent {

    void inject(FlightBookingNationalityFragment flightBookingNationalityFragment);

    void inject(FlightBookingReviewFragment flightBookingReviewFragment);

    void inject(@NotNull com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingActivity flightBookingActivity);

    void inject(@NotNull com.tokopedia.flight.bookingV2.presentation.fragment.FlightBookingFragment flightBookingFragment);

    void inject(FlightBookingActivity flightBookingActivity);

    void inject(FlightBookingFragment flightBookingFragment);

}
