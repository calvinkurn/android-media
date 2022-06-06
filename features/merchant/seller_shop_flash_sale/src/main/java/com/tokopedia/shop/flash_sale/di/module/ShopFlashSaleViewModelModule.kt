package com.tokopedia.shop.flash_sale.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.flash_sale.di.scope.ShopFlashSaleScope
import com.tokopedia.shop.flash_sale.presentation.campaign_list.container.CampaignListContainerViewModel
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_rule.bottomsheet.MerchantCampaignTNCViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class ShopFlashSaleViewModelModule {

    @ShopFlashSaleScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CampaignListContainerViewModel::class)
    internal abstract fun provideCampaignListContainerViewModel(viewModel: CampaignListContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MerchantCampaignTNCViewModel::class)
    internal abstract fun provideMerchantCampaignTNCViewModel(viewModel: MerchantCampaignTNCViewModel): ViewModel

}