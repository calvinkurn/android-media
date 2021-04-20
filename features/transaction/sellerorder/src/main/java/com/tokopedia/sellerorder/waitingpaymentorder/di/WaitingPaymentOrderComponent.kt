package com.tokopedia.sellerorder.waitingpaymentorder.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.fragment.WaitingPaymentOrderFragment
import dagger.Component

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

@WaitingPaymentOrderScope
@Component(modules = [WaitingPaymentOrderViewModelModule::class], dependencies = [SomComponent::class])
interface WaitingPaymentOrderComponent {
    fun inject(waitingPaymentOrderFragment: WaitingPaymentOrderFragment)
}