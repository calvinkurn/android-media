package com.tokopedia.checkout.robot

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCostViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutOrderViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutPromoViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.promocheckout.common.R as promocheckoutcommonR

fun checkoutPageRevamp(func: CheckoutPageRevampRobot.() -> Unit) =
    CheckoutPageRevampRobot().apply(func)

class CheckoutPageRevampRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun scrollRecyclerViewToFirstOrder(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is CheckoutOrderViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun scrollRecyclerViewToShipmentCartItem(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        productIndex: Int
    ): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        var currentIndex = 0
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is CheckoutProductViewHolder -> {
                    position = i
                    if (currentIndex == productIndex) break else currentIndex += 1
                }
            }
        }

        return position
    }

    private fun scrollRecyclerViewToShoppingSummary(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = (recyclerView.adapter?.itemCount?.minus(1)) ?: return -1

        var position = RecyclerView.NO_POSITION
        for (i in itemCount downTo 0) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is CheckoutCostViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    fun scrollRecyclerViewToChoosePaymentButton(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
//        Log.i("qwertyuiop", "scroll")
        val itemCount = (recyclerView.adapter?.itemCount?.minus(1)) ?: return -1

        var position = itemCount
        scrollRecyclerViewToPosition(activityRule, recyclerView, itemCount)
//        for (i in itemCount downTo 0) {
//            Log.i("qwertyuiop", "scroll $i")
//            val findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(i)
//            Log.i("qwertyuiop", "scroll $findViewHolderForAdapterPosition")
//            when (findViewHolderForAdapterPosition) {
//                is CheckoutButtonPaymentViewHolder -> {
//                    Log.i("qwertyuiop", "found $i")
//                    position = i
//                    break
//                }
//            }
//        }

        return position
    }

    private fun scrollRecyclerViewToPromoButton(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = (recyclerView.adapter?.itemCount?.minus(1)) ?: return -1

        var position = RecyclerView.NO_POSITION
        for (i in itemCount downTo 0) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is CheckoutPromoViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun scrollRecyclerViewToPosition(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        recyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            ViewActions.click().perform(uiController, view.findViewById(viewId))
    }

    fun clickChooseDuration(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(logisticcartR.id.layout_state_no_selected_shipping)
                    )
                )
        }
    }

    fun click2hrOption(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Click 2hr option"

                            override fun perform(uiController: UiController?, view: View) {
                                val linearLayout =
                                    view.findViewById<LinearLayout>(logisticcartR.id.shipment_time_option_view)

                                val now = linearLayout.children.last()
                                now.findViewById<RadioButtonUnify>(logisticcartR.id.rb_shipment).isChecked = true
                            }
                        }
                    )
                )
        }
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun clickPromoButton(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToPromoButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(promocheckoutcommonR.id.active_promo_checkout_view)
                    )
                )
        }
    }

    fun clickChoosePaymentButton(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToChoosePaymentButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(R.id.btn_checkout_pay)
                    )
                )
        }
    }

    infix fun validateAnalytics(func: ResultRevampRobot.() -> Unit): ResultRevampRobot {
        return ResultRevampRobot().apply(func)
    }

    /**
     * Assert Single Shipment State UI
     *
     * @param title Shipment title with postfix " (", ex: "Pengiriman 2 jam (" or "Pengiriman 2 jam (Rp0)
     * @param originalPrice if discounted price is not equal, ex: "Rp20.000"
     * @param discountedPrice additional price with prefix space and postfix ")" if available, ex: " Rp5.000)"
     * @param eta eta message
     * @param message additional promo message if available
     */
    fun assertHasSingleShipmentSelected(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        title: String,
        originalPrice: String? = null,
        discountedPrice: String? = null,
        eta: String,
        message: String? = null
    ) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Single Shipment Selected UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<View>(logisticcartR.id.layout_state_has_selected_single_shipping).visibility
                                )
                                assertEquals(
                                    title,
                                    view.findViewById<Typography>(logisticcartR.id.label_selected_single_shipping_title).text.toString()
                                )
                                if (originalPrice != null) {
                                    assertTrue(
                                        (view.findViewById<Typography>(logisticcartR.id.label_selected_single_shipping_title).text).contains(
                                            originalPrice
                                        )
                                    )
                                }
                                if (discountedPrice != null) {
                                    assertTrue(
                                        (view.findViewById<Typography>(logisticcartR.id.label_selected_single_shipping_title).text).contains(
                                            discountedPrice
                                        )
                                    )
                                }
