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
            is PayLaterBottomSheetImpression -> sendBottomSheetImpressionEvent(event)
            is PayLaterProductImpressionEvent -> sendPayLaterProductImpressionTracking(event)
            else -> sendPayLaterImpressionEvent(event)
        }
    }

    fun sendGoPayBottomSheetEvent(event: PdpSimulationEvent) {
        when (event) {
            is PdpSimulationEvent.PayLater.GopayBottomSheetButtonClick ->
                sendGopayClick(event.emiAmount, event.partnerName, event.productId, event.tenure, event.url)
            is PdpSimulationEvent.PayLater.GopayBottomSheetImpression ->
                sendGopayImpression(event.emiAmount, event.partnerName, event.productId, event.tenure)
        }
    }

    private fun sendGopayClick(emiAmount: String, partnerName: String, productId: String, tenure: String, url: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_CLICK_GOPAY_BOTTOMSHEET,
            "$EVENT_PDP- $productId - $tenure -$partnerName -$url - $emiAmount"

        )
        sendGeneralEvent(map)
    }

    private fun sendGopayImpression(
        emiAmount: String,
        partnerName: String,
        productId: String,
        tenure: String
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH_IMPRESSION,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_IMPRESSION_GOPAY_BOTTOMSHEET,
            "$EVENT_PDP- $productId - $tenure -$partnerName -$PDP_SIMULATION_PAGE - $emiAmount"

        )
        sendGeneralEvent(map)
    }

    private fun sendPayLaterImpressionEvent(event: PayLaterAnalyticsBase) {
        val label = computeLabel(event.productId, event.userStatus,
            event.tenureOption.toString())
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendClickCtaEvent(event: PayLaterCtaClick) {
        val label = computeLabel(event.productId, event.userStatus, event.tenureOption.toString(),
            event.emiAmount, event.limit, event.redirectLink, event.payLaterPartnerName, event.ctaWording)
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendBottomSheetImpressionEvent(event: PayLaterBottomSheetImpression) {
        val label = computeLabel(event.productId, event.userStatus, event.tenureOption.toString(),
            event.emiAmount, event.limit, event.redirectLink, event.payLaterPartnerName)
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendPayLaterProductImpressionTracking(event: PayLaterProductImpressionEvent) {
        val label = computeLabel(event.productId, event.userStatus, event.tenureOption.toString(),
            event.emiAmount, event.payLaterPartnerName)
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
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
        const val EVENT_PDP = "pdp"
        const val PDP_SIMULATION_PAGE = "PDPSimulationPage"

        const val EVENT_NAME_FIN_TECH_IMPRESSION = "viewFintechMicrosite"
        const val IRIS_EVENT_NAME_FIN_TECH_V3 = "viewFintechIris"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val CLICK_CTA_HOW_TO_USE = "sim bnpl - cari tahu lebih lanjut click"
        const val IMPRESSION_HOW_TO_USE = "sim bnpl - bottom sheet how to use impression"
        const val IMPRESSION_BOTTOMSHEET = "sim bnpl - bottom sheet impression"
        const val CLICK_INSTALLMENT_INFO = "gopaylater cicil CTA “i” - click"
        const val CLICK_CTA_PARTNER_CARD = "bnpl partner card CTA - click"
        const val IMPRESSION_PARTNER_CARD = "sim bnpl - impression status GPL Cicil card"
        const val IMPRESSION_PAYLATER = "sim bnpl - impression status buyers"

        const val EVENT_IMPRESSION_GOPAY_BOTTOMSHEET = "sim vcc - impression sambungin akun"
        const val EVENT_CLICK_GOPAY_BOTTOMSHEET = "sim vcc - click sambungin akun"
    }

}