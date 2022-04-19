package com.tokopedia.flight.homepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightHolidayCalendarViewModel
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 27/03/2020
 */
@Module
abstract class FlightHomepageViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightHomepageViewModel::class)
    abstract fun flightHomepageViewModel(flightHomepageViewModel: FlightHomepageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlightHolidayCalendarViewModel::class)
    internal abstract fun flightHolidayCalendarViewModel(customViewModel: FlightHolidayCalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlightFareCalendarViewModel::class)
    internal abstract fun flightFareCalendarViewModel(customViewModel: FlightFareCalendarViewModel): ViewModel

}