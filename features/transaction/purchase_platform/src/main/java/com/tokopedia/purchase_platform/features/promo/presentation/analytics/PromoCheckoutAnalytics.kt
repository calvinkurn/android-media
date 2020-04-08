package com.tokopedia.purchase_platform.features.promo.presentation.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class PromoCheckoutAnalytics @Inject constructor() : TransactionAnalytics() {

    companion object {
        val PAGE_CART = 1
        @JvmStatic
        val PAGE_CHECKOUT = 2

        val EVENT_NAME_VIEW = "view"
        val EVENT_NAME_CLICK = "click"
    }

    private fun sendEventByPage(page: Int,
                                event: String,
                                eventAction: String,
                                eventLabel: String) {
        var eventCategoryPage: String? = null
        var eventNamePage: String? = null
        if (page == PAGE_CART) {
            if (event == EVENT_NAME_VIEW) {
                eventNamePage = EventName.VIEW_ATC_IRIS
            } else if (event == EVENT_NAME_CLICK) {
                eventNamePage = EventName.CLICK_ATC
            }
            eventCategoryPage = EventCategory.CART
        } else if (page == PAGE_CHECKOUT) {
            if (event == EVENT_NAME_VIEW) {
                eventNamePage = EventName.VIEW_COURIER_IRIS
            } else if (event == EVENT_NAME_CLICK) {
                eventNamePage = EventName.CLICK_COURIER
            }
            eventCategoryPage = EventCategory.COURIER_SELECTION
        }

        if (eventNamePage != null && eventCategoryPage != null) {
            sendEventCategoryActionLabel(
                    event,
                    eventCategoryPage,
                    eventAction,
                    eventLabel
            )
        }
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

    fun eventViewAvailablePromoListIneligibleProduct(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_VIEW,
                EventAction.VIEW_AVAILABLE_PROMO_LIST,
                EventLabel.INELIGIBLE_PRODUCT
        )
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
        val data = hashMapOf<String, Any>()
        if (page == PAGE_CART) {
            data["event"] = EventName.CLICK_ATC
            data["eventCategory"] = EventCategory.CART
        } else if (page == PAGE_CHECKOUT) {
            data["event"] = EventName.CLICK_COURIER
            data["eventCategory"] = EventCategory.COURIER_SELECTION
        }
        data["eventAction"] = EventAction.CLICK_PILIH_PROMO_RECOMMENDATION
        data["eventLabel"] = ""
        data["promoCode"] = promoCodes.joinToString(", ", "[", "]")

        if (data.containsKey("event") && data.containsKey("eventCategory")) {
            sendGeneralEvent(data)
        }
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

    fun eventClickPilihPromoFailedTerjadiKesalahanServer(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_PILIH_PROMO,
                EventLabel.FAILED_TERJADI_KESALAHAN_SERVER
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
        val data = hashMapOf<String, Any>()
        if (page == PAGE_CART) {
            data["event"] = EventName.CLICK_ATC
            data["eventCategory"] = EventCategory.CART
        } else if (page == PAGE_CHECKOUT) {
            data["event"] = EventName.CLICK_COURIER
            data["eventCategory"] = EventCategory.COURIER_SELECTION
        }
        data["eventAction"] = EventAction.CLICK_PAKAI_PROMO
        data["eventLabel"] = "success - $status"
        data["promoCode"] = promoCodes.joinToString(", ", "[", "]")

        if (data.containsKey("event") && data.containsKey("eventCategory")) {
            sendGeneralEvent(data)
        }
    }

    fun eventClickResetPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_RESET_PROMO,
                ""
        )
    }

    // Todo FU : UI not valid, row 46
    fun eventClickBeliTanpaPromo(page: Int) {
        sendEventByPage(
                page,
                EVENT_NAME_CLICK,
                EventAction.CLICK_BELI_TANPA_PROMO,
                ""
        )
    }

}