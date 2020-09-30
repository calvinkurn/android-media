package com.tokopedia.product.detail.analytics

import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.analytics.ProductDetailActivityTestUtil.performClickMatches

class ProductDetailPageRobot {
    fun clickButtonBuy() = performClickMatches(R.id.btn_buy_now)
    fun clickAddToCart() = performClickMatches(R.id.btn_add_to_cart)
    fun clickGuideSizeChart() = performClickMatches(R.id.txtVariantGuideline)

    infix fun assertTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}

fun actionTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)
