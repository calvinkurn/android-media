package com.tokopedia.oneclickcheckout.payment.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oneclickcheckout.payment.list.view.PaymentListingActivity
import com.tokopedia.oneclickcheckout.payment.list.view.PaymentListingFragment
import com.tokopedia.oneclickcheckout.payment.topup.view.OvoTopUpWebViewFragment
import dagger.Component

@PaymentScope
@Component(modules = [PaymentModule::class, PaymentViewModelModule::class], dependencies = [BaseAppComponent::class])
interface PaymentComponent{
    fun inject(paymentListingActivity: PaymentListingActivity)
    fun inject(paymentListingFragment: PaymentListingFragment)
    fun inject(ovoTopUpWebViewFragment: OvoTopUpWebViewFragment)
}