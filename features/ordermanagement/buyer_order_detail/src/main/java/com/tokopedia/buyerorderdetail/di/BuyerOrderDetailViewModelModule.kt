package com.tokopedia.buyerorderdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.digital.digital_recommendation.di.DigitalRecommendationViewModelModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [DigitalRecommendationViewModelModule::class])
abstract class BuyerOrderDetailViewModelModule {
    @Binds
    @BuyerOrderDetailScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BuyerOrderDetailViewModel::class)
    internal abstract fun provideBuyerOrderDetailViewModel(viewModel: BuyerOrderDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BuyerOrderDetailExtensionViewModel::class)
    internal abstract fun provideOrderDetailExtensionViewModel(viewModel: BuyerOrderDetailExtensionViewModel): ViewModel
}