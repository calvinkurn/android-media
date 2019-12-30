package com.tokopedia.topads.widget.dashboard.analytic

import com.tokopedia.track.TrackApp

private val ACTION_CREDIT_HOSTORY = "click - kredit topads"

/**
 * Author errysuprayogi on 30,December,2019
 */

class TopAdsWidgetTracker() {

    companion object {
        private val EVENT_NAME = "clickHomepage"
        private val EVENT_CATEGORY = "seller app homepage"
        private val ACTION_AKTIFKAN_SEKARANG = "click - aktifkan sekarang"
        private val ACTION_LIHAT_DASHBOARD = "click - lihat dashboard"
        private val ACTION_MULAI_BERIKLAN = "click - mulai beriklan"
    }

    fun eventTopAdsCreditHistory(){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT_NAME, EVENT_CATEGORY, ACTION_CREDIT_HOSTORY, "")
    }

    fun eventTopAdsMulaiBeriklan(){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT_NAME, EVENT_CATEGORY, ACTION_MULAI_BERIKLAN, "")
    }

    fun eventTopAdsLihatDashboard(){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT_NAME, EVENT_CATEGORY, ACTION_LIHAT_DASHBOARD, "")
    }

    fun eventTopAdsAktifkan(){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT_NAME, EVENT_CATEGORY, ACTION_AKTIFKAN_SEKARANG, "")
    }

}