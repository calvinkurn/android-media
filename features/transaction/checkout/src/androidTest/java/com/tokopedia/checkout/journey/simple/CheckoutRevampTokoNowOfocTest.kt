package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.RATES_SELLY_DEFAULT_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SAF_TOKONOW_OFOC_MERGE_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SAF_TOKONOW_OFOC_MERGE_WITH_POPUP_ACTION_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SAF_TOKONOW_OFOC_SPLIT_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SELLY_NO_PROMO_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.VALIDATE_USE_SELLY_2HR_RESPONSE
import com.tokopedia.checkout.interceptor.VALIDATE_USE_SELLY_DEFAULT_RESPONSE
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampTokoNowOfocTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<RevampShipmentActivity>(RevampShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var interceptor = CheckoutInterceptor

    @Before
    fun before() {
        interceptor.resetAllCustomResponse()
        interceptor.setupGraphqlMockResponse(context)
    }

    @Test
    fun merge_change_to_split() {
        interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_MERGE_RESPONSE_PATH
        interceptor.logisticInterceptor.customRatesResponsePath = RATES_SELLY_DEFAULT_RESPONSE_PATH
        interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_DEFAULT_RESPONSE
        activityRule.launchActivity(null)
        intending(anyIntent())
            .respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for page load
            waitForData()
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            waitForData()
            interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_SPLIT_RESPONSE_PATH
            interceptor.logisticInterceptor.customSellyResponsePath = SELLY_NO_PROMO_RESPONSE_PATH
            interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_2HR_RESPONSE
            click2hrOption(activityRule)
            waitForData()
            assertHas2hrSelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = null,
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp135.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp15.000",
                shippingOriginalPrice = "Rp38.000",
                totalPrice = "Rp154.900"
            )
            assertPlatformFee(
                activityRule,
                fee = "Rp3.000",
                originalFee = null
            )
            clickChoosePaymentButton(activityRule)
        }
    }

    @Test
    fun merge_change_to_split_with_popup() {
        interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_MERGE_WITH_POPUP_ACTION_RESPONSE_PATH
        interceptor.logisticInterceptor.customRatesResponsePath = RATES_SELLY_DEFAULT_RESPONSE_PATH
        interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_DEFAULT_RESPONSE
        activityRule.launchActivity(null)
        intending(anyIntent())
            .respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for page load
            waitForData()
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            waitForData()
            interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_SPLIT_RESPONSE_PATH
            interceptor.logisticInterceptor.customSellyResponsePath = SELLY_NO_PROMO_RESPONSE_PATH
            interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_2HR_RESPONSE
            click2hrOption(activityRule)
            waitForData()
            clickPrimaryButtonDialogUnify()
            waitForData()
            assertHas2hrSelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = null,
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp135.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp15.000",
                shippingOriginalPrice = "Rp38.000",
                totalPrice = "Rp154.900"
            )
            assertPlatformFee(
                activityRule,
                fee = "Rp3.000",
                originalFee = null
            )
            clickChoosePaymentButton(activityRule)
        }
    }

    @Test
    fun merge_change_to_split_with_popup_then_decline() {
        interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_MERGE_WITH_POPUP_ACTION_RESPONSE_PATH
        interceptor.logisticInterceptor.customRatesResponsePath = RATES_SELLY_DEFAULT_RESPONSE_PATH
        interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_DEFAULT_RESPONSE
        activityRule.launchActivity(null)
        intending(anyIntent())
            .respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for page load
            waitForData()
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            waitForData()
            interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_OFOC_SPLIT_RESPONSE_PATH
            interceptor.logisticInterceptor.customSellyResponsePath = SELLY_NO_PROMO_RESPONSE_PATH
            interceptor.promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_SELLY_2HR_RESPONSE
            click2hrOption(activityRule)
            waitForData()
            clickSecondaryButtonDialogUnify()
            waitForData()
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp135.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp0",
                shippingOriginalPrice = "Rp15.000",
                totalPrice = "Rp138.900"
            )
            assertPlatformFee(
                activityRule,
                fee = "Rp3.000",
                originalFee = null
            )
            clickChoosePaymentButton(activityRule)
        }
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
