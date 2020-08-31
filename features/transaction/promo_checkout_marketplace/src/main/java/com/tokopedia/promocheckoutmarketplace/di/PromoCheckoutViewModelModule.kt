package com.tokopedia.promocheckoutmarketplace.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promocheckoutmarketplace.presentation.viewmodel.PromoCheckoutViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@PromoCheckoutMarketplaceScope
abstract class PromoCheckoutViewModelModule {

    @PromoCheckoutMarketplaceScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutViewModel::class)
    internal abstract fun promoCheckoutViewModel(viewModel: PromoCheckoutViewModel): ViewModel

}