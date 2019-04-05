package com.tokopedia.hotel.homepage.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
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
@HotelHomepageScope
abstract class HotelHomepageViewModelModule {

    @HotelHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelHomepageViewModel::class)
    internal abstract fun hotelDestinationViewModel(viewModel: HotelHomepageViewModel): ViewModel
}