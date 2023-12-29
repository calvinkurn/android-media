package com.tokopedia.shop.campaign.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.campaign.di.scope.ShopCampaignScope
import com.tokopedia.shop.campaign.view.viewmodel.ExclusiveLaunchVoucherListViewModel
import com.tokopedia.shop.campaign.view.viewmodel.VoucherDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopCampaignViewModelModule {

    @ShopCampaignScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ExclusiveLaunchVoucherListViewModel::class)
    internal abstract fun provideExclusiveLaunchVoucherListViewModel(viewModel: ExclusiveLaunchVoucherListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherDetailViewModel::class)
    internal abstract fun provideVoucherDetailViewModel(viewModel: VoucherDetailViewModel): ViewModel
}
