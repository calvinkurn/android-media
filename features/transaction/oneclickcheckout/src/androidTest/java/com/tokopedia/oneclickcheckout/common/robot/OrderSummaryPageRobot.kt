package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.action.scrollTo
import com.tokopedia.oneclickcheckout.common.action.swipeUpTop
import com.tokopedia.unifyprinciples.Typography
import org.junit.Assert.assertEquals

fun orderSummaryPage(func: OrderSummaryPageRobot.() -> Unit) = OrderSummaryPageRobot().apply(func)

class OrderSummaryPageRobot {

    fun clickOnboardingInfo() {
        onView(withId(R.id.tv_header_3)).perform(scrollTo()).perform(click())
    }

    fun closeBottomSheet() {
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    fun clickAddPreferenceForNewBuyer() {
        onView(withId(R.id.button_atur_pilihan)).perform(scrollTo()).perform(click())
    }

    fun clickAddProductQuantity() {
        onView(withId(R.id.btn_qty_plus)).perform(scrollTo()).perform(click())
    }

    fun clickEditPreference() {
        onView(withId(R.id.iv_edit_preference)).perform(scrollTo()).check(matches(isDisplayed())).perform(click())
    }

    fun clickChangePreference(func: PreferenceListBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_choose_preference)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        PreferenceListBottomSheetRobot().apply(func)
    }

    fun clickChangeCourier(func: CourierBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_shipping_price)).perform(scrollTo()).perform(click())
        CourierBottomSheetRobot().apply(func)
    }

    fun clickInsurance() {
        onView(withId(R.id.cb_insurance)).perform(scrollTo()).perform(click())
    }

    fun clickBboTicker() {
        onView(withId(R.id.ticker_shipping_promo)).perform(scrollTo())
        onView(withId(R.id.ticker_action)).perform(click())
    }

    fun pay() {
        onView(withId(R.id.btn_pay)).perform(scrollTo()).perform(click())
    }

    infix fun pay(func: OrderSummaryPageResultRobot.() -> Unit) {
        onView(withId(R.id.btn_pay)).perform(scrollTo()).perform(click())
        OrderSummaryPageResultRobot().apply(func)
    }

    fun assertProductCard(shopName: String,
                          shopLocation: String,
                          productName: String,
                          productPrice: String,
                          productSlashPrice: String?,
                          isFreeShipping: Boolean,
                          productQty: Int) {
        onView(withId(R.id.tv_shop_name)).perform(scrollTo()).check(matches(withText(shopName)))
        onView(withId(R.id.tv_shop_location)).check(matches(withText(shopLocation)))
        onView(withId(R.id.tv_product_name)).check(matches(withText(productName)))
        onView(withId(R.id.tv_product_price)).check(matches(withText(productPrice)))
        onView(withId(R.id.tv_product_slash_price)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (productSlashPrice == null) {
                assertEquals(View.GONE, view.visibility)
            } else {
                assertEquals(View.VISIBLE, view.visibility)
                assertEquals(productSlashPrice, (view as Typography).text)
            }
        }
        onView(withId(R.id.iv_free_shipping)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (isFreeShipping) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
        onView(withId(R.id.et_qty)).check(matches(withText(productQty.toString())))
    }

    fun assertProductQuantity(qty: Int) {
        onView(withId(R.id.et_qty)).perform(scrollTo()).check(matches(withText(qty.toString())))
    }

    fun assertProfileAddress(headerMessage: String,
                             addressName: String,
                             addressReceiver: String,
                             addressDetail: String,
                             isMainPreference: Boolean) {
        onView(withId(R.id.tv_card_header)).perform(scrollTo()).check(matches(withText(headerMessage)))
        onView(withId(R.id.tv_address_name)).check(matches(withText(addressName)))
        onView(withId(R.id.tv_address_receiver)).check(matches(withText(addressReceiver)))
        onView(withId(R.id.tv_address_detail)).check(matches(withText(addressDetail)))
        onView(withId(R.id.lbl_main_preference)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (isMainPreference) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
    }

    fun assertShipment(shippingName: String, shippingDuration: String, shippingPrice: String?, hasPromo: Boolean) {
        onView(withId(R.id.tv_shipping_name)).perform(scrollTo()).check(matches(withText(shippingName)))
        onView(withId(R.id.tv_shipping_duration)).check(matches(withText(shippingDuration)))
        if (shippingPrice != null) {
            onView(withId(R.id.tv_shipping_price)).perform(scrollTo()).check(matches(withText(shippingPrice)))
        }
        onView(withId(R.id.ticker_shipping_promo)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (hasPromo) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
    }

    fun assertInsurance(isChecked: Boolean) {
        if (isChecked) {
            onView(withId(R.id.cb_insurance)).perform(scrollTo()).check(matches(isChecked()))
        } else {
            onView(withId(R.id.cb_insurance)).perform(scrollTo()).check(matches(isNotChecked()))
        }
    }

    fun assertBboTicker(text: String) {
        onView(withId(R.id.ticker_shipping_promo)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.ticker_shipping_promo_description)).check(matches(withText(text)))
    }

    fun assertProfilePayment(paymentName: String) {
        onView(withId(R.id.tv_payment_name)).perform(scrollTo()).check(matches(withText(paymentName)))
    }

    fun assertPayment(total: String, buttonText: String) {
        onView(withId(R.id.btn_pay)).perform(scrollTo()).check(matches(withText(buttonText)))
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText(total)))
    }

    fun assertGlobalErrorVisible() {
        onView(withId(R.id.global_error)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.VISIBLE, view.visibility)
        }
    }
}

class OrderSummaryPageResultRobot {

    fun assertGoToPayment(redirectUrl: String, queryString: String, method: String) {
        val paymentPassData = Intents.getIntents().first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals(redirectUrl, paymentPassData.redirectUrl)
        assertEquals(queryString, paymentPassData.queryString)
        assertEquals(method, paymentPassData.method)
    }
}