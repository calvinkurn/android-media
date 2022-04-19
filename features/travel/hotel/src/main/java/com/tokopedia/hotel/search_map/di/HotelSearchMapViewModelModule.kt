package com.tokopedia.hotel.search_map.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 01/03/2021
 */
@Module
abstract class HotelSearchMapViewModelModule {
    @HotelSearchMapScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelSearchMapViewModel::class)
    internal abstract fun hotelSearchMapResultViewModel(viewModel: HotelSearchMapViewModel): ViewModel
}