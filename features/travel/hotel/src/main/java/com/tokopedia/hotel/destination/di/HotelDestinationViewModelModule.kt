package com.tokopedia.hotel.destination.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HotelDestinationViewModelModule{

    @HotelDestinationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelDestinationViewModel::class)
    internal abstract fun hotelDestinationViewModel(viewModel: HotelDestinationViewModel): ViewModel
}