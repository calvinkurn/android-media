package com.tokopedia.purchase_platform.common.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 11/03/20.
 */
object PromoRevampAnalytics {
    private const val VIEW_ATC_IRIS = "viewATCIris"
    private const val VIEW_COURIER_IRIS = "viewCourierIris"
    private const val CLICK_ATC = "clickATC"
    private const val CLICK_COURIER = "clickCourier"
    private const val CATEGORY_CART = "cart"
    private const val CATEGORY_COURIER_SELECTION = "courier selection"
    private const val EMPTY_CART_PROMO_APPLIED = "empty cart - promo already applied"
    private const val CLICK_PROMO_SECTION_WITH_PROMO = "click promo section with promo"
    private const val APPLIED = "applied"
    private const val NOT_APPLIED = "not applied"
    private const val VIEW_PROMO = "view promo"
    private const val DECREASED = "decreased"
    private const val RELEASED = "released"
    private const val AFTER_ADJUST_ITEM = " after adjust item"
    private const val VIEW_PROMO_ALREADY_APPLIED_IN_CART_LIST = "view promo already applied in cart list"
    private const val VIEW_PROMO_MESSAGE = "view promo message"
    private const val VIEW_PROMO_ALREADY_APPLIED_IN_CHECKOUT_LIST = "view promo already applied in checkout list"
    private const val VIEW_BOTTOMSHEET_PROMO_ERROR = "view bottom sheet promo error"
    private const val CLICK_LANJUT_BAYAR_ON_BOTTOMSHEET_PROMO_ERROR = "click lanjut bayar on bottom sheet promo error"
    private const val CLICK_PILIH_PROMO_LAIN_ON_BOTTOMSHEET_PROMO_ERROR = "click pilih promo lain on bottom sheet promo error"


    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    private fun sendEvent(eventData: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventData)
    }

    fun eventCartEmptyPromoApplied(listPromoCodes: List<String>) {
        var promo = ""
        listPromoCodes.forEach {
            if (promo.isNotEmpty()) promo += ", "
            promo += it
        }
        sendEventCategoryActionLabel(VIEW_ATC_IRIS, CATEGORY_CART, EMPTY_CART_PROMO_APPLIED, promo)
    }

    fun eventCartClickPromoSection(listPromoCodes: List<String>, isApplied: Boolean, userId: String) {
        var eventAction = CLICK_PROMO_SECTION_WITH_PROMO
        eventAction += if (isApplied) " $APPLIED"
        else " $NOT_APPLIED"

        var promo = ""
        listPromoCodes.forEach {
            if (promo.isNotEmpty()) promo += ", "
            promo += it
        }
        val gtmData = TrackAppUtils.gtmData(CLICK_ATC, CATEGORY_CART, eventAction, promo)
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.USER_ID] = userId
        sendEvent(gtmData)
    }

    // TODO : check after backend give new struct
    fun eventCartViewPromoAfterAdjustItem(isDecreased: Boolean) {
        var eventAction = VIEW_PROMO
        eventAction += if (isDecreased) eventAction += " $DECREASED"
        else eventAction += " $RELEASED"
        eventAction += ""
        sendEventCategoryAction(VIEW_ATC_IRIS, CATEGORY_CART, AFTER_ADJUST_ITEM)
    }

    fun eventCartViewPromoAlreadyApplied() {
        sendEventCategoryAction(VIEW_ATC_IRIS, CATEGORY_CART, VIEW_PROMO_ALREADY_APPLIED_IN_CART_LIST)
    }

    fun eventCartViewPromoMessage(promoMessage: String) {
        sendEventCategoryActionLabel(VIEW_ATC_IRIS, CATEGORY_CART, VIEW_PROMO_MESSAGE, promoMessage)
    }

    fun eventCheckoutClickPromoSection(listPromoCodes: List<String>, isApplied: Boolean, userId: String) {
        var eventAction = CLICK_PROMO_SECTION_WITH_PROMO
        eventAction += if (isApplied && listPromoCodes.isNotEmpty()) " $APPLIED" else " $NOT_APPLIED"

        var promo = ""
        listPromoCodes.forEach {
            if (promo.isNotEmpty()) promo += ", "
            promo += it
        }
        val gtmData = TrackAppUtils.gtmData(CLICK_COURIER, CATEGORY_COURIER_SELECTION, eventAction, promo)
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.USER_ID] = userId
        sendEvent(gtmData)
    }

    fun eventCheckoutViewPromoAlreadyApplied() {
        sendEventCategoryAction(VIEW_COURIER_IRIS, CATEGORY_COURIER_SELECTION, VIEW_PROMO_ALREADY_APPLIED_IN_CHECKOUT_LIST)
    }

    fun eventCheckoutViewPromoMessage(promoMessage: String) {
        sendEventCategoryActionLabel(VIEW_COURIER_IRIS, CATEGORY_COURIER_SELECTION, VIEW_PROMO_MESSAGE, promoMessage)
    }

    fun eventCheckoutViewBottomsheetPromoError() {
        sendEventCategoryAction(VIEW_COURIER_IRIS, CATEGORY_COURIER_SELECTION, VIEW_BOTTOMSHEET_PROMO_ERROR)
    }

    fun eventCheckoutClickPilihPromoLainOnBottomsheetPromoError() {
        sendEventCategoryAction(CLICK_COURIER, CATEGORY_COURIER_SELECTION, CLICK_PILIH_PROMO_LAIN_ON_BOTTOMSHEET_PROMO_ERROR)
    }
}