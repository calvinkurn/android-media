package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.oneclickcheckout.order.di.DaggerOrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.di.TestOrderSummaryPageModule

// For running OrderSummaryPageActivity with TestOrderSummaryPageModule
class TestOrderSummaryPageActivity: OrderSummaryPageActivity() {

    override fun getComponent(): OrderSummaryPageComponent {
        return DaggerOrderSummaryPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .orderSummaryPageModule(TestOrderSummaryPageModule(this))
                .build()
    }
}