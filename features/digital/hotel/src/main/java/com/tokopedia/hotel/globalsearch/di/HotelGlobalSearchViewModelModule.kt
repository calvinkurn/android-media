package com.tokopedia.hotel.globalsearch.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author by furqan on 19/11/2019
 */
@Module
@HotelGlobalSearchScope
abstract class HotelGlobalSearchViewModelModule {

    @HotelGlobalSearchScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory



}