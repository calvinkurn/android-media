package com.tokopedia.flight.airport.di

import com.tokopedia.flight.airport.presentation.bottomsheet.FlightAirportPickerBottomSheet
import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * Created by zulfikarrahman on 10/24/17.
 */
@FlightAirportScope
@Component(modules = [FlightAirportModule::class, FlightAirportViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightAirportComponent {

    fun inject(flightAirportPickerFragment: FlightAirportPickerBottomSheet)

}
