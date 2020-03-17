package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.DaggerOrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import javax.inject.Inject

class OrderSummaryPageActivity : BaseSimpleActivity(), HasComponent<OrderSummaryPageComponent> {

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    override fun getNewFragment(): Fragment? {
        return OrderSummaryPageFragment()
    }

    override fun getComponent(): OrderSummaryPageComponent {
        return DaggerOrderSummaryPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }
}