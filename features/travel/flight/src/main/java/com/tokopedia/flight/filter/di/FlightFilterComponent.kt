package com.tokopedia.flight.filter.di

import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * @author by furqan on 17/02/2020
 */
@FlightFilterScope
@Component(modules = [FlightFilterModule::class, FlightFilterViewModelModule::class], dependencies = [FlightComponent::class])
interface FlightFilterComponent {
}