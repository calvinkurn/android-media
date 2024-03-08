package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.SAF_BMSM_DISCOUNT_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SAF_BMSM_GWP_RESPONSE_PATH
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampBmsmTest {

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
    fun bmsm_discount() {
        interceptor.cartInterceptor.customSafResponsePath =
            SAF_BMSM_DISCOUNT_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            assertBmsmProduct(
                activityRule,
                productIndex = 0,
                bmsmTitle = "Potongan Rp20rb • BMTH1"
            )
            waitForData()

            expandShoppingSummary(activityRule)
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp20.000",
                itemOriginalPrice = "Rp40.000",
                shippingTotalPrice = null,
                shippingOriginalPrice = null,
                totalPrice = "-"
            )

            // choose shipping
            clickChooseDuration(activityRule)
            waitForData()
            selectDurationOptionWithText("Reguler (Rp93.000)")
            waitForData()

            // assert add ons value
            expandShoppingSummary(activityRule)
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp20.000",
                itemOriginalPrice = "Rp40.000",
                shippingTotalPrice = "Rp93.000",
                shippingOriginalPrice = null,
                totalPrice = "Rp116.000"
            )
            assertPlatformFee(activityRule, fee = "Rp3.000", originalFee = null)

            clickChoosePaymentButton(activityRule)
        }
    }

    @Test
    fun bmsm_gwp() {
        interceptor.cartInterceptor.customSafResponsePath =
            SAF_BMSM_GWP_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            assertBmsmProduct(
                activityRule,
                productIndex = 0,
                bmsmTitle = "Potongan Rp20rb • BMTH1"
            )
            waitForData()
            assertBmsmProductBenefit(
                activityRule,
                productIndex = 2,
                title = "gift 1 nih",
                description = "1 x Rp0"
            )
            waitForData()
            assertBmsmProductBenefit(
                activityRule,
                productIndex = 3,
                title = "gift 2 nih",
                description = "2 x Rp0"
            )

            expandShoppingSummary(activityRule)
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp20.000",
                itemOriginalPrice = "Rp40.000",
                shippingTotalPrice = null,
                shippingOriginalPrice = null,
                totalPrice = "-"
            )

            // choose shipping
            clickChooseDuration(activityRule)
            waitForData()
            selectDurationOptionWithText("Reguler (Rp93.000)")
            waitForData()

            // assert add ons value
            expandShoppingSummary(activityRule)
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp20.000",
                itemOriginalPrice = "Rp40.000",
                shippingTotalPrice = "Rp93.000",
                shippingOriginalPrice = null,
                totalPrice = "Rp116.000"
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
