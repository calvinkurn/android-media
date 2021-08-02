package com.tokopedia.promocheckout.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promocheckout.detail.view.viewmodel.PromoCheckoutDetailHotelViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author: astidhiyaa on 02/08/21.
 */
@Module
abstract class PromoCheckoutDetailViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutDetailHotelViewModel::class)
    internal abstract fun providePromoCheckoutDetailhotelViewModel(viewModel: PromoCheckoutDetailHotelViewModel): ViewModel

}