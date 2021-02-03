package com.tokopedia.hotel.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HotelSearchPropertyVMModule {

    @HotelSearchPropertyScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelSearchResultViewModel::class)
    internal abstract fun hotelSearchResultViewModel(viewModel: HotelSearchResultViewModel): ViewModel
}