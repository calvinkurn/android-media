package com.tokopedia.flight.booking.di

import com.tokopedia.flight.booking.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.booking.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * Created by alvarisi on 11/8/17.
 */
@FlightBookingScope
@Component(
        modules = [FlightBookingModule::class,
            FlightBookingViewModelModule::class],
        dependencies = [FlightComponent::class]
)
interface FlightBookingComponent {
    fun inject(flightBookingActivity: FlightBookingActivity?)
    fun inject(flightBookingFragment: FlightBookingFragment?)
}