package com.tokopedia.flight.dashboardV2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.dashboardV2.presentation.viewmodel.FlightDashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 27/03/2020
 */
@Module
@FlightDashboardV2Scope
abstract class FlightDashboardV2ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightDashboardViewModel::class)
    abstract fun flightDashboardViewModel(flightDashboardViewModel: FlightDashboardViewModel): ViewModel

}