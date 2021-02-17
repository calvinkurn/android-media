package com.tokopedia.pdpsimulation.common.analytics

import com.tokopedia.pdpsimulation.common.helper.PaymentMode
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class PdpSimulationAnalytics @Inject constructor(
        private val userSession: dagger.Lazy<UserSessionInterface>,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendPdpSimulationEvent(event: PdpSimulationEvent) {
        when(event) {
            is PdpSimulationEvent.PayLater.TabChangeEvent -> sendTabChangeEvent(event.mode, event.tabTitle)
            is PdpSimulationEvent.PayLater.RegisterWidgetClickEvent -> sendRegisterWidgetClickEvent()
            is PdpSimulationEvent.PayLater.ChoosePayLaterOptionClickEvent -> sendChoosePayLaterOptionClickEvent(event.payLaterProduct)
            is PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent -> sendPayLaterProductImpressionTracking(event.payLaterProduct, event.actionType)
            is PdpSimulationEvent.PayLater.RegisterPayLaterOptionClickEvent -> sendRegisterPayLaterClickEvent(event.payLaterProduct)
        }
    }

    private fun sendTabChangeEvent(mode: PaymentMode, tabTitle: String) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_PAY_LATER_TAB_CLICK,
            "$EVENT_LABEL_PAY_LATER_TAB_CLICK - $tabTitle")
        sendGeneralEvent(map)
    }

    private fun sendRegisterWidgetClickEvent() {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_REGISTER_CLICK,
                EVENT_LABEL_PAY_LATER_TAB_CLICK)
        sendGeneralEvent(map)
    }

    private fun sendChoosePayLaterOptionClickEvent(payLaterProduct: String?) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_CHOOSE_PAY_LATER_CLICK,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_CLICK - ${payLaterProduct ?: ""}")
        sendGeneralEvent(map)
    }

    private fun sendPayLaterProductImpressionTracking(payLaterProduct: String, actionType: String?) {
        val postfixLabel = if(actionType != null) "$payLaterProduct - ${actionType.toLowerCase()}" else payLaterProduct

        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_IMPRESSION,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK - $postfixLabel")
        sendGeneralEvent(map)
    }

    private fun sendRegisterPayLaterClickEvent(payLaterProduct: String?) {
        val map = TrackAppUtils.gtmData(EVENT_NAME_FIN_TECH,
                EVENT_CATEGORY_FIN_TECH,
                EVENT_ACTION_PAY_LATER_SIGN_UP_NOW_CLICK,
                "$EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK - ${payLaterProduct ?: ""}")
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
        const val EVENT_LABEL_PAY_LATER_PRODUCT_CLICK = "daftar paylater popup"
        const val EVENT_LABEL_PAY_LATER_PRODUCT_INFO_CLICK = "paylater info page"

        const val PAY_LATER_REGISTER_ACTION = "info cara daftar"
        const val PAY_LATER_USAGE_ACTION = "info cara gunakan"


    }

}