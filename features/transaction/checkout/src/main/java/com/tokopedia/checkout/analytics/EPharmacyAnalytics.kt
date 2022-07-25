package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class EPharmacyAnalytics constructor(val userId: String, val isLoggedIn : Boolean) : TransactionAnalytics() {
    companion object {
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val VALUE_BUSINESS_UNIT = "Physical Goods"
        const val KEY_CURRENT_SITE = "currentSite"
        const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        const val KEY_USER_ID = "userId"
        const val KEY_TRACKER_ID = "trackerId"
        const val KEY_IS_LOGGED_IN = "isLoggedInStatus"
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
        }
    }

    interface TrackerId {
        companion object {
            const val CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID = "33117"
        }
    }

    fun sendPrescriptionWidgetClick(cartId : String) {
        val gtmData = getGtmData(
            Events.CLICK_PP,Category.EPharmacyCategory,
            Actions.CLICK_UPLOAD_PRESCRIPTION_WIDGET,"cart_id: $cartId")
        gtmData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
        gtmData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE
        gtmData[KEY_TRACKER_ID] = TrackerId.CLICK_UPLOAD_PRESCRIPTION_TRACKER_ID
        gtmData[KEY_USER_ID] = userId
        gtmData[KEY_IS_LOGGED_IN] = isLoggedIn
        sendGeneralEvent(gtmData)
    }
}