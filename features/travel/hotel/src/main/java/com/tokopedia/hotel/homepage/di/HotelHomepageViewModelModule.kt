package com.tokopedia.hotel.homepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 04/04/19
 */
@Module
abstract class HotelHomepageViewModelModule {

    @HotelHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelHomepageViewModel::class)
    internal abstract fun hotelDestinationViewModel(viewModel: HotelHomepageViewModel): ViewModel
}