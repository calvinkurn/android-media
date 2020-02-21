package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address.AddressListFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import dagger.Component

@PreferenceEditScope
@Component(modules = [PreferenceEditModule::class, PreferenceEditViewModelModule::class], dependencies = [BaseAppComponent::class])

interface PreferenceEditComponent{
    fun inject(addressListFragment: AddressListFragment )
    fun inject(shippingDurationFragment: ShippingDurationFragment)
}