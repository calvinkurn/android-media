package com.tokopedia.shop.home.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.home.view.viewmodel.ShopHomeNplCampaignTncBottomSheetViewModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopPageHomeViewModelModule {

    @ShopPageHomeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopHomeViewModel::class)
    internal abstract fun shopPageHomeViewModel(viewModel: ShopHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopHomeNplCampaignTncBottomSheetViewModel::class)
    internal abstract fun shopHomeNplCampaignTncBottomSheetViewModel(viewModel: ShopHomeNplCampaignTncBottomSheetViewModel): ViewModel

}