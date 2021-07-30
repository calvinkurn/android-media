package com.tokopedia.promocheckout.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListHotelViewModel
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author: astidhiyaa on 30/07/21.
 */
@Module
abstract class PromoCheckoutListViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutListViewModel::class)
    internal abstract fun providePromoCheckoutListViewModel(viewModel: PromoCheckoutListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutListHotelViewModel::class)
    internal abstract fun provideHotelPromoCheckoutListViewModel(viewModel: PromoCheckoutListHotelViewModel): ViewModel
}