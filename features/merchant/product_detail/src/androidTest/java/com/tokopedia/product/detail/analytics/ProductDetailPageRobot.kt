package com.tokopedia.product.detail.analytics

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.analytics.ProductDetailActivityTestUtil.performClickMatches
import org.hamcrest.MatcherAssert

class ProductDetailPageRobot {
    fun clickButtonBuy() = performClickMatches(R.id.btn_buy_now)
    fun clickAddToCart() = performClickMatches(R.id.btn_add_to_cart)

    infix fun assertTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}

fun actionTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)
