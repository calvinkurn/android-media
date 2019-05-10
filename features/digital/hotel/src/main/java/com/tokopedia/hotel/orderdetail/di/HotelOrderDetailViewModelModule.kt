package com.tokopedia.hotel.orderdetail.di

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.common.presentation.viewmodel.ViewModelFactory
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

//    @Binds
//    @IntoMap
//    @ViewModelKey(HotelHomepageViewModel::class)
//    internal abstract fun hotelDestinationViewModel(viewModel: HotelHomepageViewModel): ViewModel
}