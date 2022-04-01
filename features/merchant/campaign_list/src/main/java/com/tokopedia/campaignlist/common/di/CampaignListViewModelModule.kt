package com.tokopedia.campaignlist.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CampaignListViewModelModule {

    @CampaignListScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CampaignListViewModel::class)
    abstract fun provideCampaignListViewModel(campaignListViewModel: CampaignListViewModel): ViewModel
}