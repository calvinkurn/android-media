package com.tokopedia.hotel.evoucher.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.evoucher.presentation.viewmodel.HotelEVoucherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 14/05/19
 */

@Module
@HotelEVoucherScope
abstract class HotelEVoucherViewModelModule {

    @HotelEVoucherScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelEVoucherViewModel::class)
    internal abstract fun hotelEVoucherViewModel(viewModel: HotelEVoucherViewModel): ViewModel

}