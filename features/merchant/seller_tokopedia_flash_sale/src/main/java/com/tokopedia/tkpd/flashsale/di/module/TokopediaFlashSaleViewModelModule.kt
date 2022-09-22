package com.tokopedia.tkpd.flashsale.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tkpd.flashsale.di.scope.TokopediaFlashSaleScope
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.ManageProductVariantViewModel
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel.ChooseProductViewModel
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListViewModel
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleContainerViewModel
import com.tokopedia.tkpd.flashsale.presentation.detail.viewmodel.CampaignDetailBottomSheetViewModel
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailViewModel
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantViewModel
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.FlashSaleManageProductListListViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class TokopediaFlashSaleViewModelModule {

    @TokopediaFlashSaleScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlashSaleContainerViewModel::class)
    internal abstract fun provideFlashSaleContainerViewModel(viewModel: FlashSaleContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlashSaleListViewModel::class)
    internal abstract fun provideFlashSaleListViewModel(viewModel: FlashSaleListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignDetailBottomSheetViewModel::class)
    internal abstract fun provideCampaignDetailBottomSheetViewModel(viewModel: CampaignDetailBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CampaignDetailViewModel::class)
    internal abstract fun provideCampaignDetailViewModel(viewModel: CampaignDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageProductVariantViewModel::class)
    internal abstract fun provideManageProductVariantViewModel(viewModel: ManageProductVariantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseProductViewModel::class)
    internal abstract fun provideChooseProductViewModel(viewModel: ChooseProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageProductNonVariantViewModel::class)
    internal abstract fun provideManageProductNonVariantViewModel(viewModel: ManageProductNonVariantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlashSaleManageProductListListViewModel::class)
    internal abstract fun provideFlashSaleManageProductListListViewModel(viewModel: FlashSaleManageProductListListViewModel): ViewModel

}