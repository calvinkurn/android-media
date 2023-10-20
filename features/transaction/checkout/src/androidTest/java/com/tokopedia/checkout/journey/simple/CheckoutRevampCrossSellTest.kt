package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.SAF_EGOLD_DONASI_RESPONSE_PATH
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampCrossSellTest {

    @get:Rule
    var activityRule = object :
        IntentsTestRule<RevampShipmentActivity>(RevampShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var interceptor = CheckoutInterceptor

    @Before
    fun before() {
        interceptor.setupGraphqlMockResponse(context)
    }

    @Test
    fun egold() {
        interceptor.cartInterceptor.customSafResponsePath =
            SAF_EGOLD_DONASI_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            assertEgold(
                activityRule,
                text = "Bulatkan dengan nabung emas. S&K berlaku (Rp5.000)",
                isChecked = false
            )
//            assertNewUiGroupType(activityRule, 0)
//            assertNewUiGroupType(activityRule, 1)
            clickChooseDuration(activityRule)
            waitForData()
            selectDurationOptionWithText("Reguler (Rp93.000)")
            waitForData()
            assertEgold(
                activityRule,
                text = "Bulatkan dengan nabung emas. S&K berlaku (Rp7.000)",
                isChecked = false
            )
//            scrollRecyclerViewToChoosePaymentButton(activityRule)
            waitForData()
//            waitForData()
//            clickChoosePaymentButton(activityRule)
//        } validateAnalytics {
//            waitForData()
//            assertGoToPayment()
        }
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
