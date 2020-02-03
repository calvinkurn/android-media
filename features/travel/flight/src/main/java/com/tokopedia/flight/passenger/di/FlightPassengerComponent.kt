package com.tokopedia.flight.passenger.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.passenger.view.fragment.FlightBookingPassengerFragment
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by furqan on 12/03/18.
 */

@FlightPassengerScope
@Component(modules = [FlightPassengerModule::class, FlightPassengerViewModelModule::class], dependencies = [FlightComponent::class])
interface FlightPassengerComponent {

    fun dispatcher(): CoroutineDispatcher

    fun inject(flightBookingPassengerFragment: FlightBookingPassengerFragment)

}
