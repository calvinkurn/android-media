package com.tokopedia.home_account.explicitprofile.personalize

import com.tokopedia.track.builder.Tracker

object ExplicitPersonalizeAnalytics {

    private const val ACTION_CLICK_ON_NANTI_SAJA = "click on nanti saja"
    private const val ACTION_CLICK_ON_SIMPAN = "click on simpan"
    private const val ACTION_VIEW_ON_LOADING_SIMPAN_PILIHAN = "view on loading simpan pilihan"
    private const val ACTION_VIEW_ON_EXPLICIT_PAGE = "view on explicit page"
    private const val CATEGORY_EXPLICIT_PERSONALIZE = "explicit page - new register"
    private const val EVENT_CLICK_ACCOUNT = "clickAccount"
    private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val VALUE_TRACKER_ID_50392 = "50392"
    private const val VALUE_TRACKER_ID_50393 = "50393"
    private const val VALUE_TRACKER_ID_50394 = "50394"
    private const val VALUE_TRACKER_ID_50395 = "50395"
    private const val EMPTY_STRING = ""
    private const val VALUE_BUSINESS_UNIT = "user platform"
    private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4536
    // Tracker ID: 50392
    fun sendClickOnNantiSajaEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_NANTI_SAJA)
            .setEventCategory(CATEGORY_EXPLICIT_PERSONALIZE)
            .setEventLabel(EMPTY_STRING)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_50392)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4536
    // Tracker ID: 50393
    fun sendClickOnSimpanEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_SIMPAN)
            .setEventCategory(CATEGORY_EXPLICIT_PERSONALIZE)
            .setEventLabel(EMPTY_STRING)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_50393)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4536
    // Tracker ID: 50394
    fun sendViewOnLoadingSimpanPilihanEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_ON_LOADING_SIMPAN_PILIHAN)
            .setEventCategory(CATEGORY_EXPLICIT_PERSONALIZE)
            .setEventLabel(EMPTY_STRING)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_50394)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4536
    // Tracker ID: 50395
    fun sendViewOnExplicitPageEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_ON_EXPLICIT_PAGE)
            .setEventCategory(CATEGORY_EXPLICIT_PERSONALIZE)
            .setEventLabel(EMPTY_STRING)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_50395)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }
}
