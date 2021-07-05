package com.tokopedia.flight.passenger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.passenger.view.viewmodel.FlightPassengerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-09-06
 */

@Module
abstract class FlightPassengerViewModelModule {
    @FlightPassengerScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightPassengerViewModel::class)
    abstract fun flightBookingPassengerViewModel(viewModel: FlightPassengerViewModel): ViewModel
}