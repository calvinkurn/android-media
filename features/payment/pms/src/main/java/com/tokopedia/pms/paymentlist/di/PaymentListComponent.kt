package com.tokopedia.pms.paymentlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pms.paymentlist.di.module.PaymentListModule
import com.tokopedia.pms.paymentlist.di.module.ViewModelModule
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import dagger.Component

@PaymentListScope
@Component(
    modules = [PaymentListModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PaymentListComponent {
    fun inject(deferredPaymentListFragment: DeferredPaymentListFragment)
}