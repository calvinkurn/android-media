package com.tokopedia.flight.booking.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.bookingV2.viewmodel.FlightBookingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-09-06
 */

@Module
@FlightBookingScope
abstract class FlightBookingViewModelModule {
    @FlightBookingScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightBookingViewModel::class)
    abstract fun flightBookingPassengerViewModel(viewModel: FlightBookingViewModel): ViewModel
}