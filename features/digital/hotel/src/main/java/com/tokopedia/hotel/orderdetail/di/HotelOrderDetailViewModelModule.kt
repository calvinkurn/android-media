package com.tokopedia.hotel.orderdetail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 09/05/19
 */

@Module
@HotelOrderDetailScope
abstract class HotelOrderDetailViewModelModule {

    @HotelOrderDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelOrderDetailViewModel::class)
    internal abstract fun hotelOrderDetailViewModel(viewModel: HotelOrderDetailViewModel):ViewModel
}