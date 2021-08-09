package com.tokopedia.promocheckout.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promocheckout.detail.view.viewmodel.*
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
    internal abstract fun providePromoCheckoutDetailHotelViewModel(viewModel: PromoCheckoutDetailHotelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutDetailFlightViewModel::class)
    internal abstract fun providePromoCheckoutDetailFlightViewModel(viewModel: PromoCheckoutDetailFlightViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutDetailDealsViewModel::class)
    internal abstract fun providePromoCheckoutDetailDealsViewModel(viewModel: PromoCheckoutDetailDealsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutDetailDigitalViewModel::class)
    internal abstract fun providePromoCheckoutDetailDigitalViewModel(viewModel: PromoCheckoutDetailDigitalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutDetailEventViewModel::class)
    internal abstract fun providePromoCheckoutDetailEventViewModel(viewModel: PromoCheckoutDetailEventViewModel): ViewModel
}