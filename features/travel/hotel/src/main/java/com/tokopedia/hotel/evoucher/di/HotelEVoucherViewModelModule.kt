package com.tokopedia.hotel.evoucher.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
abstract class HotelEVoucherViewModelModule {

    @HotelEVoucherScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelEVoucherViewModel::class)
    internal abstract fun hotelEVoucherViewModel(viewModel: HotelEVoucherViewModel): ViewModel

}