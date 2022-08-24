package com.tokopedia.campaign.components.bottomsheet.bulkapply.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.CampaignManageProductBulkApplyBottomSheetViewModel
import com.tokopedia.campaign.components.bottomsheet.bulkapply.di.scope.CampaignManageProductBulkApplyBottomSheetScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CampaignManageProductBulkApplyBottomSheetViewModelModule {

    @CampaignManageProductBulkApplyBottomSheetScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CampaignManageProductBulkApplyBottomSheetViewModel::class)
    internal abstract fun provideCampaignManageProductBulkApplyViewModel(viewModel: CampaignManageProductBulkApplyBottomSheetViewModel): ViewModel
}