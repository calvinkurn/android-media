package com.tokopedia.flight.dashboard.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.dashboard.view.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.flight.dashboard.view.viewmodel.FlightHolidayCalendarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@FlightDashboardScope
abstract class FlightDashboardViewModelModule {

    @FlightDashboardScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightHolidayCalendarViewModel::class)
    internal abstract fun flightHolidayCalendarViewModel(customViewModel: FlightHolidayCalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlightFareCalendarViewModel::class)
    internal abstract fun flightFareCalendarViewModel(customViewModel: FlightFareCalendarViewModel): ViewModel
}