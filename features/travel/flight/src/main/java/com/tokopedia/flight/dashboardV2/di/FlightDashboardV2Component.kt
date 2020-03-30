package com.tokopedia.flight.dashboardV2.di

import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.dashboardV2.presentation.fragment.FlightDashboardV2Fragment
import dagger.Component

/**
 * @author by furqan on 27/03/2020
 */
@FlightDashboardV2Scope
@Component(modules = [FlightDashboardV2Module::class, FlightDashboardV2ViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightDashboardV2Component {
    fun inject(flightDashboardV2Fragment: FlightDashboardV2Fragment)

}