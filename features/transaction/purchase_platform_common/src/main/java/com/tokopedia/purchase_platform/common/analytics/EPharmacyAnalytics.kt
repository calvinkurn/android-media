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
        }
    }

    interface Category {
        companion object {
            const val EPharmacyCategory = "courier selection"
        }
    }

    interface Actions {
        companion object {
            const val CLICK_UPLOAD_PRESCRIPTION_WIDGET = "click upload prescription widget"
            const val OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON = "epharmacy checkout page abandon"
            const val CLICK_KELUAR_IN_ABANDON_PAGE = "click keluar in abandon page"
            const val CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE = "click lanjut bayar in abandon page"
        }
    }

    interface TrackerId {
        companion object {
            const val CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID = "33117"
            const val OPEN_SCREEN_EPHARMACY_CHECKOUT_PAGE_ABANDON = "38029"
            const val CLICK_KELUAR_IN_ABANDON_PAGE = "38030"
            const val CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE = "38031"
        }
    }

    fun sendPrescriptionWidgetClick(cartId: String) {
        val gtmData = getGtmData(
            Events.CLICK_PP,
            Category.EPharmacyCategory,
            Actions.CLICK_UPLOAD_PRESCRIPTION_WIDGET,
            "cart_id: $cartId"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID
        gtmData[KEY_USER_ID] = userId
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
            Category.EPharmacyCategory,
            Actions.CLICK_KELUAR_IN_ABANDON_PAGE,
            "${epharmacyGroupIds.joinToString(",")} - $status"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_KELUAR_IN_ABANDON_PAGE
        gtmData[KEY_USER_ID] = userId
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
            Category.EPharmacyCategory,
            Actions.CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE,
            "${epharmacyGroupIds.joinToString(",")} - $status"
        )
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_LANJUT_BAYAR_IN_ABANDON_PAGE
        gtmData[KEY_USER_ID] = userId
        sendGeneralEvent(gtmData)
    }
}
