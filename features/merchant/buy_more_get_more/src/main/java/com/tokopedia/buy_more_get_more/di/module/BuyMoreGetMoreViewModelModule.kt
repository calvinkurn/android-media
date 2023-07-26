package com.tokopedia.buy_more_get_more.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buy_more_get_more.di.scope.BuyMoreGetMoreScope
import com.tokopedia.buy_more_get_more.presentation.olp.OfferLandingPageViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class BuyMoreGetMoreViewModelModule {

    @BuyMoreGetMoreScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OfferLandingPageViewModel::class)
    internal abstract fun provideOfferLandingPageViewMode(viewModel: OfferLandingPageViewModel): ViewModel
}
