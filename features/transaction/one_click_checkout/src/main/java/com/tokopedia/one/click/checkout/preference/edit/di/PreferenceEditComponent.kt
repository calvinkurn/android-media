package com.tokopedia.one.click.checkout.preference.edit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.one.click.checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.one.click.checkout.preference.edit.view.address.AddressListFragment
import com.tokopedia.one.click.checkout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.one.click.checkout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.one.click.checkout.preference.edit.view.summary.PreferenceSummaryFragment
import dagger.Component

@PreferenceEditScope
@Component(modules = [PreferenceEditModule::class, PreferenceEditViewModelModule::class], dependencies = [BaseAppComponent::class])

interface PreferenceEditComponent{
    fun inject(preferenceEditActivity: PreferenceEditActivity)
    fun inject(addressListFragment: AddressListFragment )
    fun inject(shippingDurationFragment: ShippingDurationFragment)
    fun inject(summaryFragment: PreferenceSummaryFragment)
    fun inject(paymentMethodFragment: PaymentMethodFragment)
}