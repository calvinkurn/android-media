package com.tokopedia.hotel.booking.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
abstract class HotelBookingViewModelModule {

    @HotelBookingScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelBookingViewModel::class)
    abstract fun hotelDestinationViewModel(viewModel: HotelBookingViewModel): ViewModel
}