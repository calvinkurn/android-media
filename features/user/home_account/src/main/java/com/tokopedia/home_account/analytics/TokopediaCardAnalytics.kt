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
    private const val VALUE_LABEL_TOKOPEDIA_CARD_ACTIVE = "active"
    private const val VALUE_LABEL_TOKOPEDIA_CARD_NOT_ACTIVE = "not active"
    private const val VALUE_CATEGORY_BUYER_ACCOUNT = "akun saya pembeli"
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
    fun sendClickOnTokopediaCardPyEvent (isActive: Boolean) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_TOKOPEDIA_CARD)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(getStatusActiveTokopediaCard(isActive))
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45095)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45096
    fun sendClickOnLihatSemuaPyEvent (isActive: Boolean) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_LIHAT_SEMUA)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(getStatusActiveTokopediaCard(isActive))
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45096)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45097
    fun sendViewLihatSemuaPagePyEvent (isActive: Boolean) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_VIEW_IRIS)
            .setEventAction(VALUE_ACTION_VIEW_LIHAT_SEMUA)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(getStatusActiveTokopediaCard(isActive))
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45097)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45098
    fun sendClickPaymentWidgetOnLihatSemuaPagePyEvent (isActive: Boolean) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_CLICK_ACCOUNT)
            .setEventAction(VALUE_ACTION_CLICK_PAYMENT)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(getStatusActiveTokopediaCard(isActive))
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45098)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4063
    // Tracker ID: 45106
    fun sendViewTokopediaCardIconPyEvent (isActive: Boolean) {
        Tracker.Builder()
            .setEvent(VALUE_EVENT_VIEW_IRIS)
            .setEventAction(VALUE_ACTION_VIEW_TOKOPEDIA_CARD)
            .setEventCategory(VALUE_CATEGORY_BUYER_ACCOUNT)
            .setEventLabel(getStatusActiveTokopediaCard(isActive))
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45106)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    private fun getStatusActiveTokopediaCard(isActive: Boolean) : String {
        return if (isActive) {
            VALUE_LABEL_TOKOPEDIA_CARD_ACTIVE
        } else {
            VALUE_LABEL_TOKOPEDIA_CARD_NOT_ACTIVE
        }
    }

}
