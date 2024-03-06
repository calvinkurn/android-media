package com.tokopedia.sellerorder.buyer_request_cancel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.buyer_request_cancel.presentation.BuyerRequestCancelRespondViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BuyerRequestCancelRespondViewModelModule {
    @BuyerRequestCancelRespondScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BuyerRequestCancelRespondViewModel::class)
    internal abstract fun buyerRequestCancelRespondViewModel(viewModel: BuyerRequestCancelRespondViewModel): ViewModel
}
