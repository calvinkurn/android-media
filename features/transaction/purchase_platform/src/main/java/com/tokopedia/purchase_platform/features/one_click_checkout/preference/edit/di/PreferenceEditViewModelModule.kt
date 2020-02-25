package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address.AddressListViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary.PreferenceSummaryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@PreferenceEditScope
@Module
abstract class PreferenceEditViewModelModule {

    @PreferenceEditScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddressListViewModel::class)
    internal abstract fun provideAddressListViewModel(viewModel: AddressListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShippingDurationViewModel::class)
    internal abstract fun provideShippingDurationViewModel(viewModel: ShippingDurationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreferenceSummaryViewModel::class)
    internal abstract fun providePreferenceSummaryViewModel(viewModel: PreferenceSummaryViewModel): ViewModel
}