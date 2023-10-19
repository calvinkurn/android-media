package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.SAF_OWOC_DEFAULT_RESPONSE_PATH
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampOwocTest {

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
    fun newUiGroupType() {
        interceptor.cartInterceptor.customSafResponsePath =
            SAF_OWOC_DEFAULT_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            assertNewUiGroupType(activityRule, 0)
            assertNewUiGroupType(activityRule, 1)
            clickChooseDuration(activityRule)
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            scrollRecyclerViewToChoosePaymentButton(activityRule)
            waitForData()
            waitForData()
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
