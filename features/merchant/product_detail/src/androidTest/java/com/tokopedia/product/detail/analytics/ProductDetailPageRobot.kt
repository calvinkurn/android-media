package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.product.detail.R

class ProductDetailPageRobot {

    fun clickBuyNow() {
        onView(ViewMatchers.withId(R.id.btn_buy_now)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun clickAddToCart() {
        onView(ViewMatchers.withId(R.id.btn_add_to_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    infix fun assertTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}

fun actionTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)
