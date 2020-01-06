package com.tokopedia.referral.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class ReferralAnalytics {

    fun eventReferralAndShare(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        CLICK_APP_SHARE_REFERRAL,
                        REFERRAL,
                        action,
                        label
                )
        )
    }

    fun sendMoEngageReferralScreenOpen(screenName: String) {
        val value = DataLayer.mapOf(
                SCREEN_NAME, screenName
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, REFERRAL_SCREEN_LAUNCHED)
    }

    fun sendAnalyticsShareEventToGtm(label: String) {
        var gtmMap : HashMap<String,Any>  = TrackAppUtils.gtmData(
                CLICK_APP_SHARE_REFERRAL,
                REFERRAL,
                SELECT_CHANNEL,
                "Android") as HashMap<String, Any>
        gtmMap.put("channel",label)
        gtmMap.put("source","referral")
        TrackApp.getInstance().gtm.sendGeneralEvent(gtmMap)
    }

    companion object {
        const val CLICK_APP_SHARE_REFERRAL = "clickReferral"
        const val REFERRAL = "Referral"
        const val SCREEN_NAME = "screen_name"
        const val REFERRAL_SCREEN_LAUNCHED = "Referral_Screen_Launched"
        const val SELECT_CHANNEL="select channel"
        val CLICK = "Click"
        val SHARE_TO = "Share - "
    }
}