package com.tokopedia.pdpsimulation.paylater.helper

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pdpsimulation.common.analytics.PayLaterBottomSheetImpression
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.Cta
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet

object ActionHandler {

    private const val TYPE_APP_LINK = 1
    private const val TYPE_WEB_VIEW = 2
    private const val TYPE_HOW_TO_USE = 3
    private const val TYPE_HOW_TO_USE_II = 4

    fun handleClickNavigation(
        context: Context?,
        detail: Detail,
        productId: String,
        openHowToUse: (Bundle) -> Unit,
        openGoPay: (Bundle) -> Unit
    ) {
        when (detail.cta.cta_type) {
            TYPE_APP_LINK -> routeToAppLink(context, detail.cta.android_url)
            TYPE_WEB_VIEW -> {
                if (shouldShowGoPayBottomSheet(detail))
                    openGoPay(getGoPayBundle(productId, detail))
                else routeToWebView(context, detail.cta.android_url)
            }
            TYPE_HOW_TO_USE, TYPE_HOW_TO_USE_II -> {
                if (detail.gatewayDetail?.how_toUse != null) {
                    openHowToUse(getHowToUseBundle(detail))
                }
            }
        }
    }

    private fun routeToAppLink(context: Context?, appLink: String?) {
        if (!appLink.isNullOrEmpty())
            try {
                RouteManager.route(context, appLink)
            } catch (e: Exception) {
            }
    }

    private fun routeToWebView(context: Context?, webLink: String?) {
        if (!webLink.isNullOrEmpty())
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
    }

    private fun shouldShowGoPayBottomSheet(detail: Detail) = !detail.cta.android_url.isNullOrEmpty()
            && detail.cta.bottomSheet != null && detail.cta.bottomSheet.isShow == true

    private fun getHowToUseBundle(detail: Detail) = Bundle().apply {
        putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, detail)
    }

    private fun getGoPayBundle(productId: String, detail: Detail) =
        Bundle().apply {
            putParcelable(PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL, detail.cta)
            putString(PayLaterTokopediaGopayBottomsheet.PARTER_NAME, detail.gatewayDetail?.name?:"")
            putInt(PayLaterTokopediaGopayBottomsheet.TENURE, detail.tenure?:0)
            putString(PayLaterTokopediaGopayBottomsheet.PRODUCT_ID, productId)
            putInt(PayLaterTokopediaGopayBottomsheet.EMI_AMOUNT, detail.installment_per_month_ceil ?:0)
        }

    fun getInstallmentBundle(detail: Detail): Bundle {
        val eventImpression = PayLaterBottomSheetImpression().apply {
            tenureOption = detail.tenure?:0
            payLaterPartnerName = detail.gatewayDetail?.name ?:""
            action = PdpSimulationAnalytics.IMPRESSION_BOTTOMSHEET
            emiAmount = detail.installment_per_month_ceil?.toString() ?:""
            // TODO userStatus, limit and redirectLink
        }
        return Bundle().apply {
            putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, detail.installementDetails)
            putParcelable(PayLaterInstallmentFeeInfo.IMPRESSION_DETAIL, eventImpression)
        }
    }

}