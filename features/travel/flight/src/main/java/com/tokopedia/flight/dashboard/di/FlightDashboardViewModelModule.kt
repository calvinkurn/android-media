package com.tokopedia.flight.dashboard.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.dashboard.view.viewmodel.FlightFareCalendarModel
import com.tokopedia.flight.dashboard.view.viewmodel.FlightHolidayCalendarModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FlightDashboardViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightHolidayCalendarModel::class)
    internal abstract fun flightHolidayCalendarViewModel(customModel: FlightHolidayCalendarModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlightFareCalendarModel::class)
    internal abstract fun flightFareCalendarViewModel(customModel: FlightFareCalendarModel): ViewModel
}