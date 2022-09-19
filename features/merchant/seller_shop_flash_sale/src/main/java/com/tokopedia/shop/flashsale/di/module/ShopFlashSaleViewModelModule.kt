package com.tokopedia.shop.flashsale.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.flashsale.di.scope.ShopFlashSaleScope
import com.tokopedia.shop.flashsale.presentation.creation.highlight.ManageHighlightedProductViewModel
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.CampaignInformationViewModel
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDataPickerViewModel
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.VpsPackageViewModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductViewModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.ChooseProductViewModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.EditProductInfoViewModel
import com.tokopedia.shop.flashsale.presentation.creation.rule.CampaignRuleViewModel
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCViewModel
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.ChooseRelatedCampaignViewModel
import com.tokopedia.shop.flashsale.presentation.detail.CampaignDetailViewModel
import com.tokopedia.shop.flashsale.presentation.draft.viewmodel.DraftDeleteViewModel
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerViewModel
import com.tokopedia.shop.flashsale.presentation.list.list.CampaignListViewModel
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.QuotaMonitoringViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(CampaignListViewModel::class)
    internal abstract fun provideCampaignListViewModel(viewModel: CampaignListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DraftDeleteViewModel::class)
    internal abstract fun provideDraftDeleteViewModel(viewModel: DraftDeleteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignDataPickerViewModel::class)
    internal abstract fun provideCampaignDatePickerBottomSheet(viewModel: CampaignDataPickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignInformationViewModel::class)
    internal abstract fun provideCampaignInformationViewModel(viewModel: CampaignInformationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignRuleViewModel::class)
    internal abstract fun provideCampaignRuleViewModel(viewModel: CampaignRuleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseProductViewModel::class)
    internal abstract fun provideChooseProductViewModel(viewModel: ChooseProductViewModel): ViewModel
  
    @Binds
    @IntoMap
    @ViewModelKey(ManageProductViewModel::class)
    internal abstract fun provideManageProductViewModel(viewModel: ManageProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageHighlightedProductViewModel::class)
    internal abstract fun provideManageHighlightedProductViewModel(viewModel: ManageHighlightedProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseRelatedCampaignViewModel::class)
    internal abstract fun provideChooseRelatedCampaignViewModel(viewModel: ChooseRelatedCampaignViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProductInfoViewModel::class)
    internal abstract fun provideEditProductInfoViewModel(viewModel: EditProductInfoViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignDetailViewModel::class)
    internal abstract fun provideCampaignDetailViewModel(viewModel: CampaignDetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VpsPackageViewModel::class)
    internal abstract fun provideVpsPackageViewModel(viewModel: VpsPackageViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuotaMonitoringViewModel::class)
    internal abstract fun provideQuotaMonitoringViewModel(viewModel: QuotaMonitoringViewModel) : ViewModel
}