package com.tokopedia.oneclickcheckout.payment.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oneclickcheckout.payment.topup.view.PaymentTopUpWebViewFragment
import dagger.Component

@PaymentScope
@Component(modules = [PaymentModule::class, PaymentViewModelModule::class], dependencies = [BaseAppComponent::class])
interface PaymentComponent {
    fun inject(paymentTopUpWebViewFragment: PaymentTopUpWebViewFragment)
}
