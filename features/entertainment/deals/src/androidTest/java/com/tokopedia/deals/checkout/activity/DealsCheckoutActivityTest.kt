package com.tokopedia.deals.checkout.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.deals.checkout.mock.DealsCheckoutMockData
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.deals.test.R
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@CassavaTest
class DealsCheckoutActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
        ActivityTestRule(DealsCheckoutActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()

        val intent = Intent(context, DealsCheckoutActivity::class.java).apply {
            putExtra(DealsCheckoutActivity.EXTRA_DEAL_DETAIL, DealsCheckoutMockData.createPDPData())
            putExtra(DealsCheckoutActivity.EXTRA_DEAL_VERIFY, DealsCheckoutMockData.createVerifyData())
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun testCheckoutDeals() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null))

        clickPromo()
        clickCheckoutPayment()

        Assert.assertThat(cassavaTestRule.validate(
            ANALYTIC_VALIDATOR_QUERY_DEALS_CHECKOUT_PAGE
        ), hasAllSuccess())
    }

    private fun clickPromo() {
        onView(withId(R.id.ticker_promocode)).perform(click())
    }

    private fun clickCheckoutPayment() {
        onView(withId(R.id.btn_select_payment_method)).perform(click())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_CHECKOUT_PAGE = "tracker/entertainment/deals/deals_checkout_tracking.json"
    }
}
