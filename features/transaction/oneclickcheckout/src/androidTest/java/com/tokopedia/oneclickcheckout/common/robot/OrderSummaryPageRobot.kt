package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import android.widget.TextView
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
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.unifycomponents.UnifyButton
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

    fun clickAddProductQuantity(times: Int = 1) {
        val addButton = onView(withId(com.tokopedia.unifycomponents.R.id.quantity_editor_add)).perform(scrollTo())
        for (i in 0 until times) {
            addButton.perform(click())
        }
        Thread.sleep(OrderSummaryPageViewModel.DEBOUNCE_TIME)
    }

    fun clickMinusProductQuantity() {
        onView(withId(com.tokopedia.unifycomponents.R.id.quantity_editor_substract)).perform(scrollTo()).perform(click())
    }

    fun clickCloseProfileTicker() {
        onView(withId(R.id.ticker_preference_info)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            view.findViewById<View>(com.tokopedia.unifycomponents.R.id.ticker_close_icon).performClick()
        }
    }

    fun clickChangePreference(func: PreferenceListBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_choose_preference)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        PreferenceListBottomSheetRobot().apply(func)
    }

    fun clickAddOrChangePreferenceRevamp(func: (PreferenceListBottomSheetRobot.() -> Unit)?) {
        onView(withId(R.id.tv_new_choose_preference)).perform(scrollTo()).perform(click())
        func?.let {
            onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
            PreferenceListBottomSheetRobot().apply(it)
        }
    }

    fun clickChangeAddressRevamp(func: (AddressBottomSheetRobot.() -> Unit)? = null) {
        onView(withId(R.id.btn_new_change_address)).perform(scrollTo()).perform(click())
        if (func != null) {
            AddressBottomSheetRobot().apply(func)
        }
    }

    fun clickChangeDurationRevamp(func: DurationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.btn_new_change_duration)).perform(scrollTo()).perform(click())
        DurationBottomSheetRobot().apply(func)
    }

    fun clickChangePaymentRevamp() {
        onView(withId(R.id.btn_new_change_payment)).perform(scrollTo()).perform(click())
    }

    fun clickChangeCourier(func: CourierBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_shipping_price)).perform(scrollTo()).perform(click())
        CourierBottomSheetRobot().apply(func)
    }

    fun clickChangeCourierRevamp(func: CourierBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.btn_new_change_courier)).perform(scrollTo()).perform(click())
        CourierBottomSheetRobot().apply(func)
    }

    fun clickUbahDuration(func: DurationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_shipping_change_duration)).perform(scrollTo()).perform(click())
        DurationBottomSheetRobot().apply(func)
    }

    fun clickInsurance() {
        onView(withId(R.id.cb_insurance)).perform(scrollTo()).perform(click())
    }

    fun clickBboTicker() {
        onView(withId(R.id.ticker_shipping_promo)).perform(scrollTo())
        onView(withId(R.id.ticker_action)).perform(click())
    }

    fun clickApplyShipmentPromoRevamp() {
        onView(withId(R.id.ticker_new_action)).perform(scrollTo()).perform(click())
    }

    fun clickOvoActivationButton(func: OvoActivationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_payment_ovo_error_action)).perform(scrollTo()).perform(click())
        OvoActivationBottomSheetRobot().apply(func)
    }

    fun clickOvoActivationButtonRevamp(func: OvoActivationBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_new_payment_ovo_error_action)).perform(scrollTo()).perform(click())
        OvoActivationBottomSheetRobot().apply(func)
    }

    fun clickOvoTopUpButton() {
        onView(withId(R.id.tv_payment_ovo_error_action)).perform(scrollTo()).perform(click())
    }

    fun clickOvoTopUpButtonRevamp() {
        onView(withId(R.id.tv_new_payment_error_message)).perform(scrollTo()).perform(click())
    }

    fun clickChangeInstallment(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_installment_detail)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickChangeInstallmentRevamp(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_new_installment_detail)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickInstallmentErrorAction(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_installment_error_action)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickInstallmentErrorActionRevamp(func: InstallmentDetailBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.tv_new_installment_error_action)).perform(scrollTo()).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        InstallmentDetailBottomSheetRobot().apply(func)
    }

    fun clickButtonPromo() {
        onView(withId(R.id.btn_promo_checkout)).perform(scrollTo()).perform(click())
    }

    fun clickButtonOrderDetail(func: OrderPriceSummaryBottomSheetRobot.() -> Unit) {
        onView(withId(R.id.btn_order_detail)).perform(scrollTo()).perform(click())
        OrderPriceSummaryBottomSheetRobot().apply(func)
    }

    fun clickButtonContinueWithRedPromo() {
        onView(withId(com.tokopedia.purchase_platform.common.R.id.btn_continue)).perform(click())
    }

    fun closePromoNotEligibleBottomSheet() {
        onView(withId(com.tokopedia.purchase_platform.common.R.id.btn_close)).perform(click())
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
                          hasShopLocationImg: Boolean,
                          hasShopBadge: Boolean,
                          productName: String,
                          productPrice: String,
                          productSlashPrice: String?,
                          isFreeShipping: Boolean,
                          productQty: Int) {
        onView(withId(R.id.tv_shop_name)).perform(scrollTo()).check(matches(withText(shopName)))
        onView(withId(R.id.tv_shop_location)).check(matches(withText(shopLocation)))
        onView(withId(R.id.iu_image_fulfill)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (hasShopLocationImg) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
        onView(withId(R.id.iv_shop)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (hasShopBadge) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
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
        onView(withId(R.id.iu_free_shipping)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (isFreeShipping) {
                assertEquals(View.VISIBLE, view.visibility)
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
        onView(withId(com.tokopedia.unifycomponents.R.id.quantity_editor_qty)).check(matches(withText(productQty.toString())))
    }

    fun assertProductQuantity(qty: Int) {
        onView(withId(com.tokopedia.unifycomponents.R.id.quantity_editor_qty)).perform(scrollTo()).check(matches(withText(qty.toString())))
    }

    fun assertProfileRevampWording(wording: String) {
        onView(withId(R.id.tv_new_card_header)).perform(scrollTo()).check(matches(withText(wording)))
    }

    fun assertProfileRevampUtama(isDefaultProfile: Boolean) {
        onView(withId(R.id.lbl_new_default_preference)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(if (isDefaultProfile) View.VISIBLE else View.GONE, view.visibility)
        }
    }

    fun assertProfileRevampActionWording(actionWording: String) {
        onView(withId(R.id.tv_new_choose_preference)).perform(scrollTo()).check(matches(withText(actionWording)))
    }

    fun assertProfileTicker(isShown: Boolean,
                            title: String? = null,
                            description: String? = null,
                            closeButtonVisible: Boolean? = null) {
        onView(withId(R.id.ticker_preference_info)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (isShown) {
                assertEquals(View.VISIBLE, view.visibility)
                if (title != null) {
                    assertEquals(title, view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_title).text.toString())
                }
                if (description != null) {
                    assertEquals(description, view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description).text.toString())
                }
                if (closeButtonVisible == true) {
                    assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.unifycomponents.R.id.ticker_close_icon).visibility)
                } else {
                    assertEquals(View.GONE, view.findViewById<View>(com.tokopedia.unifycomponents.R.id.ticker_close_icon).visibility)
                }
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
    }

    fun assertProfileAddress(headerMessage: String,
                             addressName: String,
                             addressDetail: String,
                             isMainPreference: Boolean) {
        onView(withId(R.id.tv_card_header)).perform(scrollTo()).check(matches(withText(headerMessage)))
        onView(withId(R.id.tv_address_name)).check(matches(withText(addressName)))
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

    fun assertAddressRevamp(addressName: String, addressDetail: String) {
        onView(withId(R.id.tv_new_address_name)).check(matches(withText(addressName)))
        onView(withId(R.id.tv_new_address_detail)).check(matches(withText(addressDetail)))
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

    fun assertShipmentRevamp(shippingDuration: String?, shippingCourier: String, shippingPrice: String?, shippingEta: String?) {
        if (shippingDuration != null) {
            onView(withId(R.id.tv_new_shipping_duration)).perform(scrollTo()).check(matches(withText(shippingDuration)))
        } else {
            onView(withId(R.id.tv_new_shipping_duration)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
        }
        onView(withId(R.id.tv_new_shipping_courier)).perform(scrollTo()).check(matches(withText(shippingCourier)))
        if (shippingPrice != null) {
            onView(withId(R.id.tv_new_shipping_price)).perform(scrollTo()).check(matches(withText(shippingPrice)))
        } else {
            onView(withId(R.id.tv_new_shipping_price)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
        }
        if (shippingEta != null) {
            onView(withId(R.id.tv_new_shipping_courier_eta)).perform(scrollTo()).check(matches(withText(shippingEta)))
        } else {
            onView(withId(R.id.tv_new_shipping_courier_eta)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
        }
    }

    fun assertShipmentPromoRevamp(hasPromo: Boolean, promoTitle: String? = null, promoSubtitle: String? = null, promoDescription: String? = null) {
        onView(withId(R.id.ticker_new_shipping_promo)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            if (hasPromo) {
                assertEquals(View.VISIBLE, view.visibility)
                val title = view.findViewById<Typography>(R.id.ticker_new_shipping_promo_title)
                if (promoTitle != null) {
                    assertEquals(promoTitle, title.text)
                    assertEquals(View.VISIBLE, title.visibility)
                } else {
                    assertEquals(View.GONE, title.visibility)
                }
                val subtitle = view.findViewById<Typography>(R.id.ticker_new_shipping_promo_subtitle)
                if (promoSubtitle != null) {
                    assertEquals(promoSubtitle, subtitle.text)
                    assertEquals(View.VISIBLE, subtitle.visibility)
                } else {
                    assertEquals(View.GONE, subtitle.visibility)
                }
                val desc = view.findViewById<Typography>(R.id.ticker_new_shipping_promo_description)
                if (promoDescription != null) {
                    assertEquals(promoDescription, desc.text)
                    assertEquals(View.VISIBLE, desc.visibility)
                } else {
                    assertEquals(View.GONE, desc.visibility)
                }
            } else {
                assertEquals(View.GONE, view.visibility)
            }
        }
    }

    fun assertShipmentError(errorMessage: String) {
        onView(withId(R.id.tv_shipping_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(errorMessage)))
    }

    fun assertShipmentWithCustomDuration(shippingNameAndDuration: String, shippingCourierAndPrice: String, hasPromo: Boolean) {
        onView(withId(R.id.tv_shipping_duration)).perform(scrollTo()).check(matches(withText(shippingNameAndDuration)))
        onView(withId(R.id.tv_shipping_courier)).perform(scrollTo()).check(matches(withText(shippingCourierAndPrice)))
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
        onView(withId(R.id.tv_payment_name)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(paymentName)))
    }

    fun assertProfilePaymentDetail(detail: String) {
        onView(withId(R.id.tv_payment_detail)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(detail)))
    }

    fun assertPaymentRevamp(paymentName: String, paymentDetail: String?) {
        onView(withId(R.id.tv_new_payment_name)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(paymentName)))
        if (paymentDetail != null) {
            onView(withId(R.id.tv_new_payment_detail)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(paymentDetail)))
        } else {
            onView(withId(R.id.tv_new_payment_detail)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view?.visibility)
            }
        }
    }

    fun assertInstallment(detail: String?) {
        if (detail == null) {
            onView(withId(R.id.tv_installment_type)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
            onView(withId(R.id.tv_installment_detail)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
        } else {
            onView(withId(R.id.tv_installment_type)).perform(scrollTo()).check(matches(isDisplayed()))
            onView(withId(R.id.tv_installment_detail)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(detail)))
        }
    }

    fun assertInstallmentRevamp(detail: String?) {
        if (detail == null) {
            onView(withId(R.id.tv_new_installment_type)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
            onView(withId(R.id.tv_new_installment_detail)).check { view, noViewFoundException ->
                noViewFoundException?.printStackTrace()
                assertEquals(View.GONE, view.visibility)
            }
        } else {
            onView(withId(R.id.tv_new_installment_type)).perform(scrollTo()).check(matches(isDisplayed()))
            onView(withId(R.id.tv_new_installment_detail)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(detail)))
        }
    }

    fun assertInstallmentError() {
        onView(withId(R.id.tv_installment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("Cicilan tidak tersedia.")))
        onView(withId(R.id.tv_installment_error_action)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("Ubah")))
    }

    fun assertInstallmentErrorRevamp() {
        onView(withId(R.id.tv_new_installment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("Cicilan tidak tersedia.")))
        onView(withId(R.id.tv_new_installment_error_action)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("Ubah")))
    }

    fun assertProfilePaymentError(message: String, buttonText: String) {
        onView(withId(R.id.tv_payment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(message)))
        onView(withId(R.id.tv_payment_error_action)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(buttonText)))
    }

    fun assertProfilePaymentErrorRevamp(message: String, buttonText: String?) {
        var expectedMessage = "$message "
        if (buttonText != null) {
            expectedMessage += buttonText
        }
        onView(withId(R.id.tv_new_payment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(expectedMessage)))
    }

    fun assertProfilePaymentOvoError(message: String?, buttonText: String?) {
        if (message != null) {
            onView(withId(R.id.tv_payment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(message)))
        }
        if (buttonText != null) {
            onView(withId(R.id.tv_payment_ovo_error_action)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(buttonText)))
        }
    }

    fun assertProfilePaymentOvoErrorRevamp(message: String?, buttonText: String?) {
        if (message != null && buttonText != null) {
            onView(withId(R.id.tv_new_payment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("$message $buttonText")))
        } else if (message != null) {
            onView(withId(R.id.tv_new_payment_error_message)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText("$message ")))
        } else if (buttonText != null) {
            onView(withId(R.id.tv_new_payment_ovo_error_action)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(buttonText)))
        }
    }

    fun assertProfilePaymentInfoRevamp(message: String) {
        onView(withId(R.id.tv_new_payment_info)).perform(scrollTo()).check(matches(isDisplayed())).check(matches(withText(message)))
    }

    fun assertPayment(total: String, buttonText: String) {
        onView(withId(R.id.btn_pay)).perform(scrollTo()).check(matches(withText(buttonText))).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(true, view.isEnabled)
            assertEquals(false, (view as UnifyButton).isLoading)
        }
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText(total)))
    }

    fun assertPaymentButtonEnable(isEnable: Boolean) {
        onView(withId(R.id.btn_pay)).perform(scrollTo()).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(isEnable, view.isEnabled)
        }
    }

    fun assertPaymentErrorTicker(message: String) {
        onView(withId(R.id.ticker_payment_error)).perform(scrollTo()).check(matches(isDisplayed())).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(message, view.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description).text)
        }
    }

    fun assertGlobalErrorVisible() {
        onView(withId(R.id.global_error)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.VISIBLE, view.visibility)
        }
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
            onView(withId(com.tokopedia.dialog.R.id.dialog_description)).check(matches(withText(description)))
        }
        if (primaryButton.isNotEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary)).check(matches(isDisplayed())).check(matches(withText(primaryButton)))
        }
        if (!secondaryButton.isNullOrEmpty()) {
            onView(withId(com.tokopedia.dialog.R.id.dialog_btn_secondary)).check(matches(isDisplayed())).check(matches(withText(secondaryButton)))
        }
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