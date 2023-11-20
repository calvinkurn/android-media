package com.tokopedia.home_account.analytics

import com.tokopedia.track.builder.Tracker

object TokopediaCardAnalytics {

    private const val VALUE_ACTION_CLICK_TOKOPEDIA_CARD = "click on tokopedia card - py"
    private const val VALUE_ACTION_CLICK_LIHAT_SEMUA = "click on lihat semua - py"
    private const val VALUE_ACTION_CLICK_PAYMENT = "click payment widget on lihat semua page - py"
    private const val VALUE_ACTION_VIEW_LIHAT_SEMUA = "view lihat semua page - py"
    private const val VALUE_ACTION_VIEW_TOKOPEDIA_CARD = "view tokopedia card icon - py"
    private const val VALUE_EVENT_VIEW_IRIS = "viewAccountIris"
    private const val VALUE_EVENT_CLICK_ACCOUNT = "clickAccount"
    private const val VALUE_CATEGORY_BUYER_ACCOUNT = "akun saya pembeli"
    private const val VALUE_CURRENCY = "IDR"
    private const val KEY_CURRENCY = "currency"
    private const val KEY_USER_ID = "userId"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val VALUE_TRACKER_ID_45095 = "45095"
    private const val VALUE_TRACKER_ID_45096 = "45096"
    private const val VALUE_TRACKER_ID_45097 = "45097"
    private const val VALUE_TRACKER_ID_45098 = "45098"
    private const val VALUE_TRACKER_ID_45106 = "45106"
    private const val BUSINESS_UNIT = "user platform"
    private const val CURRENT_SITE = "tokopediamarketplace"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45095
    fun sendClickOnTokopediaCardPyEvent (eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_TOKOPEDIA_CARD)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45095)
            .setCustomProperty(KEY_USER_ID, userId)
            .setCustomProperty(KEY_CURRENCY, VALUE_CURRENCY)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45096
    fun sendClickOnLihatSemuaPyEvent (eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_LIHAT_SEMUA)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45096)
            .setCustomProperty(KEY_USER_ID, userId)
            .setCustomProperty(KEY_CURRENCY, VALUE_CURRENCY)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45097
    fun sendViewLihatSemuaPagePyEvent (eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_VIEW_IRIS)
            .setEventAction(VALUE_ACTION_VIEW_LIHAT_SEMUA)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45097)
            .setCustomProperty(KEY_USER_ID, userId)
            .setCustomProperty(KEY_CURRENCY, VALUE_CURRENCY)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45098
    fun sendClickPaymentWidgetOnLihatSemuaPagePyEvent (eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_PAYMENT)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45098)
            .setCustomProperty(KEY_USER_ID, userId)
            .setCustomProperty(KEY_CURRENCY, VALUE_CURRENCY)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45106
    fun sendViewTokopediaCardIconPyEvent (eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_VIEW_IRIS)
            .setEventAction(VALUE_ACTION_VIEW_TOKOPEDIA_CARD)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45106)
            .setCustomProperty(KEY_USER_ID, userId)
            .setCustomProperty(KEY_CURRENCY, VALUE_CURRENCY)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

}
