package com.tokopedia.promocheckoutmarketplace.presentation.analytics

import android.os.Bundle
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.Key
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.constant.PAGE_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.PAGE_OCC
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class PromoCheckoutAnalytics @Inject constructor(private val userSession: UserSessionInterface) : TransactionAnalytics() {

    companion object {
        val EVENT_NAME_VIEW = "view"
        val EVENT_NAME_CLICK = "click"
    }

    @Parcelize
    class Promotion(
            @SerializedName("creative_name")
            var creativeName: String = "",
            @SerializedName("creative_slot")
            var creativeSlot: String = "",
            @SerializedName("item_id")
            var itemId: String = "",
            @SerializedName("item_name")
            var itemName: String = ""
    ) : Parcelable

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
        const val SELECT_PROMO_CODE_FROM_LAST_SEEN = "select promo code from Last Seen"
        const val DISMISS_LAST_SEEN = "dismiss Last Seen"
        const val CLICK_INPUT_FIELD = "click input field"
        const val SHOW_LAST_SEEN_POP_UP = "show Last Seen pop-up"

        // Revamp
        const val CLICK_LIHAT_DETAIL_INELIGIBLE_COUPON = "click lihat detail ineligible kupon"
        const val IMPRESSION_INELIGIBLE_PROMO_SECTION = "impression - ineligible promo section"
        const val IMPRESSION_RECOMMENDATION_PROMO_SECTION = "impression - recommendation promo section"
        const val CLICK_TAB_PROMO_CATEGORY = "click tab promo category"
        const val VIEW_ERROR_AFTER_CLICK_PAKAI_PROMO = "view error after click pakai promo"
        const val VIEW_ERROR_AFTER_CLICK_TERAPKAN_PROMO = "view error after click terapkan promo"
        const val IMPRESSION_LOCK_TO_SHIPPING_PROMO_SECTION = "impression - lock to shipping promo section"
        const val IMPRESSION_LOCK_TO_PAYMENT_PROMO_SECTION = "impression - lock to payment promo section"
        const val IMPRESSION_ELIGIBLE_PROMO_SECTION = "impression- eligible promo section"
    }

    private fun sendEventByPage(page: Int,
                                event: String,
                                eventAction: String,
                                eventLabel: String,
                                additionalData: Map<String, Any> = emptyMap(),
                                isPromoBackFunnelImprovement: Boolean = false) {
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
                    EVENT_NAME_VIEW -> eventNamePage = if (isPromoBackFunnelImprovement) EventName.VIEW_CHECKOUT_IRIS else EventName.VIEW_COURIER_IRIS
                    EVENT_NAME_CLICK -> eventNamePage = if (isPromoBackFunnelImprovement) EventName.CLICK_CHECKOUT else EventName.CLICK_COURIER
                }
                eventCategoryPage = EventCategory.COURIER_SELECTION
            }
            PAGE_OCC -> {
                when (event) {
                    EVENT_NAME_VIEW -> eventNamePage = if (isPromoBackFunnelImprovement) EventName.VIEW_ORDER_IRIS else EventName.VIEW_CHECKOUT_EXPRESS_IRIS
                    EVENT_NAME_CLICK -> eventNamePage = if (isPromoBackFunnelImprovement) EventName.CLICK_ORDER else EventName.CLICK_CHECKOUT_EXPRESS
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

    private fun sendEventEnhancedEcommerceByPage(page: Int,
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

    private fun sendEventEnhancedEcommerceByPage(page: Int,
                                                 eventAction: String,
                                                 eventLabel: String,
                                                 bundle: Bundle) {
        bundle.apply {
            putString(TrackAppUtils.EVENT, EventName.VIEW_ITEM)
            putString(TrackAppUtils.EVENT_CATEGORY,
                    when (page) {
                        PAGE_CART -> EventCategory.CART
                        PAGE_CHECKOUT -> EventCategory.COURIER_SELECTION
                        PAGE_OCC -> EventCategory.ORDER_SUMMARY
                        else -> ""
                    }
            )
            putString(TrackAppUtils.EVENT_ACTION, eventAction)
            putString(TrackAppUtils.EVENT_LABEL, eventLabel)
            putString(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO)
            putString(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            putString(ExtraKey.USER_ID, userSession.userId)
        }

        sendEnhancedEcommerce(
                eventName = EventName.VIEW_ITEM,
                bundle = bundle
        )
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

    // 1 - Done
    fun eventImpressionEligiblePromoSection(page: Int, index: Int, promoItem: PromoListItemUiModel) {
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoItem.uiData.promoCode,
                itemName = promoItem.uiData.title
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.IMPRESSION_ELIGIBLE_PROMO_SECTION,
                eventLabel = "${promoItem.uiData.promoCode} - $index - ${promoItem.uiData.benefitAmount} - promo type - ${promoItem.uiData.remainingPromoCount} ",
                bundle = bundle
        )
    }

    // 2,3,4 - Canceled

    // 5 - Done
    fun eventImpressionLockToPaymentPromoSection(page: Int, index: Int, promoItem: PromoListItemUiModel) {
        /*
        {
          "event": "view_item",
          "eventAction": "impression - lock to payment promo section",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{promo_amount}} - {{payament option}}",
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoItem.uiData.promoCode,
                itemName = promoItem.uiData.title
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.IMPRESSION_LOCK_TO_PAYMENT_PROMO_SECTION,
                eventLabel = "${promoItem.uiData.promoCode} - ${promoItem.uiData.benefitAmount} - ${promoItem.uiData.paymentOptions}",
                bundle = bundle
        )
    }

    // 6 - Done
    fun eventImpressionLockToShippingPromoSection(page: Int, index: Int, promoItem: PromoListItemUiModel) {
        /*
        {
          "event": "view_item",
          "eventAction": "impression - lock to shipping promo section",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{promo_amount}} - {{shipping option}}",
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoItem.uiData.promoCode,
                itemName = promoItem.uiData.title
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.IMPRESSION_LOCK_TO_SHIPPING_PROMO_SECTION,
                eventLabel = "${promoItem.uiData.promoCode} - ${promoItem.uiData.benefitAmount} - ${promoItem.uiData.shippingOptions} ",
                bundle = bundle
        )
    }

    // 7
    fun eventImpressionHighlightedPromoSection(page: Int, index: Int, promoItem: PromoListItemUiModel) {
        /*
        {
          "event": "view_item",
          "eventAction": "impression - highlighted promo section",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{promo_amount}}",
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoItem.uiData.promoCode,
                itemName = promoItem.uiData.title
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.VIEW_ERROR_AFTER_CLICK_TERAPKAN_PROMO,
                eventLabel = "${promoItem.uiData.promoCode} - ${promoItem.uiData.benefitAmount}",
                bundle = bundle
        )
    }

    // 8 - Done
    fun eventViewErrorAfterClickTerapkanPromo(page: Int, errorMessage: String, index: Int, promoCode: String) {
        /*
        {
          "event": "view_item",
          "eventAction": "view error after click terapkan promo",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{error message}}",
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoCode,
                itemName = ""
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.VIEW_ERROR_AFTER_CLICK_TERAPKAN_PROMO,
                eventLabel = "$promoCode - $errorMessage",
                bundle = bundle
        )
    }

    // 9 - Done
    fun eventViewErrorAfterClickPakaiPromo(page: Int, promoCode: String, errorMessage: String) {
        /*
        {
          "event": "viewATCIris",
          "eventAction": "view error after click pakai promo",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{error message}}",
          "businessUnit": "{businessUnit}",
          "currentSite": "{currentSite}"
        }
        * */
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO
        additionalData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE

        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_ERROR_AFTER_CLICK_PAKAI_PROMO,
                "$promoCode - $errorMessage",
                additionalData
        )
    }

    // 10 - Done
    fun eventClickTabPromoCategory(page: Int, promoCategoryName: String) {
        /*
        {
          "event": "clickATC",
          "eventAction": "click tab promo category",
          "eventCategory": "cart",
          "eventLabel": "{{category name}}",
          "businessUnit": "{businessUnit}",
          "currentSite": "{currentSite}"
        }
        * */
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO
        additionalData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_TAB_PROMO_CATEGORY,
                promoCategoryName,
                additionalData
        )
    }

    // 11 - Done
    fun eventImpressionRecommendationPromoSection(page: Int, totalCouponCanApply: Int, totalPotentialBenefit: Int) {
        /*
        {
          "event": "viewATCIris",
          "eventAction": "impression - recommendation promo section",
          "eventCategory": "cart",
          "eventLabel": "{{total coupon can apply}} - {{all potential benefit}}",
          "businessUnit": "{businessUnit}",
          "currentSite": "{currentSite}"
        }
        * */
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO
        additionalData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE

        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.IMPRESSION_RECOMMENDATION_PROMO_SECTION,
                "$totalCouponCanApply - $totalPotentialBenefit",
                additionalData
        )
    }

    // 12 - Done
    fun eventClickPilihOnRecommendation(page: Int, promoCode:  String, isCausingClash: Boolean) {
        /*
        {
          "event": "clickATC",
          "eventAction": "click pilih promo recommendation",
          "eventCategory": "cart",
          "eventLabel": "{{promo_code}} - {{status trigger clashing or not}}",
          "businessUnit": "{businessUnit}",
          "currentSite": "{currentSite}"
        }
        * */
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO
        additionalData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PILIH_PROMO_RECOMMENDATION,
                "$promoCode - $isCausingClash",
                additionalData
        )
    }

    // 13 - Done
    fun eventClickLihatDetailOnIneligibleCoupon(page: Int, promoCode: String, ineligibleMessage: String) {
        /*
        {
          "event": "clickATC",
          "eventAction": "click lihat detail ineligible kupon",
          "eventCategory": "cart",
          "eventLabel": "{{promo_id}} - {{message of ineligible reason}}",
          "businessUnit": "{businessUnit}",
          "currentSite": "{currentSite}"
        }
        * */
        val additionalData = HashMap<String, Any>()
        additionalData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PROMO
        additionalData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE

        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_LIHAT_DETAIL_INELIGIBLE_COUPON,
                "$promoCode - $ineligibleMessage",
                additionalData
        )
    }

    // 14 - Done
    fun eventImpressionIneligiblePromoSection(page: Int, index: Int, promoItem: PromoListItemUiModel) {
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
        val promotion = Promotion(
                creativeName = "",
                creativeSlot = index.toString(),
                itemId = promoItem.uiData.promoCode,
                itemName = promoItem.uiData.title
        )

        val bundle = Bundle().apply {
            putParcelableArrayList("promotions", arrayListOf(promotion))
        }
        sendEventEnhancedEcommerceByPage(
                page = page,
                eventAction = EventAction.IMPRESSION_INELIGIBLE_PROMO_SECTION,
                eventLabel = "${promoItem.uiData.promoCode} - ${promoItem.uiData.errorMessage}",
                bundle = bundle
        )
    }
}