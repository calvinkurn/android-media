package com.tokopedia.buyerorderdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BuyerOrderDetailViewModelModule {
    @Binds
    @BuyerOrderDetailScope
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BuyerOrderDetailViewModel::class)
    abstract fun provideBuyerOrderDetailViewModel(viewModel: BuyerOrderDetailViewModel): ViewModel
}