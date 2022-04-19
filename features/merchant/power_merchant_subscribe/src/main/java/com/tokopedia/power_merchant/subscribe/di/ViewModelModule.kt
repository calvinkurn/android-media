package com.tokopedia.power_merchant.subscribe.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.power_merchant.subscribe.view.viewmodel.BenefitPackageViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.DeactivationViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSharedViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @PowerMerchantSubscribeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(DeactivationViewModel::class)
    internal abstract fun provideDeactivationViewModel(viewModel: DeactivationViewModel): ViewModel

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PowerMerchantSubscriptionViewModel::class)
    internal abstract fun providePowerMerchantSubscriptionViewModel(viewModel: PowerMerchantSubscriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PowerMerchantSharedViewModel::class)
    internal abstract fun providePmSubscriptionActivityViewModel(viewModel: PowerMerchantSharedViewModel): ViewModel

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(BenefitPackageViewModel::class)
    internal abstract fun provideBenefitPackageViewModel(viewModel: BenefitPackageViewModel): ViewModel
}