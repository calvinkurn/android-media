package com.tokopedia.buyerorder.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorder.detail.revamp.activity.RevampOrderListDetailActivity
import com.tokopedia.buyerorder.detail.revamp.activity.SeeInvoiceActivity
import com.tokopedia.buyerorder.detail.view.fragment.OmsDetailFragment
import com.tokopedia.buyerorder.detail.view.fragment.OrderListDetailFragment
import dagger.Component

/**
 * Created by baghira on 10/05/18.
 */
@OrderDetailScope
@Component(dependencies = [BaseAppComponent::class], modules = [OrderDetailModule::class, OrderDetailViewModelModule::class])
interface OrderDetailsComponent {

    fun inject(orderListDetailFragment: OrderListDetailFragment)
    fun inject(omsDetailFragment: OmsDetailFragment)
    fun inject(seeInvoiceActivity: SeeInvoiceActivity)
    fun inject(revampOrderListDetailActivity: RevampOrderListDetailActivity)
    fun inject(fragment: com.tokopedia.buyerorder.detail.revamp.fragment.OmsDetailFragment)
}