package com.tokopedia.flight.dashboard.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.dashboard.view.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.flight.dashboard.view.viewmodel.FlightHolidayCalendarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FlightDashboardViewModelModule {

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