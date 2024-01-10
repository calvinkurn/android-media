package com.tokopedia.oneclickcheckout.order.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.di.DaggerOrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageModule
import com.tokopedia.purchase_platform.common.constant.ARGS_LIST_AUTO_APPLY_PROMO
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import com.tokopedia.telemetry.ITelemetryActivity
import javax.inject.Inject

open class OrderSummaryPageActivity :
    BaseSimpleActivity(),
    HasComponent<OrderSummaryPageComponent>,
    ITelemetryActivity {

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    override fun getNewFragment(): Fragment? {
        return OrderSummaryPageFragment.newInstance(
            intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_PRODUCT_ID),
            intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_GATEWAY_CODE),
            intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_TENURE_TYPE),
            intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_SOURCE),
            intent?.getParcelableArrayListExtra<PromoExternalAutoApply>(ARGS_LIST_AUTO_APPLY_PROMO)?.toList() ?: emptyList()
            /*intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_PROMO_CODE),
            intent?.data?.getQueryParameter(OrderSummaryPageFragment.QUERY_PROMO_TYPE)*/
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun getComponent(): OrderSummaryPageComponent {
        return DaggerOrderSummaryPageComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .orderSummaryPageModule(OrderSummaryPageModule(this))
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

    override fun getTelemetrySectionName() = "checkout"
}
