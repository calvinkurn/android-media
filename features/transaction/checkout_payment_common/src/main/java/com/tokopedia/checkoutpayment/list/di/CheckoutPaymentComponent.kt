package com.tokopedia.checkoutpayment.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.checkoutpayment.list.view.PaymentListingActivity
import com.tokopedia.checkoutpayment.list.view.PaymentListingFragment
import com.tokopedia.checkoutpayment.topup.view.PaymentTopUpWebViewFragment
import dagger.Component

@CheckoutPaymentScope
@Component(modules = [CheckoutPaymentModule::class, CheckoutPaymentViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutPaymentComponent {
    fun inject(paymentListingActivity: PaymentListingActivity)
    fun inject(paymentListingFragment: PaymentListingFragment)
    fun inject(paymentTopUpWebViewFragment: PaymentTopUpWebViewFragment)
}
