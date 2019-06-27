package com.tokopedia.flight.booking.di;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity;
import com.tokopedia.flight.booking.view.fragment.FLightBookingPhoneCodeFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;

import org.jetbrains.annotations.NotNull;

import dagger.Component;

/**
 * Created by alvarisi on 11/8/17.
 */
@FlightBookingScope
@Component(modules = FlightBookingModule.class, dependencies = FlightComponent.class)
public interface FlightBookingComponent {
    FlightModuleRouter flightModuleRouter();

    void inject(FlightBookingFragment flightBookingFragment);

    void inject(FLightBookingPhoneCodeFragment fLightBookingPhoneCodeFragment);

    void inject(FlightBookingNationalityFragment flightBookingNationalityFragment);

    void inject(FlightBookingReviewFragment flightBookingReviewFragment);

    void inject(FlightBookingPassengerFragment flightBookingPassengerFragment);

    void inject(FlightBookingPassengerActivity flightBookingPassengerActivity);

    void inject(FlightBookingActivity flightBookingActivity);

    void inject(@NotNull com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingActivity flightBookingActivity);

    void inject(@NotNull com.tokopedia.flight.bookingV2.presentation.fragment.FlightBookingFragment flightBookingFragment);
}
