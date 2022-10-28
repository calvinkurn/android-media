package com.tokopedia.loginregister.goto_seamless.trackers

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object GotoSeamlessTracker {

    fun viewGotoSeamlessPage(label: String) {
        val data = TrackAppUtils.gtmData(
            Event.VIEW_ACC_IRIS,
            Category.SEAMLESS_LOGIN_PAGE,
            Action.VIEW_GOTO_SEAMLESS_PAGE,
            label
        )
        track(data, trackerId = "33190")
    }

    fun clickOnSeamlessButton(label: String, variant: String) {
        val finalLabel = if(variant.isNotEmpty()) {
            "$label - $variant"
        } else { label }
        val data = TrackAppUtils.gtmData(
            Event.CLICK_ACCOUNT,
            Category.SEAMLESS_LOGIN_PAGE,
            Action.CLICK_LOGIN_WITH_SEAMLESS,
            finalLabel
        )
        track(data, trackerId = "33191")
    }

    fun clickOnMasukAkunLain(label: String) {
        val data = TrackAppUtils.gtmData(
            Event.CLICK_ACCOUNT,
            Category.SEAMLESS_LOGIN_PAGE,
            Action.CLICK_LOGIN_WITH_OTHER_ACC,
            label
        )
        track(data, trackerId = "33192")
    }

    fun clickOnBackBtn(label: String) {
        val data = TrackAppUtils.gtmData(
            Event.CLICK_ACCOUNT,
            Category.SEAMLESS_LOGIN_PAGE,
            Action.CLICK_BACK_BTN,
            label
        )
        track(data, trackerId = "33193")
    }

    private fun track(map: MutableMap<String, Any>, trackerId: String) {
        map[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        map[KEY_TRACKER_ID] = trackerId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_TRACKER_ID = "trackerId"

    private const val BUSSINESS_UNIT = "user platform"
    private const val CURRENT_SITE = "tokopediamarketplace"


    object Event {
        const val VIEW_ACC_IRIS = "viewAccountIris"
        const val CLICK_ACCOUNT = "clickAccount"
    }

    object Category {
        const val SEAMLESS_LOGIN_PAGE = "goto seamless login page"
    }

    object Action {
        const val VIEW_GOTO_SEAMLESS_PAGE = "view goto seamless login page"
        const val CLICK_LOGIN_WITH_SEAMLESS = "click on login with goto seamless"
        const val CLICK_LOGIN_WITH_OTHER_ACC = "click on masuk ke akun lain"
        const val CLICK_BACK_BTN = "click on back button"
    }

    object Label {
        const val CLICK = "click"
        const val SUCCESS = "success"
        const val FAILED = "failed"
    }

}
