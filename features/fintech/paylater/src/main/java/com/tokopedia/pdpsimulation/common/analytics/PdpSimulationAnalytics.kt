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
            is PdpSimulationEvent.PayLater.TabChangeEvent -> sendTabChangeEvent(event.tabTitle)
            is PdpSimulationEvent.PayLater.ChoosePayLaterOptionClickEvent -> sendChoosePayLaterOptionClickEvent(
                event.payLaterProduct
            )
            is PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent -> sendPayLaterProductImpressionTracking(
                event.payLaterProduct,
                event.actionType,
                event.tenure
            )
            is PdpSimulationEvent.PayLater.MainBottomSheetImpression -> sendRegisterPayLaterClickEvent(
                event.payLaterProduct,
                event.tenure
            )

            is PdpSimulationEvent.CreditCard.TabChangeEvent -> sendCCTabChangeEvent(event.tabTitle)
            is PdpSimulationEvent.CreditCard.CCNotAvailableEvent -> sendCCNotAvailableEvent()
            is PdpSimulationEvent.CreditCard.ApplyCreditCardEvent -> sendApplyCCEvent()
            is PdpSimulationEvent.CreditCard.ChooseBankClickEvent -> sendChooseCCEvent(
                EVENT_ACTION_CC_CHOOSE_BANK,
                event.bankName
            )
            is PdpSimulationEvent.CreditCard.SeeMoreBankClickEvent -> sendSeeMoreEvent(
                EVENT_ACTION_CC_SEE_MORE_BANK
            )
            is PdpSimulationEvent.CreditCard.ChooseCardClickEvent -> sendChooseCCEvent(
                EVENT_ACTION_CC_CHOOSE_CARD,
                event.cardName
            )
            is PdpSimulationEvent.CreditCard.SeeMoreCardClickEvent -> sendSeeMoreEvent(
                EVENT_ACTION_CC_SEE_MORE_CARD
            )
            is PdpSimulationEvent.PayLater.TenureSortFilterClicker -> sendTenureEvent(event.tenureSelector)
        }
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
            EVENT_NAME_FIN_TECH,
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
        const val IRIS_EVENT_NAME_FIN_TECH = "viewFintechMicrositeIris"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val EVENT_ACTION_PAY_LATER_TAB_CLICK = "sim vcc - click tab"
        const val EVENT_ACTION_CHOOSE_PAY_LATER_CLICK = "sim vcc - click pilih paylater"


        const val EVENT_LABEL_PAY_LATER_TAB_CLICK = "paylater simulation page"
        const val EVENT_LABEL_PAY_LATER_PRODUCT_CLICK = "daftar paylater popup"


        const val EVENT_LABEL_CLICKED_TENURE_FILTER = "pdp paylater simulation page"
        const val EVENT_CLICKED_TENURE_FILTER = "sim vcc - click paylater tenure option"

        const val EVENT_LABEL_PAYLATER_PRODUCT_DETAIL = "pdp paylater simulation page"
        const val EVENT_PAYLATER_PRODUCT_DETAIL = "sim vcc - impression paylater option"

        const val EVENT_BOTTOM_ACTION_SHEET = "sim vcc - impression popup cara pakai"
        const val EVENT_LABEL_BOTTOM_ACTION_SHEET =
            "pdp paylater simulation page - popup cara pakai"

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