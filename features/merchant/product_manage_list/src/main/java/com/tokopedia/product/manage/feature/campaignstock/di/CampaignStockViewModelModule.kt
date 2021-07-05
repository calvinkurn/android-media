package com.tokopedia.product.manage.feature.campaignstock.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignMainStockViewModel
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CampaignStockViewModelModule {

    @CampaignStockScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CampaignStockViewModel::class)
    internal abstract fun provideCampaignStockViewModel(campaignStockViewModel: CampaignStockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignMainStockViewModel::class)
    internal abstract fun provideCampaignMainStockViewModel(campaignMainStockViewModel: CampaignMainStockViewModel): ViewModel
}