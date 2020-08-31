package com.tokopedia.flight.search_universal.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.search_universal.presentation.bottomsheet.FlightSearchUniversalBottomSheet
import dagger.Component

/**
 * @author by furqan on 10/03/2020
 */
@FlightSearchUniversalScope
@Component(modules = [FlightSearchUniversalModule::class, FlightSearchUniversalViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightSearchUniversalComponent {
    fun inject(flightSearchUniversalBottomSheet: FlightSearchUniversalBottomSheet)
}