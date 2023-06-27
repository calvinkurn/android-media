package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.action.swipeUpTop
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.card.OrderInsuranceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPreferenceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPromoCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderShopCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTotalPaymentCard
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals

fun orderSummaryPage(func: OrderSummaryPageRobot.() -> Unit) = OrderSummaryPageRobot().apply(func)

fun waitForBottomSheet() {
    Thread.sleep(100)
}

class OrderSummaryPageRobot {

    fun closeBottomSheet() {
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    fun clickAddProductQuantity(index: Int = 0, times: Int = 1) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnItemAtPosition<OrderProductCard>(
                index + 3,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click add product quantity"

                    override fun perform(uiController: UiController?, view: View) {
                        val addButton = view.findViewById<View>(com.tokopedia.unifycomponents.R.id.quantity_editor_add)
                        for (i in 0 until times) {
                            addButton.performClick()
                        }
                    }
                }
            )
        )
        Thread.sleep(OrderSummaryPageViewModel.DEBOUNCE_TIME)
    }

    fun clickMinusProductQuantity(index: Int = 0, times: Int = 1) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnItemAtPosition<OrderProductCard>(
                index + 3,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click minus product quantity"

                    override fun perform(uiController: UiController?, view: View) {
                        val minusButton = view.findViewById<View>(com.tokopedia.unifycomponents.R.id.quantity_editor_substract)
                        for (i in 0 until times) {
                            minusButton.performClick()
                        }
                        Thread.sleep(OrderSummaryPageViewModel.DEBOUNCE_TIME)
                    }
                }
            )
        )
    }

    fun clickChangeAddressRevamp(func: (AddressBottomSheetRobot.() -> Unit)? = null) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change address"

                    override fun perform(uiController: UiController?, view: View) {
                        click().perform(uiController, view.findViewById(R.id.btn_change_address))
                    }
                }
            )
        )
        // Wait for bottomsheet to fully open
        waitForBottomSheet()
        if (func != null) {
            AddressBottomSheetRobot().apply(func)
        }
    }

    fun clickChangeDurationRevamp(func: DurationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change duration"

                    override fun perform(uiController: UiController?, view: View) {
                        click().perform(uiController, view.findViewById(com.tokopedia.logisticcart.R.id.btn_change_duration))
                    }
                }
            )
        )
        // Wait for bottomsheet to fully open
        waitForBottomSheet()
        DurationBottomSheetRobot().apply(func)
    }

    fun clickChangePaymentRevamp() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change payment"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.btn_change_payment).performClick()
                    }
                }
            )
        )
    }

    fun clickChangeCourierRevamp(func: CourierBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change courier"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(com.tokopedia.logisticcart.R.id.btn_change_courier).performClick()
                    }
                }
            )
        )
        // Wait for bottomsheet to fully open
        waitForBottomSheet()
        CourierBottomSheetRobot().apply(func)
    }

    fun clickShipmentErrorAction(func: DurationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click shipping error action"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_error_message).performClick()
                    }
                }
            )
        )
        // Wait for bottomsheet to fully open
        waitForBottomSheet()
        DurationBottomSheetRobot().apply(func)
    }

    fun clickShipmentReloadAction() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click shipping reload action"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(com.tokopedia.logisticcart.R.id.btn_reload_shipping).performClick()
                    }
                }
            )
        )
    }

    fun clickInsurance() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderInsuranceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click insurance checkbox"

                    override fun perform(uiController: UiController?, view: View) {
                        click().perform(uiController, view.findViewById(R.id.cb_insurance))
                    }
                }
            )
        )
    }

    fun clickApplyShipmentPromoRevamp() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click apply shipment promo from ticker"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(com.tokopedia.logisticcart.R.id.ticker_action).performClick()
                    }
                }
            )
        )
    }

    fun clickOvoActivationButtonRevamp(func: (OvoActivationBottomSheetRobot.() -> Unit)? = null) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click payment error action"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.tv_payment_ovo_error_action).performClick()
                    }
                }
            )
        )
        if (func != null) {
            OvoActivationBottomSheetRobot().apply(func)
        }
    }

    fun clickOvoTopUpButtonRevamp() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click payment error message"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.tv_payment_error_message).performClick()
                    }
                }
            )
        )
    }

    fun clickChangeInstallmentRevamp(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        // Wait for espresso
        waitForBottomSheet()
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change installment"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.tv_installment_detail).performClick()
                    }
                }
            )
        )
        waitForBottomSheet()
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickChangeGoCicilInstallment(func: GoCicilInstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change installment"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.tv_installment_detail_wrap).performClick()
                    }
                }
            )
        )
        waitForBottomSheet()
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        GoCicilInstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickInstallmentErrorActionRevamp(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click change installment error"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.tv_installment_error_action).performClick()
                    }
                }
            )
        )
        waitForBottomSheet()
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickButtonPromo() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPromoCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click button promo"

                    override fun perform(uiController: UiController?, view: View) {
                        click().perform(uiController, view.findViewById(R.id.btn_promo_checkout))
                    }
                }
            )
        )
    }

    fun clickButtonOrderDetail(func: OrderPriceSummaryBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click button order detail"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.btn_order_detail).performClick()
                    }
                }
            )
        )
        // Wait for bottom sheet to fully appear
        waitForBottomSheet()
        OrderPriceSummaryBottomSheetRobot().apply(func)
    }

    fun clickButtonContinueWithRedPromo() {
        // Wait for bottom sheet to fully appear
        waitForBottomSheet()
        onView(withId(com.tokopedia.purchase_platform.common.R.id.btn_continue)).perform(click())
    }

    fun clickDialogPrimaryButton() {
        // Wait for dialog to fully appear
        waitForBottomSheet()
        onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary)).perform(click())
    }

    fun pay() {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click pay"

                    override fun perform(uiController: UiController?, view: View) {
                        click().perform(uiController, view.findViewById(R.id.btn_pay))
                    }
                }
            )
        )
    }

    infix fun pay(func: OrderSummaryPageResultRobot.() -> Unit) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "click pay"

                    override fun perform(uiController: UiController?, view: View) {
                        view.findViewById<View>(R.id.btn_pay).performClick()
                    }
                }
            )
        )
        waitForBottomSheet()
        OrderSummaryPageResultRobot().apply(func)
    }

    fun clickAddNewAddress() {
        onView(withId(R.id.btn_occ_add_new_address)).perform(click())
    }

    fun assertShopCard(
        shopName: String,
        hasShopBadge: Boolean,
        shopLocation: String,
        hasShopLocationImg: Boolean,
        isFreeShipping: Boolean,
        preOrderText: String,
        alertMessage: String
    ) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderShopCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shop card"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(shopName, view.findViewById<Typography>(R.id.tv_shop_name).text.toString())
                        assertEquals(shopLocation, view.findViewById<Typography>(R.id.tv_shop_location).text.toString())
                        assertEquals(if (hasShopLocationImg) View.VISIBLE else View.GONE, view.findViewById<View>(R.id.iu_image_fulfill).visibility)
                        assertEquals(if (hasShopBadge) View.VISIBLE else View.GONE, view.findViewById<View>(R.id.iv_shop_badge).visibility)
                        assertEquals(if (isFreeShipping) View.VISIBLE else View.GONE, view.findViewById<View>(R.id.iu_free_shipping).visibility)
                        assertEquals(if (isFreeShipping) View.VISIBLE else View.GONE, view.findViewById<View>(R.id.separator_free_shipping).visibility)
                        if (preOrderText.isNotEmpty()) {
                            assertEquals(preOrderText, view.findViewById<Label>(R.id.lbl_pre_order).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.lbl_pre_order).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.separator_pre_order).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.lbl_pre_order).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.separator_pre_order).visibility)
                        }
                        if (alertMessage.isNotEmpty()) {
                            assertEquals(alertMessage, view.findViewById<Label>(R.id.lbl_alert_message).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.lbl_alert_message).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.separator_alert_message).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.lbl_alert_message).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.separator_alert_message).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertShopBadge(hasShopBadge: Boolean = true, shopTypeName: String) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderShopCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shop badge"

                    override fun perform(uiController: UiController?, view: View) {
                        if (hasShopBadge) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.iv_shop_badge).visibility)
                            assertEquals("image shop badge $shopTypeName", view.findViewById<View>(R.id.iv_shop_badge).contentDescription)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.iv_shop_badge).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertShopTicker(
        tickerMessage: String? = null,
        isCustom: Boolean = false
    ) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderShopCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shop badge"

                    override fun perform(uiController: UiController?, view: View) {
                        if (tickerMessage != null && isCustom) {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.ticker_order_shop).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.occ_custom_ticker_error).visibility)
                            assertEquals(tickerMessage, view.findViewById<Typography>(R.id.occ_custom_ticker_description).text.toString())
                        } else if (tickerMessage != null) {
                            assertEquals(View.VISIBLE, view.findViewById<Ticker>(R.id.ticker_order_shop).visibility)
                            assertEquals(tickerMessage, view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description).text.toString())
                            assertEquals(View.GONE, view.findViewById<View>(R.id.occ_custom_ticker_error).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.ticker_order_shop).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.occ_custom_ticker_error).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertProductCard(
        index: Int = 0,
        productName: String,
        productPrice: String,
        productSlashPrice: String?,
        productSlashPriceLabel: String?,
        productVariant: String?,
        productWarningMessage: String?,
        productAlertMessage: String?,
        productInfo: List<String>?,
        productQty: Int,
        productNotes: String?
    ) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnItemAtPosition<OrderProductCard>(
                index + 3,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert product $index card"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(View.GONE, view.findViewById<View>(R.id.ticker_order_product).visibility)
                        assertEquals(productName, view.findViewById<Typography>(R.id.tv_product_name).text.toString())
                        assertEquals(productPrice, view.findViewById<Typography>(R.id.tv_product_price).text.toString())
                        if (productSlashPrice == null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_slash_price).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_slash_price).visibility)
                            assertEquals(productSlashPrice, view.findViewById<Typography>(R.id.tv_product_slash_price).text.toString())
                        }
                        if (productSlashPriceLabel == null) {
                            assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_product_slash_price_percentage).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.lbl_product_slash_price_percentage).visibility)
                            assertEquals(productSlashPriceLabel, view.findViewById<Label>(R.id.lbl_product_slash_price_percentage).text.toString())
                        }
                        if (productVariant == null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_variant).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_variant).visibility)
                            assertEquals(productVariant, view.findViewById<Typography>(R.id.tv_product_variant).text.toString())
                        }
                        if (productWarningMessage == null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_qty_left).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_qty_left).visibility)
                            assertEquals(productWarningMessage, view.findViewById<Typography>(R.id.tv_qty_left).text.toString())
                        }
                        if (productAlertMessage == null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_alert_message).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_alert_message).visibility)
                            assertEquals(productAlertMessage, view.findViewById<Typography>(R.id.tv_product_alert_message).text.toString())
                        }
                        val productInfoGroup = view.findViewById<ViewGroup>(R.id.flexbox_order_product_info)
                        if (productInfo != null) {
                            for (i in productInfo.indices) {
                                when (productInfoGroup.getChildAt(i)) {
                                    is Typography -> assertEquals(
                                        productInfo[i],
                                        (productInfoGroup.getChildAt(i) as Typography).text.toString()
                                    )
                                    is LinearLayout -> assertEquals(
                                        productInfo[i],
                                        ((productInfoGroup.getChildAt(i) as LinearLayout).findViewById(com.tokopedia.purchase_platform.common.R.id.pp_label_product_info_add_on) as Typography).text.toString()
                                    )
                                }
                            }
                            assertEquals(productInfo.size, productInfoGroup.childCount)
                        } else {
                            assertEquals(0, productInfoGroup.childCount)
                        }
                        assertEquals(productQty.toString(), view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.quantity_editor_qty).text.toString())
                        if (productNotes != null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_placeholder).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_notes_edit).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_notes_preview).visibility)
                            assertEquals(productNotes, view.findViewById<Typography>(R.id.tv_product_notes_preview).text.toString())
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tf_note).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_notes_placeholder).visibility)
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_edit).visibility)
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_preview).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tf_note).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertProductQuantity(index: Int = 0, qty: Int) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnItemAtPosition<OrderProductCard>(
                index + 3,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert product $index quantity"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(qty.toString(), view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.quantity_editor_qty).text.toString())
                    }
                }
            )
        )
    }

    fun assertProductError(
        index: Int = 0,
        productName: String = "",
        productPrice: String = "",
        productVariant: String? = null,
        tickerMessage: String? = null
    ) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnItemAtPosition<OrderProductCard>(
                index + 3,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert product $index card error"

                    override fun perform(uiController: UiController?, view: View) {
                        if (tickerMessage != null) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.ticker_order_product).visibility)
                            assertEquals(tickerMessage, view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description).text.toString())
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.ticker_order_product).visibility)
                        }
                        assertEquals(productName, view.findViewById<Typography>(R.id.tv_product_name).text.toString())
                        assertEquals(productPrice, view.findViewById<Typography>(R.id.tv_product_price).text.toString())
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_slash_price).visibility)
                        assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_product_slash_price_percentage).visibility)
                        if (productVariant == null) {
                            assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_variant).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_product_variant).visibility)
                            assertEquals(productVariant, view.findViewById<Typography>(R.id.tv_product_variant).text.toString())
                        }
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_qty_left).visibility)
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_alert_message).visibility)
                        assertEquals(0, view.findViewById<ViewGroup>(R.id.flexbox_order_product_info).childCount)
                        assertEquals(View.GONE, view.findViewById<QuantityEditorUnify>(R.id.qty_editor_product).visibility)
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_placeholder).visibility)
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_edit).visibility)
                        assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_product_notes_preview).visibility)
                        assertEquals(View.GONE, view.findViewById<View>(R.id.tf_note).visibility)
                    }
                }
            )
        )
    }

    fun assertProfileEnable(isEnable: Boolean) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert profile enable"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(if (isEnable) 1.0f else 0.5f, view.alpha)
                    }
                }
            )
        )
    }

    fun assertAddressRevamp(addressName: String, addressDetail: String, isMainAddress: Boolean = false) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert address"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(addressName, view.findViewById<Typography>(R.id.tv_address_name).text.toString())
                        assertEquals(addressDetail, view.findViewById<Typography>(R.id.tv_address_detail).text.toString())
                        assertEquals(if (isMainAddress) View.VISIBLE else View.GONE, view.findViewById<View>(R.id.lbl_main_address).visibility)
                    }
                }
            )
        )
    }

    fun assertShipmentRevamp(
        shippingDuration: String? = null,
        shippingCourier: String = "",
        shippingPrice: String? = null,
        shippingEta: String? = null,
        shippingNotes: String? = null
    ) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shipment"

                    override fun perform(uiController: UiController?, view: View) {
                        if (shippingDuration != null) {
                            assertEquals(shippingDuration, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_duration).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_duration).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_duration).visibility)
                        }
                        assertEquals(shippingCourier, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_courier).text.toString())
                        assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_courier).visibility)
                        if (shippingPrice != null) {
                            assertEquals(shippingPrice, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_price).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_price).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_price).visibility)
                        }
                        if (shippingEta != null) {
                            assertEquals(shippingEta, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_eta).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_eta).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_eta).visibility)
                        }
                        if (shippingNotes != null) {
                            assertEquals(shippingNotes, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_notes).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_notes).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.tv_shipping_courier_notes).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertShipmentPromoRevamp(hasPromo: Boolean, promoTitle: String? = null, promoSubtitle: String? = null, promoDescription: String? = null) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shipment promo"

                    override fun perform(uiController: UiController?, view: View) {
                        val tickerPromo = view.findViewById<View>(com.tokopedia.logisticcart.R.id.ticker_shipping_promo)
                        if (hasPromo) {
                            assertEquals(View.VISIBLE, tickerPromo.visibility)
                            val title = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.ticker_shipping_promo_title)
                            if (promoTitle != null) {
                                assertEquals(promoTitle, title.text.toString())
                                assertEquals(View.VISIBLE, title.visibility)
                            } else {
                                assertEquals(View.GONE, title.visibility)
                            }
                            val subtitle = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.ticker_shipping_promo_subtitle)
                            if (promoSubtitle != null) {
                                assertEquals(promoSubtitle, subtitle.text.toString())
                                assertEquals(View.VISIBLE, subtitle.visibility)
                            } else {
                                assertEquals(View.GONE, subtitle.visibility)
                            }
                            val desc = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.ticker_shipping_promo_description)
                            if (promoDescription != null) {
                                assertEquals(promoDescription, desc.text.toString())
                                assertEquals(View.VISIBLE, desc.visibility)
                            } else {
                                assertEquals(View.GONE, desc.visibility)
                            }
                        } else {
                            assertEquals(View.GONE, tickerPromo.visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertShipmentError(errorMessage: String) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shipment error"

                    override fun perform(uiController: UiController?, view: View) {
                        val tvError = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_error_message)
                        assertEquals(View.VISIBLE, tvError.visibility)
                        assertEquals(errorMessage, tvError.text.toString())
                    }
                }
            )
        )
    }

    fun assertShipmentDisabled(title: String, description: String) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert shipment disable"

                    override fun perform(uiController: UiController?, view: View) {
                        val tvErrorTitle = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_courier)
                        assertEquals(View.VISIBLE, tvErrorTitle.visibility)
                        assertEquals(title, tvErrorTitle.text.toString())
                        val tvErrorDescription = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_shipping_price)
                        assertEquals(View.VISIBLE, tvErrorDescription.visibility)
                        assertEquals(description, tvErrorDescription.text.toString())
                    }
                }
            )
        )
    }

    fun assertInsurance(isChecked: Boolean) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderInsuranceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert insurance"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(isChecked, view.findViewById<CheckboxUnify>(R.id.cb_insurance).isChecked)
                    }
                }
            )
        )
    }

    fun assertPaymentRevamp(paymentName: String, paymentDetail: String?) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert payment"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(paymentName, view.findViewById<Typography>(R.id.tv_payment_name).text.toString())
                        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_name).visibility)
                        if (paymentDetail != null) {
                            assertEquals(paymentDetail, view.findViewById<Typography>(R.id.tv_payment_detail).text.toString())
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_detail).visibility)
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_payment_detail).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertInstallmentRevamp(detail: String?) {
        // Wait for espresso
        waitForBottomSheet()
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert installment"

                    override fun perform(uiController: UiController?, view: View) {
                        if (detail == null) {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_installment_type).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_installment_detail).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_type).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_detail).visibility)
                            assertEquals(detail, view.findViewById<Typography>(R.id.tv_installment_detail).text.toString())
                        }
                    }
                }
            )
        )
    }

    fun assertInstallmentErrorRevamp(errorMessage: String, errorAction: String?) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert installment error"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_error_message).visibility)
                        assertEquals(errorMessage, view.findViewById<Typography>(R.id.tv_installment_error_message).text.toString())
                        if (errorAction != null) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_error_action).visibility)
                            assertEquals(errorAction, view.findViewById<Typography>(R.id.tv_installment_error_action).text.toString())
                        } else {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_installment_error_action).visibility)
                        }
                    }
                }
            )
        )
    }

    fun assertGoCicilInstallment(detail: String?) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert installment"

                    override fun perform(uiController: UiController?, view: View) {
                        if (detail == null) {
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_installment_type).visibility)
                            assertEquals(View.GONE, view.findViewById<View>(R.id.tv_installment_detail_wrap).visibility)
                        } else {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_type).visibility)
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_installment_detail_wrap).visibility)
                            assertEquals(detail, view.findViewById<Typography>(R.id.tv_installment_detail_wrap).text.toString())
                        }
                    }
                }
            )
        )
    }

    fun assertProfilePaymentErrorRevamp(message: String, buttonText: String?) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert payment error"

                    override fun perform(uiController: UiController?, view: View) {
                        var expectedMessage = "$message "
                        if (buttonText != null) {
                            expectedMessage += buttonText
                        }
                        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_error_message).visibility)
                        assertEquals(expectedMessage, view.findViewById<Typography>(R.id.tv_payment_error_message).text.toString())
                    }
                }
            )
        )
    }

    fun assertProfilePaymentOvoErrorRevamp(message: String?, buttonText: String?) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert payment wallet error"

                    override fun perform(uiController: UiController?, view: View) {
                        if (message != null && buttonText != null) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_error_message).visibility)
                            assertEquals("$message $buttonText", view.findViewById<Typography>(R.id.tv_payment_error_message).text.toString())
                        } else if (message != null) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_error_message).visibility)
                            assertEquals("$message ", view.findViewById<Typography>(R.id.tv_payment_error_message).text.toString())
                        } else if (buttonText != null) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_ovo_error_action).visibility)
                            assertEquals(buttonText, view.findViewById<Typography>(R.id.tv_payment_ovo_error_action).text.toString())
                        }
                    }
                }
            )
        )
    }

    fun assertProfilePaymentInfoRevamp(message: String) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderPreferenceCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert payment info"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_payment_info).visibility)
                        assertEquals(message, view.findViewById<Typography>(R.id.tv_payment_info).text.toString())
                    }
                }
            )
        )
    }

    fun assertPayment(total: String, buttonText: String) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert total"

                    override fun perform(uiController: UiController?, view: View) {
                        val btnPay = view.findViewById<UnifyButton>(R.id.btn_pay)
                        assertEquals(buttonText, btnPay.text.toString())
                        assertEquals(true, btnPay.isEnabled)
                        assertEquals(false, btnPay.isLoading)
                        assertEquals(View.VISIBLE, btnPay.visibility)
                        assertEquals(total, view.findViewById<Typography>(R.id.tv_total_payment_value).text.toString())
                        assertEquals(View.VISIBLE, view.findViewById<View>(R.id.tv_total_payment_value).visibility)
                    }
                }
            )
        )
    }

    fun assertPaymentButtonEnable(isEnable: Boolean) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object : BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert button payment enable"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(isEnable, view.findViewById<UnifyButton>(R.id.btn_pay).isEnabled)
                    }
                }
            )
        )
    }

    fun assertPaymentFeeErrorTicker(isVisible: Boolean) {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object :
                    BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        /* no-op */
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is OrderTotalPaymentCard
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert ticker error payment fee enable"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(
                            if (isVisible) View.VISIBLE else View.GONE,
                            view.findViewById<Ticker>(R.id.ticker_payment_error).visibility
                        )
                    }
                }
            )
        )
    }

    fun assertGlobalErrorVisible() {
        onView(withId(R.id.global_error)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.VISIBLE, view.visibility)
        }
    }

    fun assertNoAddressLayoutVisible() {
        onView(withId(R.id.layout_no_address)).check(matches(isDisplayed()))
    }

    fun assertPromptBottomSheetVisible(title: String = "", description: String = "", primaryButton: String = "", secondaryButton: String? = null) {
        onView(withId(R.id.es_checkout)).check(matches(isDisplayed()))
        if (title.isNotEmpty()) {
            onView(withId(com.tokopedia.unifycomponents.R.id.empty_state_title_id)).check(matches(withText(title)))
        }
        if (description.isNotEmpty()) {
            onView(withId(com.tokopedia.unifycomponents.R.id.empty_state_description_id)).check(matches(withText(description)))
        }
        if (primaryButton.isNotEmpty()) {
            onView(withId(com.tokopedia.unifycomponents.R.id.empty_state_cta_id)).check(matches(isDisplayed())).check(matches(withText(primaryButton)))
        }
        if (!secondaryButton.isNullOrEmpty()) {
            onView(withId(com.tokopedia.unifycomponents.R.id.empty_state_secondary_cta_id)).check(matches(isDisplayed())).check(matches(withText(secondaryButton)))
        }
    }

    fun assertPromptDialogVisible(title: String = "", description: String = "", primaryButton: String = "", secondaryButton: String? = null) {
        onView(withId(com.tokopedia.dialog.R.id.dialog_container)).check(matches(isDisplayed()))
        if (title.isNotEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_title)).check(matches(withText(title)))
        }
        if (description.isNotEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_description)).check(
                matches(
                    withText(
                        description
                    )
                )
            )
        }
        if (primaryButton.isNotEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary)).check(matches(isDisplayed()))
                .check(matches(withText(primaryButton)))
        }
        if (!secondaryButton.isNullOrEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_btn_secondary)).check(matches(isDisplayed()))
                .check(matches(withText(secondaryButton)))
        }
    }

    fun assertUploadPrescription(prescriptionText: String, descriptionText: String? = "") {
        onView(withId(R.id.rv_order_summary_page)).perform(
            actionOnHolderItem(
                object :
                    BaseMatcher<RecyclerView.ViewHolder?>() {
                    override fun describeTo(description: Description?) {
                        // no op
                    }

                    override fun matches(item: Any?): Boolean {
                        return item is UploadPrescriptionViewHolder
                    }
                },
                object : ViewAction {
                    override fun getConstraints(): Matcher<View>? = null

                    override fun getDescription(): String = "assert upload prescription"

                    override fun perform(uiController: UiController?, view: View) {
                        assertEquals(
                            prescriptionText,
                            view.findViewById<Typography>(com.tokopedia.purchase_platform.common.R.id.upload_prescription_text).text.toString()
                        )
                        assertEquals(
                            descriptionText,
                            view.findViewById<Typography>(com.tokopedia.purchase_platform.common.R.id.upload_description_text).text.toString()
                        )
                    }
                }
            )
        )
    }
}

class OrderSummaryPageResultRobot {

    fun assertGoToPayment(redirectUrl: String, queryString: String, method: String) {
        val paymentPassData = Intents.getIntents().last().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals(redirectUrl, paymentPassData.redirectUrl)
        assertEquals(queryString, paymentPassData.queryString)
        assertEquals(method, paymentPassData.method)
    }
}
