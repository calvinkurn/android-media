package com.tokopedia.hotel.evoucher.di

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author by furqan on 14/05/19
 */

@Module
@HotelEVoucherScope
abstract class HotelEVoucherViewModelModule {

    @HotelEVoucherScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}