//                                assertEquals(eta, view.findViewById<Typography>(logisticcartR.id.label_single_shipping_eta).text)
                                if (message != null) {
                                    assertEquals(
                                        message,
                                        view.findViewById<Typography>(logisticcartR.id.label_single_shipping_message).text.toString()
                                    )
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(logisticcartR.id.label_single_shipping_message).visibility
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<Typography>(logisticcartR.id.label_single_shipping_message).visibility
                                    )
                                }
                            }
                        }
                    )
                )
        }
    }

    fun assertHasSellySelected(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        titleSelly: String,
        originalPriceSelly: String? = null,
        discountedPriceSelly: String? = null,
        etaSelly: String,
        title2hr: String,
        originalPrice2hr: String? = null,
        discountedPrice2hr: String? = null
    ) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Selly Shipment Selected UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<View>(logisticcartR.id.shipping_now_widget).visibility
                                )
                                val linearLayout =
                                    view.findViewById<LinearLayout>(logisticcartR.id.shipment_time_option_view)
                                val selly = linearLayout.children.first()
                                assertTrue(
                                    (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                        titleSelly
                                    )
                                )
                                if (originalPriceSelly != null) {
                                    assertTrue(
                                        (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            originalPriceSelly
                                        )
                                    )
                                }
                                if (discountedPriceSelly != null) {
                                    assertTrue(
                                        (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            discountedPriceSelly
                                        )
                                    )
                                }
                                assertEquals(
                                    etaSelly,
                                    selly.findViewById<Typography>(logisticcartR.id.tv_description_shipment).text.toString()
                                )
                                assertEquals(
                                    true,
                                    selly.findViewById<RadioButtonUnify>(logisticcartR.id.rb_shipment).isChecked
                                )

                                val now = linearLayout.children.last()
                                assertTrue(
                                    (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                        title2hr
                                    )
                                )
                                if (originalPrice2hr != null) {
                                    assertTrue(
                                        (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            originalPrice2hr
                                        )
                                    )
                                }
                                if (discountedPrice2hr != null) {
                                    assertTrue(
                                        (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            discountedPrice2hr
                                        )
                                    )
                                }
                                assertEquals(
                                    false,
                                    now.findViewById<RadioButtonUnify>(logisticcartR.id.rb_shipment).isChecked
                                )
                            }
                        }
                    )
                )
        }
    }

    fun assertHas2hrSelected(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        titleSelly: String,
        originalPriceSelly: String? = null,
        discountedPriceSelly: String? = null,
        etaSelly: String,
        title2hr: String,
        originalPrice2hr: String? = null,
        discountedPrice2hr: String? = null
    ) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Selly Shipment Selected UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<View>(logisticcartR.id.shipping_now_widget).visibility
                                )
                                val linearLayout =
                                    view.findViewById<LinearLayout>(logisticcartR.id.shipment_time_option_view)
                                val selly = linearLayout.children.first()
                                assertTrue(
                                    (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                        titleSelly
                                    )
                                )
                                if (originalPriceSelly != null) {
                                    assertTrue(
                                        (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            originalPriceSelly
                                        )
                                    )
                                }
                                if (discountedPriceSelly != null) {
                                    assertTrue(
                                        (selly.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            discountedPriceSelly
                                        )
                                    )
                                }
                                assertEquals(
                                    etaSelly,
                                    selly.findViewById<Typography>(logisticcartR.id.tv_description_shipment).text.toString()
                                )
                                assertEquals(
                                    false,
                                    selly.findViewById<RadioButtonUnify>(logisticcartR.id.rb_shipment).isChecked
                                )

                                val now = linearLayout.children.last()
                                assertTrue(
                                    (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                        title2hr
                                    )
                                )
                                if (originalPrice2hr != null) {
                                    assertTrue(
                                        (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            originalPrice2hr
                                        )
                                    )
                                }
                                if (discountedPrice2hr != null) {
                                    assertTrue(
                                        (now.findViewById<Typography>(logisticcartR.id.tv_title_shipment).text).contains(
                                            discountedPrice2hr
                                        )
                                    )
                                }
                                assertEquals(
                                    true,
                                    now.findViewById<RadioButtonUnify>(logisticcartR.id.rb_shipment).isChecked
                                )
                            }
                        }
                    )
                )
        }
    }

    fun assertNewUiGroupType(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        productIndex: Int
    ) {
        val position = scrollRecyclerViewToShipmentCartItem(activityRule, productIndex)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String = "Assert New UI Group Type"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<Typography>(R.id.tv_checkout_shop_name).visibility
                                )
                                assertEquals(
                                    true,
                                    view.findViewById<Typography>(R.id.tv_checkout_shop_name).text.isNotBlank()
                                )
                            }
                        }
                    )
                )
        }
    }

    fun assertShoppingSummary(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        itemTotalPrice: String,
        itemOriginalPrice: String?,
        shippingTotalPrice: String,
        shippingOriginalPrice: String?,
        totalPrice: String
    ) {
        val position = scrollRecyclerViewToShoppingSummary(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Shopping Summary UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<View>(R.id.tv_checkout_cost_header).visibility
                                )

                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_item_price_value).visibility
                                )
                                assertEquals(
                                    itemTotalPrice,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_item_price_value).text.toString()
                                )
                                if (itemOriginalPrice != null) {
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_item_price_slashed_value).visibility
                                    )
                                    assertEquals(
                                        itemOriginalPrice,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_item_price_slashed_value).text.toString()
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_item_price_slashed_value).visibility
                                    )
                                }

                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_value).visibility
                                )
                                assertEquals(
                                    shippingTotalPrice,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_value).text.toString()
                                )
                                if (shippingOriginalPrice != null) {
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_slashed_value).visibility
                                    )
                                    assertEquals(
                                        shippingOriginalPrice,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_slashed_value).text.toString()
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_slashed_value).visibility
                                    )
                                }

                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_total_value).visibility
                                )
                                assertEquals(
                                    totalPrice,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_total_value).text.toString()
                                )
                            }
                        }
                    )
                )
        }
    }

    fun assertPlatformFee(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        fee: String,
        originalFee: String?
    ) {
        val position = scrollRecyclerViewToShoppingSummary(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Platform Fee UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<View>(R.id.tv_checkout_cost_header).visibility
                                )

                                assertEquals(
                                    View.VISIBLE,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_platform_fee_value).visibility
                                )
                                assertEquals(
                                    fee,
                                    view.findViewById<Typography>(R.id.tv_checkout_cost_platform_fee_value).text.toString()
                                )
                                if (originalFee != null) {
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_platform_fee_slashed_value).visibility
                                    )
                                    assertEquals(
                                        originalFee,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_platform_fee_slashed_value).text.toString()
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_platform_fee_slashed_value).visibility
                                    )
                                }
                            }
                        }
                    )
                )
        }
    }
}

class ResultRevampRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

    fun assertGoToPayment() {
        val paymentPassData = Intents.getIntents().last()
            .getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)
        assertNotNull(paymentPassData)
    }
}
