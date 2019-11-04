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

    fun sendAnalyticsEventToGtm(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                PRODUCT_DETAIL_PAGE,
                PRODUCT_DETAIL,
                CLICK,
                SHARE_TO + label
        ))
    }

    companion object {
        const val CLICK_APP_SHARE_REFERRAL = "clickReferral"
        const val REFERRAL = "Referral"
        const val SCREEN_NAME = "screen_name"
        const val REFERRAL_SCREEN_LAUNCHED = "Referral_Screen_Launched"
        val PRODUCT_DETAIL_PAGE = "clickPDP"
        val PRODUCT_DETAIL = "Product Detail Page"
        val CLICK = "Click"
        val SHARE_TO = "Share - "
    }
}