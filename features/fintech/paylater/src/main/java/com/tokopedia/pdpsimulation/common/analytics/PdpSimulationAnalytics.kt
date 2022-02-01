package com.tokopedia.pdpsimulation.common.analytics

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
        when (event) {
            is PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent -> sendPayLaterProductImpressionTracking(
                event.payLaterProduct,
                event.actionType,
                event.tenure
            )
            is PdpSimulationEvent.PayLater.MainBottomSheetImpression -> sendRegisterPayLaterClickEvent(
                event.payLaterProduct,
                event.tenure
            )
            is PdpSimulationEvent.PayLater.MainBottomSheetClickEvent -> sendMainBottomSheetClickEvent(
                event.payLaterProduct,
                event.tenure,
                event.url
            )
            is PdpSimulationEvent.PayLater.ClickCardButton -> sendClickCardEvent(
                event.tenure,
                event.partnerName,
                event.buttonName,
                event.redirectionUrl
            )
            is PdpSimulationEvent.PayLater.SelectedPayLater -> sendPayLaterImpressionEvent()
            is PdpSimulationEvent.PayLater.TenureListImpression -> sendSortFilterTenureImpression(
                event.tenure
            )
            is PdpSimulationEvent.PayLater.GopayBottomSheetButtonClick -> sendGopayClick(
                event.emiAmount,
                event.partnerName,
                event.productId,
                event.tenure,
                event.url
            )
            is PdpSimulationEvent.PayLater.GopayBottomSheetImpression -> sendGopayImpression(
                event.emiAmount,
                event.partnerName,
                event.productId,
                event.tenure
            )
        }
    }

    private fun sendGopayClick(
        emiAmount: String,
        partnerName: String,
        productId: String,
        tenure: String,
        url: String
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CLICK_GOPAY_BOTTOMSHEET,
            EVENT_CATEGORY_FIN_TECH,
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
            EVENT_IMPRESSION_GOPAY_BOTTOMSHEET,
            EVENT_CATEGORY_FIN_TECH,
            "$EVENT_PDP- $productId - $tenure -$partnerName -$PDP_SIMULATION_PAGE - $emiAmount"

        )
        sendGeneralEvent(map)
    }

    private fun sendSortFilterTenureImpression(tenure: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH_IMPRESSION,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_TENURE_FILTER,
            "$EVENT_LABEL_TENURE_FILTER - $tenure"
        )
        sendGeneralEvent(map)
    }

    private fun sendPayLaterImpressionEvent() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH_IMPRESSION,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_PAY_LATER_TAB_IMPRESSION,
            EVENT_LABEL_PAY_LATER_TAB_IMPRESSION
        )
        sendGeneralEvent(map)
    }

    private fun sendClickCardEvent(
        tenure: Int,
        partnerName: String,
        buttonName: String,
        redirectionUrl: String
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_CARD_BUTTON_CLICK,
            "$EVENT_LABEL_CARD_BUTTON_CLICK - $tenure - $partnerName - $buttonName - $redirectionUrl"
        )
        sendGeneralEvent(map)
    }

    private fun sendFaqClickEvent(partnerName: String, tenure: Int) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_CLICK_FAQ,
            "$EVENT_LABEL_CLICK_FAQ - $tenure - $partnerName"
        )
        sendGeneralEvent(map)
    }

    private fun sendFaqWebClickEvent(partnerName: String, tenure: Int, url: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_CLICK_FAQ_WEB,
            "$EVENT_LABEL_CLICK_FAQ_WEB - $tenure - $partnerName -$url"
        )
        sendGeneralEvent(map)
    }

    private fun sendFaqImpressionAnalytics(partnerName: String, tenure: Int) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH_IMPRESSION,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_FAQ_BOTTOMSHEET,
            "$EVENT_LABEL_FAQ_BOTTOMSHEET - $tenure - $partnerName"
        )
        sendGeneralEvent(map)
    }


    private fun sendMainBottomSheetClickEvent(payLaterProduct: String, tenure: Int, url: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_BOTTOM_ACTION_SHEET_CLICK,
            "$EVENT_LABEL_BOTTOM_ACTION_SHEET_CLICK - $tenure - $payLaterProduct - $url"
        )
        sendGeneralEvent(map)
    }

    private fun sendTenureEvent(tenureSelector: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_CLICKED_TENURE_FILTER,
            "$EVENT_LABEL_CLICKED_TENURE_FILTER - $tenureSelector"
        )
        sendGeneralEvent(map)
    }

    private fun sendTabChangeEvent(tabTitle: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_PAY_LATER_TAB_CLICK,
            "$EVENT_LABEL_PAY_LATER_TAB_CLICK - $tabTitle"
        )
        sendGeneralEvent(map)
    }


    private fun sendChoosePayLaterOptionClickEvent(payLaterProduct: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_CHOOSE_PAY_LATER_CLICK,
            "$EVENT_LABEL_PAY_LATER_PRODUCT_CLICK - ${payLaterProduct ?: ""}"
        )
        sendGeneralEvent(map)
    }

    private fun sendPayLaterProductImpressionTracking(
        payLaterProduct: String, buttonName: String?,
        tenure: Int
    ) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH_IMPRESSION,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_PAYLATER_PRODUCT_DETAIL,
            "$EVENT_LABEL_PAYLATER_PRODUCT_DETAIL - $tenure - $payLaterProduct - $buttonName"
        )
        sendGeneralEvent(map)
    }

    private fun sendRegisterPayLaterClickEvent(payLaterProduct: String?, tenure: Int) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_BOTTOM_ACTION_SHEET,
            "$EVENT_LABEL_BOTTOM_ACTION_SHEET - $tenure - ${payLaterProduct ?: ""}"
        )
        sendGeneralEvent(map)
    }

    private fun sendCCTabChangeEvent(tabTitle: String) {
        val title = tabTitle.replace("&", "dan").lowercase(Locale.getDefault())
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_CC_TAB_CLICK,
            "$EVENT_LABEL_CC - $title"
        )
        sendCCGeneralEvent(map)
    }


    private fun sendCCNotAvailableEvent() {
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_CC_NOT_AVAILABLE,
            EVENT_LABEL_CC
        )
        sendCCGeneralEvent(map)
    }

    private fun sendApplyCCEvent() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            EVENT_ACTION_CC_APPLY,
            EVENT_LABEL_CC
        )
        sendCCGeneralEvent(map)
    }

    private fun sendChooseCCEvent(action: String, labelArgument: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            action,
            "$EVENT_LABEL_CC_POPUP - $labelArgument"
        )
        sendCCGeneralEvent(map)
    }

    private fun sendSeeMoreEvent(action: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_FIN_TECH,
            EVENT_CATEGORY_FIN_TECH,
            action,
            EVENT_LABEL_CC_POPUP
        )
        sendCCGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    private fun sendCCGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_FINTECH
        map[KEY_CURRENT_SITE] = CURRENT_SITE_FINTECH
        map[KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT_FINTECH = "Fintech"
        const val CURRENT_SITE_FINTECH = "tokopediafintech"
        const val EVENT_NAME_FIN_TECH = "clickFintechMicrosite"
        const val EVENT_PDP = "pdp"
        const val PDP_SIMULATION_PAGE = "PDPSimulationPage"

        const val EVENT_NAME_FIN_TECH_IMPRESSION = "viewFintechMicrosite"
        const val IRIS_EVENT_NAME_FIN_TECH = "viewFintechMicrositeIris"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val EVENT_ACTION_PAY_LATER_TAB_CLICK = "sim vcc - click tab"
        const val EVENT_ACTION_CHOOSE_PAY_LATER_CLICK = "sim vcc - click pilih paylater"


        const val EVENT_LABEL_PAY_LATER_TAB_CLICK = "paylater simulation page"
        const val EVENT_LABEL_PAY_LATER_PRODUCT_CLICK = "daftar paylater popup"


        const val EVENT_LABEL_CLICKED_TENURE_FILTER = "pdp paylater simulation page"
        const val EVENT_CLICKED_TENURE_FILTER = "sim vcc - click paylater tenure option"

        const val EVENT_LABEL_TENURE_FILTER = "pdp paylater simulation page"
        const val EVENT_TENURE_FILTER = "sim vcc - impression paylater cicilan simulation"

        const val EVENT_LABEL_PAYLATER_PRODUCT_DETAIL = "pdp paylater simulation page"
        const val EVENT_PAYLATER_PRODUCT_DETAIL = "sim vcc - impression paylater option"

        const val EVENT_BOTTOM_ACTION_SHEET = "sim vcc - impression popup cara pakai"
        const val EVENT_LABEL_BOTTOM_ACTION_SHEET =
            "pdp paylater simulation page - popup cara pakai"


        const val EVENT_BOTTOM_ACTION_SHEET_CLICK = "sim vcc - click cari tahu lebih lanjut"
        const val EVENT_LABEL_BOTTOM_ACTION_SHEET_CLICK =
            "pdp paylater simulation page - popup cara pakai"

        const val EVENT_FAQ_BOTTOMSHEET = "sim vcc - impression popup lihat selengkapnya"
        const val EVENT_LABEL_FAQ_BOTTOMSHEET =
            "pdp paylater simulation page - popup lihat selengkapnya"

        const val EVENT_CLICK_FAQ_WEB = "sim vcc - click lihat lebih banyak"
        const val EVENT_LABEL_CLICK_FAQ_WEB =
            "pdp paylater simulation page - popup lihat selengkapnya "

        const val EVENT_CLICK_FAQ = "sim vcc - click lihat selengkapnya"
        const val EVENT_LABEL_CLICK_FAQ = "pdp paylater simulation page"

        const val EVENT_CARD_BUTTON_CLICK = "sim vcc - click card button"
        const val EVENT_LABEL_CARD_BUTTON_CLICK = "pdp paylater simulation page"

        const val EVENT_PAY_LATER_TAB_IMPRESSION =
            "sim vcc - impression pdp paylater simulation page"
        const val EVENT_LABEL_PAY_LATER_TAB_IMPRESSION = "pdp paylater simulation page"

        const val EVENT_IMPRESSION_GOPAY_BOTTOMSHEET = "sim vcc - impression sambungin akun"
        const val EVENT_CLICK_GOPAY_BOTTOMSHEET = "sim vcc - click sambungin akun"

        // Credit Card --> CC
        const val EVENT_ACTION_CC_TAB_CLICK = "sim cc - click tab"
        const val EVENT_ACTION_CC_NOT_AVAILABLE = "sim cc - impression harga tidak sesuai cc"
        const val EVENT_ACTION_CC_APPLY = "sim cc - click buat kartu kredit"
        const val EVENT_ACTION_CC_CHOOSE_BANK = "sim cc - click bank in bank popup"
        const val EVENT_ACTION_CC_SEE_MORE_BANK = "sim cc - click lihat selengkapnya in bank popup"
        const val EVENT_ACTION_CC_CHOOSE_CARD = "sim cc - click kartu in card popup"
        const val EVENT_ACTION_CC_SEE_MORE_CARD = "sim cc - click lihat selengkapnya in card popup"

        const val EVENT_LABEL_CC = "cicilan simulation page"
        const val EVENT_LABEL_CC_POPUP = "cicilan simulation bank popup"


    }

}