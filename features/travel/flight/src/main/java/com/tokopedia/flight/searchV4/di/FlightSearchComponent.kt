package com.tokopedia.flight.searchV4.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.searchV4.presentation.fragment.FlightSearchFragment
import dagger.Component

/**
 * @author by furqan on 06/04/2020
 */
@FlightSearchScope
@Component(modules = [FlightSearchModule::class, FlightSearchViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightSearchComponent {
    fun inject(flightSearchFragment: FlightSearchFragment)
}