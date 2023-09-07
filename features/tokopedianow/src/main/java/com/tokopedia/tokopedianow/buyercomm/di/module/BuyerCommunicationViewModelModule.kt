package com.tokopedia.tokopedianow.buyercomm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.buyercomm.di.scope.BuyerCommunicationScope
import com.tokopedia.tokopedianow.buyercomm.presentation.viewmodel.TokoNowBuyerCommunicationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BuyerCommunicationViewModelModule {

    @Binds
    @BuyerCommunicationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowBuyerCommunicationViewModel::class)
    internal abstract fun provideTokoNowBuyerCommunicationViewModel(viewModelTokoNow: TokoNowBuyerCommunicationViewModel): ViewModel
}
