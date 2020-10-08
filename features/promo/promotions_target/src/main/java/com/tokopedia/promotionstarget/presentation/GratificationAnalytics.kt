package com.tokopedia.promotionstarget.presentation

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics


const val KEY_CURRENT_SITE = "currentSite"
const val KEY_SCREEN_NAME = "screenName"
const val KEY_BUSINESS_UNIT = "businessUnit"

const val TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
const val PROMO = "promo"

object GratifEvents {
    const val CLICK_GRATIF = "clickGratification"
}

object GratifCategory {
    const val BOTTOM_SHEET_GRATIFICATION = "bottom sheet gratification"
}

object GratifActions {
    const val CLICK_YUK_BELANJA_HEMAT = "click yuk belanja hemat"
    const val CLICK_DISMISS_POP_UP = "click dismiss pop up"
    const val CLICK_CEK_KUPON_SAYA = "click cek kupon saya"
    const val CLICK_LANJUT_BERBELANJA = "click lanjut berbelanja"
}

object GratificationAnalytics {


    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3
    fun userClickMainCtaThankYou() {}

    //4
    fun userDismissPopUpThankYou() {}

    //5,7,10,12,14
    fun userClickMainCtaPush(userId: String,
                             entryPoint: Int,
                             popupType: String,
                             baseCode: String,
                             eventId: String,
                             screenName:String,
                             isUsedOrExpired:Boolean
    ) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = GratifEvents.CLICK_GRATIF
        map[KEY_EVENT_CATEGORY] = GratifCategory.BOTTOM_SHEET_GRATIFICATION

        if(isUsedOrExpired){
            map[KEY_EVENT_ACTION] = GratifActions.CLICK_LANJUT_BERBELANJA
        }else{
            map[KEY_EVENT_ACTION] = GratifActions.CLICK_YUK_BELANJA_HEMAT
        }

        map[KEY_EVENT_LABEL] = "$entryPoint - $popupType - $baseCode - $eventId"
        map[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_SCREEN_NAME] = screenName
        map[KEY_USER_ID] = userId
        map[KEY_BUSINESS_UNIT] = PROMO
    }

    //6,9,11,15
    fun userDismissPopup(userId: String, entryPoint: Int, popupType: String, baseCode: String, eventId: String, screenName:String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = GratifEvents.CLICK_GRATIF
        map[KEY_EVENT_CATEGORY] = GratifCategory.BOTTOM_SHEET_GRATIFICATION
        map[KEY_EVENT_ACTION] = GratifActions.CLICK_DISMISS_POP_UP
        map[KEY_EVENT_LABEL] = "$entryPoint - $popupType - $baseCode - $eventId"
        map[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_SCREEN_NAME] = screenName
        map[KEY_USER_ID] = userId
        map[KEY_BUSINESS_UNIT] = PROMO
    }

    //8
    fun userClickSecondaryCtaPush(userId: String, entryPoint: Int, popupType: String, baseCode: String, eventId: String, screenName:String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = GratifEvents.CLICK_GRATIF
        map[KEY_EVENT_CATEGORY] = GratifCategory.BOTTOM_SHEET_GRATIFICATION
        map[KEY_EVENT_ACTION] = GratifActions.CLICK_CEK_KUPON_SAYA
        map[KEY_EVENT_LABEL] = "$entryPoint - $popupType - $baseCode - $eventId"
        map[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_SCREEN_NAME] = screenName
        map[KEY_USER_ID] = userId
        map[KEY_BUSINESS_UNIT] = PROMO
    }
}