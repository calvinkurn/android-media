package com.tokopedia.tokofood.feature.merchant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.ManageLocationViewModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MerchantPageViewModelModule {

    @TokoFoodScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @TokoFoodScope
    @Binds
    @IntoMap
    @ViewModelKey(MerchantPageViewModel::class)
    abstract fun provideMerchantPageViewModel(viewModel: MerchantPageViewModel): ViewModel

    @TokoFoodScope
    @Binds
    @IntoMap
    @ViewModelKey(OrderCustomizationViewModel::class)
    abstract fun provideOrderCustomizationViewModel(viewModel: OrderCustomizationViewModel): ViewModel

    @TokoFoodScope
    @Binds
    @IntoMap
    @ViewModelKey(ManageLocationViewModel::class)
    abstract fun provideManageLocationViewModel(viewModel: ManageLocationViewModel): ViewModel
}
