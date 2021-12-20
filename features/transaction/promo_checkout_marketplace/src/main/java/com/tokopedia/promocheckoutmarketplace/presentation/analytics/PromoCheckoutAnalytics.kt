package com.tokopedia.promocheckoutmarketplace.presentation.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension
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

    object EventAction {
        const val CLICK_BUTTON_VERIFIKASI_NOMOR_HP = "click button verifikasi nomor HP promo page"
        const val VIEW_AVAILABLE_PROMO_LIST = "view available promo list"
        const val CLICK_PILIH_PROMO_RECOMMENDATION = "click pilih promo recommendation"
        const val SELECT_KUPON = "select kupon"
        const val DESELECT_KUPON = "deselect kupon"
        const val CLICK_LIHAT_DETAIL_KUPON = "click lihat detail kupon"
        const val CLICK_EXPAND_PROMO_LIST = "click expand promo list"
        const val CLICK_REMOVE_PROMO_CODE = "click remove promo code"
        const val CLICK_TERAPKAN_PROMO = "click terapkan promo"
        const val SELECT_PROMO = "select promo"
        const val DESELECT_PROMO = "deselect promo"
        const val VIEW_POP_UP_SAVE_PROMO = "view pop up save promo"
        const val CLICK_PAKAI_PROMO = "click pakai promo"
        const val VIEW_ERROR_POP_UP = "view error pop up"
        const val CLICK_COBA_LAGI = "click coba lagi"
        const val CLICK_SIMPAN_PROMO_BARU = "click simpan promo baru"
        const val CLICK_KELUAR_HALAMAN = "click keluar halaman"
        const val CLICK_RESET_PROMO = "click reset promo"
        const val CLICK_BELI_TANPA_PROMO = "click beli tanpa promo"
        const val VIEW_PROMO_MESSAGE = "view promo message"
        const val SELECT_PROMO_CODE_FROM_LAST_SEEN = "select promo code from Last Seen"
        const val DISMISS_LAST_SEEN = "dismiss Last Seen"
        const val CLICK_INPUT_FIELD = "click input field"
        const val SHOW_LAST_SEEN_POP_UP = "show Last Seen pop-up"

        // Revamp
        const val CLICK_LIHAT_DETAIL_INELIGIBLE_COUPON = "click lihat detail ineligible kupon"

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

    // Promo BFI / Back Funnel Improvement (Revamp)

    fun eventImpressionEligiblePromoSection(page: Int) {
        /*
        {
          "event": "view_item",
          "eventAction": "impression- eligible promo section",
          "eventCategory": "courier selection",
          "eventLabel": "{{promo_id}} - {{index}} - {{promo_amount}} - {{promo_type}} - {{total_sisa_coupon}}",
          "businessUnit": "promo",
          "currentSite": "tokopediamarketplace",
          "promotions": [
            {
              "creative_name": "{creative_name}",
              "creative_slot": "{position_index}",
              "item_id": "{promo_id}",
              "item_name": "{promo_name}"
            }
          ],
          "userId": "{user_id}"
        }
        * */
        val additionalData = HashMap<String, Any>()
//        additionalData.put(ExtraKey.USER_ID, userId)

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                "impression- eligible promo section",
                "",
                additionalData
        )
    }


    // 12
    fun eventClickPilihOnRecommendation(page: Int, promoCodes: List<String>, isCausingClash: Boolean) {
        val additionalData = HashMap<String, Any>()
        additionalData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO)
        additionalData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)

        val promoCodeStr = promoCodes.joinToString(", ")
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PILIH_PROMO_RECOMMENDATION,
                "$promoCodeStr - $isCausingClash",
                additionalData
        )
    }

    // 13
    fun eventClickLihatDetailOnIneligibleCoupon(page: Int, promoId: String, ineligibleMessage: String) {
        val additionalData = HashMap<String, Any>()
        additionalData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO)
        additionalData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_LIHAT_DETAIL_INELIGIBLE_COUPON,
                "$promoId - $ineligibleMessage",
                additionalData
        )
    }

    // 14
    /*
    {
      "event": "view_item",
      "eventAction": "impression - ineligible promo section",
      "eventCategory": "cart",
      "eventLabel": "{{promo_id}} - {{message of ineligible reason}}",
      "businessUnit": "promo",
      "currentSite": "tokopediamarketplace",
      "promotions": [
        {
          "creative_name": "{creative_name}",
          "creative_slot": "{position_index}",
          "item_id": "{promo_id}",
          "item_name": "{promo_name}"
        }
      ],
      "userId": "{user_id}"
    }
    * */
}