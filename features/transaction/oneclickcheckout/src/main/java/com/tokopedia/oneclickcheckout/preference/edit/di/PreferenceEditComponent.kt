package com.tokopedia.oneclickcheckout.preference.edit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup.OvoTopUpWebViewFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.summary.PreferenceSummaryFragment
import dagger.Component

@PreferenceEditScope
@Component(modules = [PreferenceEditModule::class, PreferenceEditViewModelModule::class], dependencies = [BaseAppComponent::class])

interface PreferenceEditComponent{
    fun inject(preferenceEditActivity: PreferenceEditActivity)
    fun inject(addressListFragment: AddressListFragment )
    fun inject(shippingDurationFragment: ShippingDurationFragment)
    fun inject(summaryFragment: PreferenceSummaryFragment)
    fun inject(paymentMethodFragment: PaymentMethodFragment)
    fun inject(ovoTopUpWebViewFragment: OvoTopUpWebViewFragment)
}