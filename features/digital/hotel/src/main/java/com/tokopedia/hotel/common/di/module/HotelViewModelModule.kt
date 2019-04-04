package com.tokopedia.hotel.common.di.module

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.hotel.common.di.scope.HotelScope
import dagger.Binds
import dagger.Module

@Module
@HotelScope
abstract class HotelViewModelModule{

    @HotelScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}