package com.tokopedia.purchase_platform.common.analytics

import android.app.Activity

class EPharmacyAnalytics constructor(val userId: String) : TransactionAnalytics() {
    companion object {
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val VALUE_BUSINESS_UNIT = "Physical Goods"
        const val KEY_CURRENT_SITE = "currentSite"
        const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        const val KEY_USER_ID = "userId"
        const val KEY_TRACKER_ID = "trackerId"

        const val VALUE_PRESCRIPTION_ATTACHED = "prescription is attached"
        const val VALUE_NO_PRESCRIPTION_ATTACHED = "no attachment"
    }

    interface Events {
        companion object {
            const val CLICK_PP = "clickPP"
            const val VIEW_PP_IRIS = "viewPPIris"
        }
    }

    interface Category {
        companion object {
            const val COURIER_SELECTION = "courier selection"
            const val CART = "cart"
        }
    }

    interface Actions {
        companion object {
            const val CLICK_INFO_CHAT_DOKTER = "click info chat dokter"
            const val CLICK_UPLOAD_PRESCRIPTION_WIDGET = "click upload prescription widget"
            const val VIEW_BANNER_PESANAN_BUTUH_RESEP_IN_CHECKOUT_PAGE =
                "view banner pesanan butuh resep in checkout page"
            const val CLICK_LAMPIRKAN_RESEP_DOKTER = "click lampirkan resep dokter"
            const val CLICK_PILIH_PEMBAYARAN = "click pilih pembayaran"
            const val OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON =
                "epharmacy checkout page abandon"
            const val CLICK_KELUAR_IN_ABANDON_PAGE = "click keluar in abandon page"
            const val CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE = "click lanjut bayar in abandon page"
        }
    }

    interface TrackerId {
        companion object {
            const val CLICK_INFO_CHAT_DOKTER = "37939"
            const val CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID = "33117"
            const val VIEW_BANNER_PESANAN_BUTUH_RESEP_IN_CHECKOUT_PAGE = "38025"
            const val CLICK_LAMPIRKAN_RESEP_DOKTER = "38026"
            const val CLICK_PILIH_PEMBAYARAN = "38027"
            const val OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON = "38029"
            const val CLICK_KELUAR_IN_ABANDON_PAGE = "38030"
            const val CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE = "38031"
        }
    }

    fun clickInfoChatDokter(enablerName: String, shopId: String, cartId: List<String>) {
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.CART,
            Actions.CLICK_INFO_CHAT_DOKTER,
            " - $enablerName - $shopId - ${cartId.joinToString(",")}"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_INFO_CHAT_DOKTER
        sendGeneralEvent(gtmData)
    }

    fun sendPrescriptionWidgetClick(cartId: String) {
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.COURIER_SELECTION,
            Actions.CLICK_UPLOAD_PRESCRIPTION_WIDGET,
            "cart_id: $cartId"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID
        gtmData[KEY_USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun viewBannerPesananButuhResepInCheckoutPage(
        epharmacyGroupIds: List<String>,
        enablerNames: List<String>,
        shopIds: List<String>,
        cartIds: List<String>
    ) {
        val gtmData = getGtmData(
            Events.VIEW_PP_IRIS,
            Category.COURIER_SELECTION,
            Actions.VIEW_BANNER_PESANAN_BUTUH_RESEP_IN_CHECKOUT_PAGE,
            "${epharmacyGroupIds.joinToString(",")} - " +
                "${enablerNames.joinToString(",")} - ${shopIds.joinToString(",")} - ${cartIds.joinToString(",")}"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.VIEW_BANNER_PESANAN_BUTUH_RESEP_IN_CHECKOUT_PAGE
        sendGeneralEvent(gtmData)
    }

    fun clickLampirkanResepDokter(
        state: String,
        buttonText: String,
        buttonNotes: String,
        epharmacyGroupIds: List<String>,
        enablerNames: List<String>,
        shopIds: List<String>,
        cartIds: List<String>
    ) {
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.COURIER_SELECTION,
            Actions.CLICK_LAMPIRKAN_RESEP_DOKTER,
            "$state - $buttonText - " +
                "$buttonNotes - ${epharmacyGroupIds.joinToString(",")} - " +
                "${enablerNames.joinToString(",")} - ${shopIds.joinToString(",")} - ${cartIds.joinToString(",")}"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_LAMPIRKAN_RESEP_DOKTER
        sendGeneralEvent(gtmData)
    }

    fun clickPilihPembayaran(
        buttonNotes: String,
        epharmacyGroupIds: List<String>,
        isSuccess: Boolean,
        reason: String
    ) {
        val successValue = if (isSuccess) {
            "success"
        } else {
            "failed"
        }
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.COURIER_SELECTION,
            Actions.CLICK_PILIH_PEMBAYARAN,
            "$successValue - $buttonNotes - ${epharmacyGroupIds.joinToString(",")} - $successValue - $reason"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_PILIH_PEMBAYARAN
        sendGeneralEvent(gtmData)
    }

    fun viewAbandonCheckoutPage(
        activity: Activity,
        epharmacyGroupIds: List<String>,
        hasAttachedPrescription: Boolean
    ) {
        val status = if (hasAttachedPrescription) {
            VALUE_PRESCRIPTION_ATTACHED
        } else {
            VALUE_NO_PRESCRIPTION_ATTACHED
        }
        val customData = mutableMapOf(
            KEY_TRACKER_ID to TrackerId.OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON,
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE
        )
        sendScreenName(
            activity,
            "${Actions.OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON} - ${epharmacyGroupIds.joinToString(",")} - $status",
            customData
        )
    }

    fun clickKeluarInAbandonPage(
        epharmacyGroupIds: List<String>,
        hasAttachedPrescription: Boolean
    ) {
        val status = if (hasAttachedPrescription) {
            VALUE_PRESCRIPTION_ATTACHED
        } else {
            VALUE_NO_PRESCRIPTION_ATTACHED
        }
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.COURIER_SELECTION,
            Actions.CLICK_KELUAR_IN_ABANDON_PAGE,
            "${epharmacyGroupIds.joinToString(",")} - $status"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_KELUAR_IN_ABANDON_PAGE
        sendGeneralEvent(gtmData)
    }

    fun clickLanjutBayarInAbandonPage(
        epharmacyGroupIds: List<String>,
        hasAttachedPrescription: Boolean
    ) {
        val status = if (hasAttachedPrescription) {
            VALUE_PRESCRIPTION_ATTACHED
        } else {
            VALUE_NO_PRESCRIPTION_ATTACHED
        }
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.COURIER_SELECTION,
            Actions.CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE,
            "${epharmacyGroupIds.joinToString(",")} - $status"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE
        sendGeneralEvent(gtmData)
    }
}
