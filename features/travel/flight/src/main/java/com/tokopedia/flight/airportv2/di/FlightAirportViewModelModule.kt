package com.tokopedia.flight.airportv2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.airportv2.presentation.viewmodel.FlightAirportPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 19/05/2020
 */
@FlightAirportScope
@Module
abstract class FlightAirportViewModelModule {
    @FlightAirportScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightAirportPickerViewModel::class)
    abstract fun flightAirportPickerViewModel(viewModel: FlightAirportPickerViewModel): ViewModel
}