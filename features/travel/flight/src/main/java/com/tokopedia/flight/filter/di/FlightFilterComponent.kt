package com.tokopedia.flight.filter.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.filter.presentation.bottomsheets.FlightFilterBottomSheet
import dagger.Component

/**
 * @author by furqan on 17/02/2020
 */
@FlightFilterScope
@Component(modules = [FlightFilterViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightFilterComponent {
    fun inject(flightFilterBottomSheet: FlightFilterBottomSheet)
}