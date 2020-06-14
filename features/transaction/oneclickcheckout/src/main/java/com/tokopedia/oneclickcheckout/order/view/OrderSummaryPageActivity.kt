package com.tokopedia.oneclickcheckout.order.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.purchase_platform.common.constant.Constant
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.di.DaggerOrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import javax.inject.Inject

class OrderSummaryPageActivity : BaseSimpleActivity(), HasComponent<OrderSummaryPageComponent> {

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    override fun getNewFragment(): Fragment? {
        return OrderSummaryPageFragment.newInstance(intent.getBooleanExtra(Constant.EXTRA_OCC_SOURCE_PDP, false))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderSummaryAnalytics = OrderSummaryAnalytics()
    }

    override fun getComponent(): OrderSummaryPageComponent {
        return DaggerOrderSummaryPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onBackPressed() {
        orderSummaryAnalytics.eventClickBackFromOSP()
        val currFragment = fragment
        if (currFragment is OrderSummaryPageFragment) {
            currFragment.setIsFinishing()
        }
        super.onBackPressed()
    }
}