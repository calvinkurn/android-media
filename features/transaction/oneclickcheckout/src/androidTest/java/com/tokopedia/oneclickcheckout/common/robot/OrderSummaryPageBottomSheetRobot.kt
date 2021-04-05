package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.action.scrollTo
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals

class OrderPriceSummaryBottomSheetRobot {

    fun assertSummary(productPrice: String = "",
                      productDiscount: String? = null,
                      shippingPrice: String = "",
                      shippingDiscount: String? = null,
                      isBbo: Boolean = false,
                      insurancePrice: String? = null,
                      paymentFee: String? = null,
                      totalPrice: String = "") {
        onView(withId(R.id.tv_total_product_price_value)).check(matches(withText(productPrice)))
        onView(withId(R.id.tv_total_product_discount_value)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (productDiscount == null) {
                assertEquals(View.GONE, view.visibility)
            } else {
                assertEquals(View.VISIBLE, view.visibility)
                assertEquals(productDiscount, (view as Typography).text)
            }
        }
        onView(withId(R.id.tv_total_shipping_price_value)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (isBbo) {
                assertEquals("Bebas Ongkir", (view as Typography).text)
            } else {
                assertEquals(shippingPrice, (view as Typography).text)
            }
        }
        onView(withId(R.id.tv_total_shipping_discount_value)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (shippingDiscount == null) {
                assertEquals(View.GONE, view.visibility)
            } else {
                assertEquals(View.VISIBLE, view.visibility)
                assertEquals(shippingDiscount, (view as Typography).text)
            }
        }
        onView(withId(R.id.tv_total_insurance_price_value)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (insurancePrice == null) {
                assertEquals(View.GONE, view.visibility)
            } else {
                assertEquals(View.VISIBLE, view.visibility)
                assertEquals(insurancePrice, (view as Typography).text)
            }
        }
        onView(withId(R.id.tv_total_payment_fee_price_value)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (paymentFee == null) {
                assertEquals(View.GONE, view.visibility)
            } else {
                assertEquals(View.VISIBLE, view.visibility)
                assertEquals(paymentFee, (view as Typography).text)
            }
        }
        onView(withId(R.id.tv_total_payment_price_value)).check(matches(withText(totalPrice)))
    }

    fun closeBottomSheet() {
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()

            override fun getDescription(): String = "Force click close bottom sheet"

            override fun perform(uiController: UiController?, view: View?) {
                view?.callOnClick()
                // Wait for bottom sheet to close
                Thread.sleep(1000)
            }
        })
    }
}

class InstallmentDetailBottomSheetRobot {

    fun chooseInstallment(term: Int) {
        val installmentName = if (term == 0) "Bayar Penuh" else "${term}x Cicilan 0%"
        onView(withText(installmentName)).perform(scrollTo()).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            val parent = view.parent as ViewGroup
            val radioButtonUnify = parent.findViewById<RadioButtonUnify>(R.id.rb_installment_detail)
            radioButtonUnify.performClick()
            // Wait for bottom sheet to close
            Thread.sleep(1000)
        }
    }
}

class OvoActivationBottomSheetRobot {

    fun performActivation(isSuccess: Boolean) {
        onView(withId(R.id.web_view)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            (view as? WebView)?.loadUrl("https://api-staging.tokopedia.com/cart/v2/receiver/?is_success=${if (isSuccess) 1 else 0}")
        }
        //block main thread for webview processing
        Thread.sleep(2000)
    }
}