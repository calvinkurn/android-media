package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.product.detail.R
import com.tokopedia.user.session.UserSession
import org.junit.Assert

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

    fun waitFor(time: Long = 500) {
        Thread.sleep(time)
    }

    infix fun assertTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)

    fun validate(cassavaTestRule: CassavaTestRule, fileName: String) {
        assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    fun assertIsLoggedIn(context: Context, actualIsLoggedIn: Boolean) {
        val userSession = UserSession(context)
        Assert.assertEquals(userSession.isLoggedIn, actualIsLoggedIn)
    }
}

fun actionTest(action: ProductDetailPageRobot.() -> Unit) = ProductDetailPageRobot().apply(action)
