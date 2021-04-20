package com.tokopedia.flight.homepage.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.homepage.presentation.fragment.FlightHomepageFragment
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarRoundTripWidget
import dagger.Component

/**
 * @author by furqan on 27/03/2020
 */
@FlightHomepageScope
@Component(modules = [FlightHomepageModule::class, FlightHomepageViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightHomepageComponent {
    fun inject(flightHomepageFragment: FlightHomepageFragment)
    fun inject(flightCalendarOneWayWidget: FlightCalendarOneWayWidget)
    fun inject(flightCalendarRoundTripWidget: FlightCalendarRoundTripWidget)
}