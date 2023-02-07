package com.tokopedia.pdpsimulation.common.analytics

import android.annotation.SuppressLint
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
        when (event) {
            is PayLaterCtaClick -> sendClickCtaEvent(event)
            is PayLaterBottomSheetImpression -> sendInstallmentBottomSheet(event)
            is PayLaterTenureClick -> sendPayLaterTenureClick(event)
            is OccBottomSheetImpression -> sendOccInstallmentBottomSheet(event)
            else -> sendPayLaterImpressionEvent(event)
        }
    }

    private fun sendOccInstallmentBottomSheet(event: OccBottomSheetImpression) {
        val label = computeLabel(
            event.productId,
            "LINKED",
            event.userStatus,
            event.productPrice,
            event.tenureOption.toString(),
            event.payLaterPartnerName,
        )
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            OCC_INSTALMENT_BOTTOMSHEET_ACTION,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendPayLaterTenureClick(event: PayLaterTenureClick) {
        val label = computeLabel(
            event.productId,
            event.linkingStatus,
            event.userStatus,
            event.productPrice,
            event.tenureOption.toString(),
            event.payLaterPartnerName,
            event.tenureOptionText,
        )
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            SIMULATION_TENURE_CLICK_ACTION,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendInstallmentBottomSheet(event: PayLaterBottomSheetImpression) {

        val label = computeLabel(
            event.productId,
            event.userStatus,
            event.tenureOption.toString(),
            event.emiAmount,
            event.limit,
            event.payLaterPartnerName,
        )
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)

    }

    private fun sendPayLaterImpressionEvent(event: PayLaterAnalyticsBase) {
        val label = computeLabel(
            event.productId, event.linkingStatus, event.userStatus,
            event.tenureOption.toString(), event.payLaterPartnerName, event.promoName
        )
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            EVENT_CATEGORY_FIN_TECH,
            event.action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendClickCtaEvent(event: PayLaterCtaClick) {
        val label = computeLabel(
            event.productId,
            event.linkingStatus,
            event.userStatus,
            event.tenureOption.toString(),
            event.emiAmount,
            event.limit,
            event.redirectLink,
            event.payLaterPartnerName,
            event.ctaWording,
            event.promoName,
        )
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

    private fun computeLabel(vararg args: String) =
        args.filter { it.isNotEmpty() }.joinToString(" - ")

    fun sendOccEvent(pdpSimulationEvent: PdpSimulationEvent) {
        when (pdpSimulationEvent) {


            is PdpSimulationEvent.OccImpressionEvent -> {
                sendOccImpressionEvent(
                    pdpSimulationEvent.productId,
                    pdpSimulationEvent.userStatus,
                    pdpSimulationEvent.partnerName,
                    pdpSimulationEvent.emiAmount,
                    pdpSimulationEvent.tenure,
                    pdpSimulationEvent.quantity,
                    pdpSimulationEvent.limit,
                    pdpSimulationEvent.variantName,
                )
            }

            is PdpSimulationEvent.ClickChangePartnerEvent -> sendChangePartnerCLickEvent(
                pdpSimulationEvent.productId,
                pdpSimulationEvent.userStatus,
                pdpSimulationEvent.partnerName,
                pdpSimulationEvent.emiAmount,
                pdpSimulationEvent.tenure,
                pdpSimulationEvent.quantity,
                pdpSimulationEvent.limit,
                pdpSimulationEvent.variantName,
            )
            is PdpSimulationEvent.OccChangeVariantClicked -> sendOccChangeVariantEvent(
                pdpSimulationEvent.productId,
                pdpSimulationEvent.userStatus,
                pdpSimulationEvent.partnerName,
                pdpSimulationEvent.emiAmount,
                pdpSimulationEvent.tenure,
                pdpSimulationEvent.quantity,
                pdpSimulationEvent.limit,
                pdpSimulationEvent.variantName,
            )
            is PdpSimulationEvent.OccChangePartnerClicked -> sendOccChangePartnerEvent(
                pdpSimulationEvent.productId,
                pdpSimulationEvent.userStatus,
                pdpSimulationEvent.partnerName,
                pdpSimulationEvent.emiAmount,
                pdpSimulationEvent.tenure,
                pdpSimulationEvent.quantity,
                pdpSimulationEvent.limit,
                pdpSimulationEvent.variantName,
            )

            is PdpSimulationEvent.ClickCTACheckoutPage -> sendCTACheckoutClicked(
                pdpSimulationEvent.productId,
                pdpSimulationEvent.userStatus,
                pdpSimulationEvent.partnerName,
                pdpSimulationEvent.emiAmount,
                pdpSimulationEvent.tenure,
                pdpSimulationEvent.quantity,
                pdpSimulationEvent.limit,
                pdpSimulationEvent.variantName,
            )
            is PdpSimulationEvent.ClickTenureEvent -> sendTenureClickAnalytic(
                pdpSimulationEvent.productId,
                pdpSimulationEvent.userStatus,
                pdpSimulationEvent.productPrice,
                pdpSimulationEvent.tenure,
                pdpSimulationEvent.partnerName,
                pdpSimulationEvent.promoName,
            )
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun sendTenureClickAnalytic(
        productId: String,
        userStatus: String,
        productPrice: String,
        tenure: String,
        partnerName: String,
        promoName: String,
    ) {
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            OCC_TENURE_OPTION_CLICK,
            computeLabel(
                productId,
                "LINKED",
                userStatus,
                productPrice,
                tenure,
                partnerName,
                promoName
            )
        )
        sendGeneralEvent(map)
    }

    private fun sendCTACheckoutClicked(
        productId: String,
        userStatus: String,
        partnerName: String,
        emiAmount: String,
        tenure: String,
        quantity: String,
        limit: String,
        variantName: String
    ) {
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            CTA_CHECKOUT_EVENT_CATEGORY,
            CTA_CHECKOUT_CLICKED_ACTION,
            computeLabel(
                productId,
                userStatus,
                "LINKED",
                partnerName,
                emiAmount,
                tenure,
                quantity,
                limit,
                variantName
            )
        )
        sendGeneralEvent(map)
    }

    private fun sendOccChangePartnerEvent(
        productId: String,
        userStatus: String,
        partnerName: String,
        emiAmount: String,
        tenure: String,
        quantity: String,
        limit: String,
        variantName: String
    ) {
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            OCC_CHANGE_PARTNER_ACTION,
            " $productId -$userStatus -LINKED - $partnerName - $emiAmount - $tenure - $quantity - $limit - $variantName"

        )
        sendGeneralEvent(map)
    }

    private fun sendChangePartnerCLickEvent(
        productId: String,
        userStatus: String,
        partnerName: String,
        emiAmount: String,
        tenure: String,
        quantity: String,
        limit: String,
        variantName: String
    ) {
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            BOTTOMSHEET_CHANGE_PARTNER_CLICK_ACTION,
            " $productId -$userStatus - LINKED - $partnerName - $emiAmount - $tenure - $quantity - $limit - $variantName"

        )
        sendGeneralEvent(map)
    }


    private fun sendOccChangeVariantEvent(
        productId: String,
        userStatus: String,
        partnerName: String,
        emiAmount: String,
        tenure: String,
        quantity: String,
        limit: String,
        variantName: String
    ) {
        val map = TrackAppUtils.gtmData(
            CLICK_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            OCC_CHANGE_VARIANT_ACTION,
            " $productId - $userStatus - LINKED - $partnerName - $emiAmount - $tenure - $quantity - $limit - $variantName"

        )
        sendGeneralEvent(map)

    }

    private fun sendOccImpressionEvent(
        productId: String,
        userStatus: String,
        partnerName: String,
        emiAmount: String,
        tenure: String,
        quantity: String,
        limit: String,
        variantName: String
    ) {
        val map = TrackAppUtils.gtmData(
            IRIS_EVENT_NAME_FIN_TECH_V3,
            OCC_EVENT_CATEGORY,
            OCC_EVENT_ACTION,
            " $productId -$userStatus - LINKED - $partnerName - $emiAmount - $tenure - $quantity - $limit - $variantName"

        )
        sendGeneralEvent(map)
    }

    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT_FINTECH = "fintechPaylater"
        const val CURRENT_SITE_FINTECH = "TokopediaFintech"
        const val EVENT_NAME_FIN_TECH = "clickFintech"
        const val IRIS_EVENT_NAME_FIN_TECH_V3 = "viewFintechIris"
        const val CLICK_EVENT_NAME_FIN_TECH_V3 = "clickFintech"
        const val EVENT_CATEGORY_FIN_TECH = "fin - info page"

        const val CLICK_CTA_PARTNER_CARD = "sim bnpl - click partner card CTA"
        const val IMPRESSION_PAYLATER = "sim bnpl - impression status buyers"

        const val OCC_EVENT_ACTION = "pre occ page - impression activated buyer"
        const val OCC_CHANGE_VARIANT_ACTION = "variant cta - clicked activated buyer"
        const val OCC_CHANGE_PARTNER_ACTION = "change partner cta - clicked activated buyer"
        const val BOTTOMSHEET_CHANGE_PARTNER_CLICK_ACTION =
            "partner option cta - click activated buyer"
        const val BOTTOMSHEET_INSTALLMENT_ACTION = "open simulation bottom sheet - impression"
        const val OCC_EVENT_CATEGORY = "fin - optimize occ page"
        const val CTA_CHECKOUT_CLICKED_ACTION = "CTA beli langsung - click activated buyers"
        const val CTA_CHECKOUT_EVENT_CATEGORY = "fin - occ page"
        const val OCC_TENURE_OPTION_CLICK = "tenure option - click"
        const val SIMULATION_TENURE_CLICK_ACTION = "sim bnpl - tenure option click"
        const val OCC_INSTALMENT_BOTTOMSHEET_ACTION = "view break up - impression"
    }

}
