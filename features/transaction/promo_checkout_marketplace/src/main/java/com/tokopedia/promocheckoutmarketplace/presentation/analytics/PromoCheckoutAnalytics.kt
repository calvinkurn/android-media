package com.tokopedia.promocheckoutmarketplace.presentation.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.Key
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.constant.PAGE_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.PAGE_OCC
import javax.inject.Inject

class PromoCheckoutAnalytics @Inject constructor() : TransactionAnalytics() {

    companion object {
        val EVENT_NAME_VIEW = "view"
        val EVENT_NAME_CLICK = "click"
    }

    private fun sendEventByPage(page: Int,
                                event: String,
                                eventAction: String,
                                eventLabel: String,
                                additionalData: Map<String, Any> = emptyMap()) {
        var eventCategoryPage: String? = null
        var eventNamePage: String? = null
        when (page) {
            PAGE_CART -> {
                when (event) {
                    EVENT_NAME_VIEW -> eventNamePage = EventName.VIEW_ATC_IRIS
                    EVENT_NAME_CLICK -> eventNamePage = EventName.CLICK_ATC
                }
                eventCategoryPage = EventCategory.CART
            }
            PAGE_CHECKOUT -> {
                when (event) {
                    EVENT_NAME_VIEW -> eventNamePage = EventName.VIEW_COURIER_IRIS
                    EVENT_NAME_CLICK -> eventNamePage = EventName.CLICK_COURIER
                }
                eventCategoryPage = EventCategory.COURIER_SELECTION
            }
            PAGE_OCC -> {
                when (event) {
                    EVENT_NAME_VIEW -> eventNamePage = EventName.VIEW_CHECKOUT_EXPRESS_IRIS
                    EVENT_NAME_CLICK -> eventNamePage = EventName.CLICK_CHECKOUT_EXPRESS
                }
                eventCategoryPage = EventCategory.ORDER_SUMMARY
            }
        }

        if (eventNamePage != null && eventCategoryPage != null) {
            val gtmData = getGtmData(
                    eventNamePage,
                    eventCategoryPage,
                    eventAction,
                    eventLabel
            )
            gtmData.putAll(additionalData)

            sendGeneralEvent(gtmData)
        }
    }

    fun sendEventEnhancedEcommerceByPage(page: Int,
                                         eventAction: String,
                                         eventLabel: String,
                                         eCommerceMapData: Map<String, Any>) {
        val dataLayer = getGtmData(
                EventName.PROMO_VIEW,
                when (page) {
                    PAGE_CART -> EventCategory.CART
                    PAGE_CHECKOUT -> EventCategory.COURIER_SELECTION
                    PAGE_OCC -> EventCategory.ORDER_SUMMARY
                    else -> ""
                },
                eventAction,
                eventLabel
        )
        dataLayer[Key.E_COMMERCE] = eCommerceMapData
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventViewBlacklistErrorAfterApplyPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.BLACKLIST_ERROR
        )
    }

    fun eventViewPhoneVerificationMessage(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.PHONE_VERIFICATION_MESSAGE
        )
    }

    fun eventClickButtonVerifikasiNomorHp(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.CLICK_BUTTON_VERIFIKASI_NOMOR_HP,
                ""
        )
    }

    fun eventViewAvailablePromoListEligiblePromo(page: Int, eCommerceMapData: Map<String, Any>) {
        sendEventEnhancedEcommerceByPage(
                page,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.ELIGIBLE_PROMO,
                eCommerceMapData)
    }

    fun eventViewAvailablePromoListIneligibleProduct(page: Int, eCommerceMapData: Map<String, Any>) {
        sendEventEnhancedEcommerceByPage(
                page,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.INELIGIBLE_PRODUCT,
                eCommerceMapData)
    }

    fun eventViewAvailablePromoListNoPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.NO_PROMO
        )
    }

    fun eventClickPilihPromoRecommendation(page: Int, promoCodes: List<String>) {
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.PROMO_CODE] = promoCodes.joinToString(", ")

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PILIH_PROMO_RECOMMENDATION,
                "",
                additionalData
        )
    }

    fun eventClickSelectKupon(page: Int, promoCode: String, triggerClashing: Boolean) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.SELECT_KUPON,
                "$promoCode - $triggerClashing"
        )
    }

    fun eventClickDeselectKupon(page: Int, promoCode: String, triggerClashing: Boolean) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.DESELECT_KUPON,
                "$promoCode - $triggerClashing"
        )
    }

    fun eventClickLihatDetailKupon(page: Int, promoCode: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_LIHAT_DETAIL_KUPON,
                promoCode
        )
    }

    fun eventClickExpandIneligiblePromoList(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_EXPAND_PROMO_LIST,
                EventLabel.INELIGIBLE_PROMO_LIST
        )
    }

    fun eventClickRemovePromoCode(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_REMOVE_PROMO_CODE,
                ""
        )
    }

    fun eventClickTerapkanPromo(page: Int, promoCode: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_TERAPKAN_PROMO,
                promoCode
        )
    }

    fun eventClickSelectPromo(page: Int, promoCode: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.SELECT_PROMO,
                promoCode
        )
    }

    fun eventClickDeselectPromo(page: Int, promoCode: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.DESELECT_PROMO,
                promoCode
        )
    }

    fun eventViewPopupSavePromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_POP_UP_SAVE_PROMO,
                ""
        )
    }

    fun eventClickPakaiPromoFailed(page: Int, errorMessage: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PAKAI_PROMO,
                errorMessage
        )
    }

    fun eventViewErrorPopup(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_ERROR_POP_UP,
                ""
        )
    }

    fun eventClickCobaLagi(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_COBA_LAGI,
                ""
        )
    }

    fun eventClickSimpanPromoBaru(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_SIMPAN_PROMO_BARU,
                ""
        )
    }

    fun eventClickKeluarHalaman(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_KELUAR_HALAMAN,
                ""
        )
    }

    fun eventClickPakaiPromoSuccess(page: Int, status: String, promoCodes: List<String>) {
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.PROMO_CODE] = promoCodes.joinToString(", ")

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PAKAI_PROMO,
                "success - $status",
                additionalData
        )
    }

    fun eventClickResetPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_RESET_PROMO,
                ""
        )
    }

    fun eventClickBeliTanpaPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_BELI_TANPA_PROMO,
                ""
        )
    }

    fun eventClickTerapkanAfterTypingPromoCode(page: Int, promoCode: String, isFromLasSeen: Boolean) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_TERAPKAN_PROMO,
                "$promoCode - ${if (isFromLasSeen) "1" else "0"}"
        )
    }

    fun eventClickPromoLastSeenItem(page: Int, promoCode: String) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.SELECT_PROMO_CODE_FROM_LAST_SEEN,
                promoCode
        )
    }

    fun eventDismissLastSeen(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.DISMISS_LAST_SEEN,
                ""
        )
    }

    fun eventClickInputField(page: Int, userId: String) {
        val additionalData = HashMap<String, Any>()
        additionalData.put(ExtraKey.USER_ID, userId)

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_INPUT_FIELD,
                "",
                additionalData
        )
    }

    fun eventShowLastSeenPopUp(page: Int, userId: String) {
        val additionalData = HashMap<String, Any>()
        additionalData.put(ExtraKey.USER_ID, userId)

        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.SHOW_LAST_SEEN_POP_UP,
                "",
                additionalData
        )
    }

}