package com.tokopedia.pdpsimulation.common.analytics

import com.tokopedia.pdpsimulation.common.helper.PaymentMode
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PdpSimulationAnalytics @Inject constructor(
        val userSession: dagger.Lazy<UserSessionInterface>,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendTabChangeEvent(mode: PaymentMode, tabTitle: String) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_PAY_LATER_TAB_CLICK,
            "$EVENT_LABEL_PAY_LATER_TAB_CLICK - $tabTitle")
        sendGeneralEvent(map)
    }

    fun sendRegisterClickEvent() {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_REGISTER_CLICK,
                EVENT_ACTION_PAY_LATER_TAB_CLICK)
        sendGeneralEvent(map)
    }

    fun sendChoosePayLaterOptionClickEvent(payLaterProduct: String) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_CHOOSE_PAY_LATER_CLICK,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_CLICK - $payLaterProduct")
        sendGeneralEvent(map)
    }

    fun sendPayLaterImpressionTracking(payLaterProduct: String, applicationStatus: String) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_IMPRESSION,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK - $payLaterProduct - $applicationStatus")
        sendGeneralEvent(map)
    }

    fun sendDaftarSekarangClickEvent(payLaterProduct: String) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_SIGN_UP_NOW_CLICK,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK - $payLaterProduct")
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val KEY_USER_ID = "userId"
        const val EVENT_NAME_FIN_TECH = "clickFintechMicrosite"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val EVENT_ACTION_PAY_LATER_TAB_CLICK = "sim vcc - click tab"
        const val EVENT_ACTION_PAY_LATER_REGISTER_CLICK = "sim vcc - click daftar paylater"
        const val EVENT_ACTION_CHOOSE_PAY_LATER_CLICK = "sim vcc - click pilih paylater"
        const val EVENT_ACTION_PAY_LATER_SIGN_UP_NOW_CLICK = "sim vcc - click daftar sekarang"
        const val EVENT_ACTION_PAY_LATER_IMPRESSION = "sim vcc - impression paylater info page"

        const val EVENT_LABEL_PAY_LATER_TAB_CLICK = "paylater simulation page"
        const val EVENT_LABEL_PAY_LATER_PRODUCT_CLICK = "daftar paylater product"
        const val EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK = "paylater info page"


    }

}