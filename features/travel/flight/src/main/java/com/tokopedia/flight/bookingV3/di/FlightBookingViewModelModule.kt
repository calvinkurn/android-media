package com.tokopedia.flight.bookingV3.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.bookingV3.viewmodel.FlightBookingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-10-25
 */

@Module
abstract class FlightBookingViewModelModule {

    @FlightBookingScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightBookingScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightBookingViewModel::class)
    abstract fun flightBookingViewModel(viewModel: FlightBookingViewModel): ViewModel
}