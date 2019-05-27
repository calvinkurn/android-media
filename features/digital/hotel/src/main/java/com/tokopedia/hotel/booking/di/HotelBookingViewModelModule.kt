package com.tokopedia.hotel.booking.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 13/05/19
 */
@Module
@HotelBookingScope
abstract class HotelBookingViewModelModule {

    @HotelBookingScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelBookingViewModel::class)
    internal abstract fun hotelDestinationViewModel(viewModel: HotelBookingViewModel): ViewModel
}