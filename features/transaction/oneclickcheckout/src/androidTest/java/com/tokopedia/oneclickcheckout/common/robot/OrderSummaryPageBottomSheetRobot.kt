package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.action.scrollTo
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals


class OrderPriceSummaryBottomSheetRobot {

    fun assertSummary(productPrice: String = "",
                      productDiscount: String? = null,
                      shippingPrice: String = "",
                      shippingDiscount: String? = null,
                      isBbo: Boolean = false,
                      insurancePrice: String? = null,
                      totalPrice: String = "",
                      isInstallment: Boolean = false,
                      paymentFeeDetails: List<OrderPaymentFee> = emptyList()) {
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
                assertEquals("Rp0", (view as Typography).text)
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

        if (paymentFeeDetails.isEmpty() && !isInstallment) {
            onView(withId(R.id.divider_transaction_fee)).check(matches(not(isDisplayed())))
            onView(withId(R.id.tv_transaction_fee)).check(matches(not(isDisplayed())))
        } else {
            onView(withId(R.id.divider_transaction_fee)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_transaction_fee)).check(matches(isDisplayed()))
            // Wait for bottom sheet window to be focused
            Thread.sleep(1000)
            onView(withId(R.id.ll_payment_fee)).check { view, _ ->
                val llPaymentFee = (view as ViewGroup)
                for (i in 0 until llPaymentFee.childCount) {
                    val clPaymentFeeView = (llPaymentFee.getChildAt(i)) as ViewGroup
                    val orderPaymentFee = paymentFeeDetails[i]
                    assertEquals(orderPaymentFee.title, clPaymentFeeView.findViewById<Typography>(R.id.tv_payment_fee_price_label).text.toString())
                    if (orderPaymentFee.showTooltip) {
                        assertEquals(View.VISIBLE, clPaymentFeeView.findViewById<IconUnify>(R.id.img_payment_fee_info).visibility)
                    } else {
                        assertEquals(View.GONE, clPaymentFeeView.findViewById<IconUnify>(R.id.img_payment_fee_info).visibility)
                    }
                    if (orderPaymentFee.showSlashed) {
                        assertEquals(View.VISIBLE, clPaymentFeeView.findViewById<Typography>(R.id.tv_payment_fee_slash_price_value).visibility)
                        assertEquals(CurrencyFormatUtil.convertPriceValueToIdrFormat(orderPaymentFee.slashedFee, false).removeDecimalSuffix(), clPaymentFeeView.findViewById<Typography>(R.id.tv_payment_fee_slash_price_value).text.toString())
                    } else {
                        assertEquals(View.INVISIBLE, clPaymentFeeView.findViewById<Typography>(R.id.tv_payment_fee_slash_price_value).visibility)
                    }
                    assertEquals(CurrencyFormatUtil.convertPriceValueToIdrFormat(orderPaymentFee.fee, false).removeDecimalSuffix(), clPaymentFeeView.findViewById<Typography>(R.id.tv_payment_fee_price_value).text.toString())
                }
            }
        }

        onView(withId(R.id.tv_total_payment_price_value)).check(matches(withText(totalPrice)))
    }

    fun assertInstallmentSummary(installmentFee: String,
                                 installmentTerm: String,
                                 installmentPerPeriod: String,
                                 installmentFirstDate: String,
                                 installmentLastDate: String) {
        onView(withId(R.id.tv_total_installment_fee_price_value)).check(matches(withText(installmentFee)))
        onView(withId(R.id.tv_total_installment_term_value)).check(matches(withText(installmentTerm)))
        onView(withId(R.id.tv_total_installment_per_period_value)).check(matches(withText(installmentPerPeriod)))
        onView(withId(R.id.tv_total_installment_first_date_value)).check(matches(withText(installmentFirstDate)))
        onView(withId(R.id.tv_total_installment_last_date_value)).check(matches(withText(installmentLastDate)))
    }

    fun clickPaymentFeeInfo(index: Int, func: OrderPriceSummaryBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.ll_payment_fee)).check { view, _ ->
            val llPaymentFee = (view as ViewGroup)
            if (llPaymentFee.childCount > 0) {
                val clPaymentFeeView = (llPaymentFee.getChildAt(index)) as ViewGroup
                clPaymentFeeView.findViewById<IconUnify>(R.id.img_payment_fee_info).performClick()
            }
        }
        // Wait for bottom sheet to fully appear
        Thread.sleep(1000)
        OrderPriceSummaryBottomSheetRobot().apply(func)
    }

    fun assertPaymentFeeBottomSheetInfo(tooltipTitle: String, tooltipInfo: String) {
        onView(withId(R.id.bottom_sheet_title)).check(matches(withText(tooltipTitle)))
        onView(withId(R.id.tv_info)).check(matches(withText(tooltipInfo)))
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

class GoCicilInstallmentDetailBottomSheetRobot {

    fun chooseInstallment(term: Int) {
        onView(withText(object : BaseMatcher<String>() {
            override fun describeTo(description: Description?) {
                description?.appendText("match Cicil $term")
            }

            override fun matches(item: Any?): Boolean {
                if (item is String) {
                    return item.startsWith("Cicil $term")
                }
                return false
            }
        })).perform(scrollTo()).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            val parent = view.parent as ViewGroup
            val radioButtonUnify = parent.findViewById<RadioButtonUnify>(R.id.rb_installment_detail)
            radioButtonUnify.performClick()
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