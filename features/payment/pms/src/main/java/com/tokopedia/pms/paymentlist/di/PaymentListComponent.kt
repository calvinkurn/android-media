package com.tokopedia.pms.paymentlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pms.payment.view.activity.PaymentListActivity
import com.tokopedia.pms.payment.view.fragment.PaymentListFragment
import com.tokopedia.pms.payment.view.fragment.PaymentListFragmentK
import com.tokopedia.pms.paymentlist.di.module.PaymentListModule
import com.tokopedia.pms.paymentlist.di.module.ViewModelModule
import dagger.Component

@PaymentListScope
@Component(
    modules = [PaymentListModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PaymentListComponent {
    fun inject(paymentListFragment: PaymentListFragmentK)
}