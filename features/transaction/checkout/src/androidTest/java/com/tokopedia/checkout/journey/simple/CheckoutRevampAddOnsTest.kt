package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.SAF_FREE_ADDONS_RESPONSE_PATH
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampAddOnsTest {

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
    fun free_addons() {
        interceptor.cartInterceptor.customSafResponsePath =
            SAF_FREE_ADDONS_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            assertAddOnsProduct(
                activityRule,
                productIndex = 0,
                addOnsName = "jasa pasang ac",
                addOnsPrice = " (Rp0)",
                isChecked = false
            )
            assertAddOnsShoppingSummary(
                activityRule,
                hasAddOns = false,
                addOnsName = "jasa pasang",
                addOnsPrice = null
            )

            // choose shipping
            clickChooseDuration(activityRule)
            waitForData()
            selectDurationOptionWithText("Reguler (Rp93.000)")
            waitForData()

            // assert add ons value
            assertAddOnsProduct(
                activityRule,
                productIndex = 0,
                addOnsName = "jasa pasang ac",
                addOnsPrice = " (Rp0)",
                isChecked = false
            )
            assertAddOnsShoppingSummary(
                activityRule,
                hasAddOns = false,
                addOnsName = "jasa pasang",
                addOnsPrice = null
            )
            expandShoppingSummary(activityRule)
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp60.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp93.000",
                shippingOriginalPrice = null,
                totalPrice = "Rp156.000"
            )
            assertPlatformFee(activityRule, fee = "Rp3.000", originalFee = null)

            clickChoosePaymentButton(activityRule)
        }
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
