package com.tokopedia.purchase_platform.features.promo.presentation

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class PromoCheckoutAnalytics @Inject constructor() : TransactionAnalytics() {

    // Promo Cart

    fun eventViewAvailablePromoListIneligibleProduct() {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.INELIGIBLE_PRODUCT
        )
    }

    fun eventViewAvailablePromoListNoPromo() {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.NO_PROMO
        )
    }

    fun eventClickPilihPromoRecommendation(promoCodes: List<String>) {
        val data = hashMapOf<String, Any>()
        data["event"] = EventName.CLICK_ATC
        data["eventCategory"] = EventCategory.CART
        data["eventAction"] = EventAction.CLICK_PILIH_PROMO_RECOMMENDATION
        data["eventLabel"] = ""
        data["promoCode"] = promoCodes.joinToString(", ", "[", "]")
        TrackApp.getInstance().getGTM().sendGeneralEvent(data)
    }

    fun eventClickSelectKupon(promoCode: String, triggerClashing: Boolean) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.SELECT_KUPON,
                "$promoCode - $triggerClashing"
        )
    }

    fun eventClickDeselectKupon(promoCode: String, triggerClashing: Boolean) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.DESELECT_KUPON,
                "$promoCode - $triggerClashing"
        )
    }

    fun eventClickLihatDetailKupon(promoCode: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_DETAIL_KUPON,
                promoCode
        )
    }

    fun eventClickExpandPromoList() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_EXPAND_PROMO_LIST,
                EventLabel.INELIGIBLE_PROMO_LIST
        )
    }

    fun eventClickRemovePromoCode() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_REMOVE_PROMO_CODE,
                ""
        )
    }

    fun eventClickTerapkanPromo(promoCode: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TERAPKAN_PROMO,
                promoCode
        )
    }

    fun eventClickSelectPromo(promoCode: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.SELECT_PROMO,
                promoCode
        )
    }


    // Promo checkout
}