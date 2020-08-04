package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.preference.list.view.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityTest {

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null
    private val cls: String? = null
    private val activityMonitor = Instrumentation.ActivityMonitor(cls, null, true)

    private val cartInterceptor = OrderSummaryPageInterceptor.cartInterceptor
    private val logisticInterceptor = OrderSummaryPageInterceptor.logisticInterceptor
    private val promoInterceptor = OrderSummaryPageInterceptor.promoInterceptor
    private val checkoutInterceptor = OrderSummaryPageInterceptor.checkoutInterceptor

    private fun setupGraphqlMockResponse() {
        val testInterceptors = listOf(cartInterceptor, logisticInterceptor, promoInterceptor, checkoutInterceptor)
        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    @Before
    fun setup() {
        setupGraphqlMockResponse()
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, Intent()))
        Thread.sleep(1000)
        onView(withId(R.id.tv_card_header)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_pay)).perform(click())

        Thread.sleep(2000)
        intended(hasExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, PaymentPassData().apply {
            redirectUrl = "https://www.tokopedia.com/payment"
            queryString = "transaction_id=123"
            method = "POST"
        }))
    }
}