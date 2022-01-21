package com.tokopedia.ordermanagement.buyercancellationorder.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.ordermanagement.buyercancellationorder.di.scope.BuyerCancellationOrderScope
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.viewmodel.BuyerCancellationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GetCancellationReasonViewModelModule {
    @BuyerCancellationOrderScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @BuyerCancellationOrderScope
    @Binds
    @IntoMap
    @ViewModelKey(BuyerCancellationViewModel::class)
    internal abstract fun buyerGetCancellationReasonViewModel(viewModel: BuyerCancellationViewModel): ViewModel
}