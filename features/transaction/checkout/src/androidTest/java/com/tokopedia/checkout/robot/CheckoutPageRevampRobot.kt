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
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCrossSellViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutOrderViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutPromoViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import com.tokopedia.dialog.R as dialogR
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.promousage.R as promousageR
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

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

    private fun scrollRecyclerViewToCrossSell(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = (recyclerView.adapter?.itemCount?.minus(1)) ?: return -1

        var position = RecyclerView.NO_POSITION
        for (i in itemCount downTo 0) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is CheckoutCrossSellViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    fun scrollRecyclerViewToChoosePaymentButton(activityRule: IntentsTestRule<RevampShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_checkout)
        val itemCount = (recyclerView.adapter?.itemCount?.minus(1)) ?: return -1
        scrollRecyclerViewToPosition(activityRule, recyclerView, itemCount)
        return itemCount
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

    fun clickProductAddOn(activityRule: IntentsTestRule<RevampShipmentActivity>, productIndex: Int, addOnsName: String) {
        val position = scrollRecyclerViewToShipmentCartItem(activityRule, productIndex)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Click Add Ons Product"

                            override fun perform(uiController: UiController?, view: View) {
                                val layout = view.findViewById<LinearLayout>(R.id.ll_addon_product_items)
                                assertEquals(View.VISIBLE, layout.visibility)
                                for (v in layout.children) {
                                    if (v.findViewById<Typography>(purchase_platformcommonR.id.tv_checkout_add_ons_item_name).text.toString() == addOnsName) {
                                        val checkboxUnify = v.findViewById<CheckboxUnify>(purchase_platformcommonR.id.cb_checkout_add_ons)
                                        checkboxUnify.isChecked = !checkboxUnify.isChecked
                                        return
                                    }
                                }
                                throw AssertionError("product add on with name $addOnsName not found in $productIndex")
                            }
                        }
                    )
                )
        }
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

    fun selectBebasOngkirDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun selectDurationOptionWithText(text: String) {
        onView(ViewMatchers.withText(text)).perform(ViewActions.click())
    }

    fun clickPromoButton(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToPromoButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(promousageR.id.active_promo_checkout_view)
                    )
                )
        }
    }

    fun clickEgold(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToCrossSell(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Click Egold UI"

                            override fun perform(uiController: UiController?, view: View) {
                                if (view.findViewById<View>(R.id.item_checkout_cross_sell_item).visibility == View.VISIBLE) {
                                    val checkbox =
                                        view.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item)
                                    checkbox.isChecked = !checkbox.isChecked
                                } else {
                                    val rv = view.findViewById<RecyclerView>(R.id.rv_checkout_cross_sell)
                                    val itemCount = rv.adapter?.itemCount ?: 0

                                    for (i in 0 until itemCount) {
                                        rv.scrollToPosition(i)
                                        val viewHolder = rv.findViewHolderForAdapterPosition(i)
                                        val itemView = viewHolder?.itemView
                                        if (itemView?.findViewById<Typography>(R.id.tv_checkout_cross_sell_item)?.text.toString().contains("emas", ignoreCase = true)) {
                                            val checkbox = itemView?.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item) ?: return
                                            checkbox.isChecked = !checkbox.isChecked
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    )
                )
        }
    }

    fun clickDonasi(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToCrossSell(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Click Donasi UI"

                            override fun perform(uiController: UiController?, view: View) {
                                if (view.findViewById<View>(R.id.item_checkout_cross_sell_item).visibility == View.VISIBLE) {
                                    val checkbox =
                                        view.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item)
                                    checkbox.isChecked = !checkbox.isChecked
                                } else {
                                    val rv = view.findViewById<RecyclerView>(R.id.rv_checkout_cross_sell)
                                    val itemCount = rv.adapter?.itemCount ?: 0

                                    for (i in 0 until itemCount) {
                                        rv.scrollToPosition(i)
                                        val viewHolder = rv.findViewHolderForAdapterPosition(i)
                                        val itemView = viewHolder?.itemView
                                        if (itemView?.findViewById<Typography>(R.id.tv_checkout_cross_sell_item)?.text.toString().contains("donasi", ignoreCase = true)) {
                                            val checkbox = itemView?.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item) ?: return
                                            checkbox.isChecked = !checkbox.isChecked
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    )
                )
        }
    }

    fun expandShoppingSummary(activityRule: IntentsTestRule<RevampShipmentActivity>) {
        val position = scrollRecyclerViewToShoppingSummary(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Expand Shopping Summary"

                            override fun perform(uiController: UiController?, view: View) {
                                if (view.findViewById<View>(R.id.tv_checkout_cost_others_title).visibility == View.VISIBLE && view.findViewById<View>(R.id.ll_checkout_cost_others_expanded).visibility == View.GONE) {
                                    view.findViewById<View>(R.id.ic_checkout_cost_others_toggle).performClick()
                                }
                            }
                        }
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

    fun clickPrimaryButtonDialogUnify() {
        onView(ViewMatchers.withId(dialogR.id.dialog_btn_primary)).perform(ViewActions.click())
    }

    fun clickSecondaryButtonDialogUnify() {
        onView(ViewMatchers.withId(dialogR.id.dialog_btn_secondary_long)).perform(ViewActions.click())
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

    fun assertAddOnsProduct(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        productIndex: Int,
        addOnsName: String,
        addOnsPrice: String,
        isChecked: Boolean
    ) {
        val position = scrollRecyclerViewToShipmentCartItem(activityRule, productIndex)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String = "Assert Add Ons Product UI"

                            override fun perform(uiController: UiController?, view: View) {
                                val layout = view.findViewById<LinearLayout>(R.id.ll_addon_product_items)
                                assertEquals(View.VISIBLE, layout.visibility)
                                for (v in layout.children) {
                                    if (v.findViewById<Typography>(purchase_platformcommonR.id.tv_checkout_add_ons_item_name).text.toString() == addOnsName) {
                                        assertEquals(addOnsPrice, v.findViewById<Typography>(purchase_platformcommonR.id.tv_checkout_add_ons_item_price).text.toString())
                                        assertEquals(isChecked, v.findViewById<CheckboxUnify>(purchase_platformcommonR.id.cb_checkout_add_ons).isChecked)
                                        return
                                    }
                                }
                                throw AssertionError("product add on with name $addOnsName not found in $productIndex")
                            }
                        }
                    )
                )
        }
    }

    fun assertBmsmProduct(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        productIndex: Int,
        bmsmTitle: String
    ) {
        val position = scrollRecyclerViewToShipmentCartItem(activityRule, productIndex)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String = "Assert Bmsm Product UI"

                            override fun perform(uiController: UiController?, view: View) {
                                val layout = view.findViewById<Typography>(R.id.tv_checkout_bmgm_title)
                                assertEquals(View.VISIBLE, layout.visibility)
                                assertEquals(bmsmTitle, layout.text.toString())
                            }
                        }
                    )
                )
        }
    }

    fun assertEgold(activityRule: IntentsTestRule<RevampShipmentActivity>, text: String, isChecked: Boolean) {
        val position = scrollRecyclerViewToCrossSell(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String =
                                "Assert Egold UI"

                            override fun perform(uiController: UiController?, view: View) {
                                if (view.findViewById<View>(R.id.item_checkout_cross_sell_item).visibility == View.VISIBLE) {
                                    assertEquals(text, view.findViewById<Typography>(R.id.tv_checkout_cross_sell_item).text.toString())
                                    assertEquals(isChecked, view.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item).isChecked)
                                } else {
                                    val rv = view.findViewById<RecyclerView>(R.id.rv_checkout_cross_sell)
                                    val itemCount = rv.adapter?.itemCount ?: 0

                                    for (i in 0 until itemCount) {
                                        rv.scrollToPosition(i)
                                        val viewHolder = rv.findViewHolderForAdapterPosition(i)
                                        val itemView = viewHolder?.itemView
                                        if (itemView?.findViewById<Typography>(R.id.tv_checkout_cross_sell_item)?.text.toString().contains("emas", ignoreCase = true)) {
                                            assertEquals(text, itemView?.findViewById<Typography>(R.id.tv_checkout_cross_sell_item)?.text.toString())
                                            assertEquals(isChecked, itemView?.findViewById<CheckboxUnify>(R.id.cb_checkout_cross_sell_item)?.isChecked)
                                            return
                                        }
                                    }

                                    // not found
                                    throw AssertionError("not found matching egold")
                                }
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
        shippingTotalPrice: String?,
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

                                if (shippingTotalPrice != null) {
                                    assertEquals(
                                        View.VISIBLE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_value).visibility
                                    )
                                    assertEquals(
                                        shippingTotalPrice,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_value).text.toString()
                                    )
                                } else {
                                    assertEquals(
                                        View.GONE,
                                        view.findViewById<Typography>(R.id.tv_checkout_cost_shipping_value).visibility
                                    )
                                }
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

    fun assertEgoldShoppingSummary(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        hasEgold: Boolean,
        egoldPrice: String
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
                                "Assert Egold Shopping Summary UI"

                            override fun perform(uiController: UiController?, view: View) {
                                val llOthers =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others)
                                if (llOthers.visibility == View.VISIBLE && llOthers.childCount > 0) {
                                    for (child in llOthers.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains("emas", ignoreCase = true)) {
                                            assertEquals(egoldPrice, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasEgold)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasEgold)
                                    return
                                }

                                val llOtherExpanded =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others_expanded)
                                if (llOtherExpanded.visibility == View.VISIBLE) {
                                    for (child in llOtherExpanded.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains("emas", ignoreCase = true)) {
                                            assertEquals(egoldPrice, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasEgold)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasEgold)
                                    return
                                }

                                assertEquals(false, hasEgold)
                            }
                        }
                    )
                )
        }
    }

    fun assertDonasiShoppingSummary(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        hasDonasi: Boolean,
        donasiPrice: String
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
                                "Assert Donasi Shopping Summary UI"

                            override fun perform(uiController: UiController?, view: View) {
                                val llOthers =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others)
                                if (llOthers.visibility == View.VISIBLE && llOthers.childCount > 0) {
                                    for (child in llOthers.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains("donasi", ignoreCase = true)) {
                                            assertEquals(donasiPrice, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasDonasi)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasDonasi)
                                    return
                                }

                                val llOtherExpanded =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others_expanded)
                                if (llOtherExpanded.visibility == View.VISIBLE) {
                                    for (child in llOtherExpanded.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains("donasi", ignoreCase = true)) {
                                            assertEquals(donasiPrice, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasDonasi)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasDonasi)
                                    return
                                }

                                assertEquals(false, hasDonasi)
                            }
                        }
                    )
                )
        }
    }

    fun assertAddOnsShoppingSummary(
        activityRule: IntentsTestRule<RevampShipmentActivity>,
        hasAddOns: Boolean,
        addOnsName: String,
        addOnsPrice: String?
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
                                "Assert Add Ons Shopping Summary UI"

                            override fun perform(uiController: UiController?, view: View) {
                                val llOthers =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others)
                                if (llOthers.visibility == View.VISIBLE && llOthers.childCount > 0) {
                                    for (child in llOthers.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains(addOnsName, ignoreCase = true)) {
                                            assertEquals(addOnsPrice!!, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasAddOns)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasAddOns)
                                    return
                                }

                                val llOtherExpanded =
                                    view.findViewById<LinearLayout>(R.id.ll_checkout_cost_others_expanded)
                                if (llOtherExpanded.visibility == View.VISIBLE) {
                                    for (child in llOtherExpanded.children) {
                                        if (child.findViewById<Typography>(R.id.tv_checkout_cost_item_title).text.toString().contains(addOnsName, ignoreCase = true)) {
                                            assertEquals(addOnsPrice, child.findViewById<Typography>(R.id.tv_checkout_cost_item_value).text.toString())
                                            assertEquals(true, hasAddOns)
                                            return
                                        }
                                    }
                                    assertEquals(false, hasAddOns)
                                    return
                                }

                                assertEquals(false, hasAddOns)
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
