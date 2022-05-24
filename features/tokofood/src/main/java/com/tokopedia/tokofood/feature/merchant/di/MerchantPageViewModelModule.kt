package com.tokopedia.tokofood.feature.merchant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MerchantPageViewModelModule {

    @MerchantPageScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MerchantPageViewModel::class)
    abstract fun provideMerchantPageViewModel(viewModel: MerchantPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderCustomizationViewModel::class)
    abstract fun provideOrderCustomizationViewModel(viewModel: OrderCustomizationViewModel): ViewModel
}