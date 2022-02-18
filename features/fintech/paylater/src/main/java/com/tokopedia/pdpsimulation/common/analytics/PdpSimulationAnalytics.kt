package com.tokopedia.pdpsimulation.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PdpSimulationAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSessionInterface>,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendPayLaterSimulationEvent(event: PayLaterAnalyticsBase) {
        when(event) {
            is PayLaterCtaClick -> sendClickCtaEvent(event)
            else -> sendPayLaterImpressionEvent(event)
        }
    }

    private fun sendPayLaterImpressionEvent(event: PayLaterAnalyticsBase) {
        val label = computeLabel(event.productId, event.linkingStatus, event.userStatus,
            event.tenureOption.toString(), event.payLaterPartnerName)
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendClickCtaEvent(event: PayLaterCtaClick) {
        val label = computeLabel(event.productId, event.linkingStatus, event.userStatus, event.tenureOption.toString(),
            event.emiAmount, event.limit, event.redirectLink, event.payLaterPartnerName, event.ctaWording)
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_FINTECH
        map[KEY_CURRENT_SITE] = CURRENT_SITE_FINTECH
        analyticTracker.sendGeneralEvent(map)
    }

    private fun computeLabel(vararg args: String) = args.filter { it.isNotEmpty() }.joinToString(" - ")

    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT_FINTECH = "fintechPayLater"
        const val CURRENT_SITE_FINTECH = "TokopediaFintech"
        const val EVENT_NAME_FIN_TECH = "clickFintechMicrosite"
        const val IRIS_EVENT_NAME_FIN_TECH_V3 = "viewFintechIris"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val CLICK_CTA_PARTNER_CARD = "bnpl partner card CTA - click"
        const val IMPRESSION_PAYLATER = "sim bnpl - impression status buyers"

    }

}