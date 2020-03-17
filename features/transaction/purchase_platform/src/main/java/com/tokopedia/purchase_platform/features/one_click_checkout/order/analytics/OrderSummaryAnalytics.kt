package com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class OrderSummaryAnalytics : TransactionAnalytics() {

    var irisSession: IrisSession? = null

    fun OrderSummaryAnalytics(context: Context) {
        irisSession = IrisSession(context)
    }

    fun eventEditQuantityIncrease(productId: String, shopId: String, productQuantity: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_QUANTITY_INCRESE,
                "$productId - $shopId - $productQuantity"
        )
    }

    fun eventEditQuantityDecrease(productId: String, shopId: String, productQuantity: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_QUANTITY_DECREASE,
                "$productId - $shopId - $productQuantity"
        )
    }

    fun eventClickVariantColour(productId: String, shopId: String, colourValue: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_VARIANT_COLOUR,
                "$productId - $shopId - $colourValue"
        )
    }

    fun eventClickVariantSize(productId: String, shopId: String, sizeValue: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_VARIANT_SIZE,
                "$productId - $shopId - $sizeValue"
        )
    }

    fun eventClickSellerNotes(productId: String, shopId: String){
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_SELLER_NOTES,
                "$productId - $shopId"
        )
    }

    fun eventClickSavesPreferenceOnBoarding() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.ONBOARDING_USER_SAVES_PREFERENCE
        )
    }

    fun eventClickEditPreferenceOnBoarding() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.ONBOARDING_USER_EDITS_PREFERENCE
        )
    }

    fun eventChangeCourierOSP(shippingAgentId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.USER_CHANGE_COURIER_OSP,
                shippingAgentId
        )
    }

    fun eventClickSimilarProduct(eventLabel: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.FIND_SIMILAR_PRODUCT,
                eventLabel
        )
    }

    fun eventClickOnInsurance(productId: String, insuranceCheck: String, insuranceValue: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_ON_INSURANCE,
                "$productId - $insuranceCheck - $insuranceValue"
        )
    }

    fun eventClickBayarNotSuccess(eventId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_BAYAR_NOT_SUCCESS,
                "$NOT_SUCCESS - $eventId"
        )
    }

    fun eventAddPreferensiFromOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_ADD_PREFERENSI_FROM_OSP
        )
    }

    fun eventChangesProfile() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.USER_CHANGES_PROFILE
        )
    }

    //irisSession(?)
    fun eventViewErrorMesageIris() {
        sendEventCategoryActionLabel(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_ERROR_ON_OSP,
                irisSession?.getSessionId()
        )
    }

    fun eventUserSetsFirstPreference(userId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.USER_SETS_FIRST_PREFERENCE,
                userId
        )
    }

    fun eventChooseBboAsDuration() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CHOOSE_BBO_AS_DURATION
        )
    }

    fun eventChooseCourierSelectionOSP(shippingAgentId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CHOOSE_COURIER_FROM_COURIER_SELECTION_OSP,
                shippingAgentId
        )
    }

    fun eventClickRingkasanBelanjaOSP(productId: String, totalPrice: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_RINGKASAN_BELANJA_OSP,
                "$productId - $totalPrice"
        )
    }

    fun eventClickGearLogoInPreferenceFromGantiPilihanOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_GEAR_LOGO_IN_PREFERENCE_FROM_GANTI_PILIHAN_OSP
        )
    }

    fun eventClickGunakanPilihanIniFromGantiPilihanOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.GUNAKAN_PILIHAN_INI_FROM_GANTI_PILIHAN_OSP
        )
    }

    fun eventCLickTambahPilihanFromGantiPilihanOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_TAMBAH_PILIHAN_FROM_GANTI_PILIHAN_OSP
        )
    }

    fun eventClickBackFromOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_BACK_FROM_OSP
        )
    }

    companion object {
        private const val NOT_SUCCESS = "not success"
    }
}