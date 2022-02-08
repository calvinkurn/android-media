package com.tokopedia.pdpsimulation.paylater.helper

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PayLaterBottomSheetImpression
import com.tokopedia.pdpsimulation.common.analytics.PayLaterCtaClick
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet

object PayLaterBundleGenerator {

    fun getHowToUseBundle(detail: Detail): Bundle {
        val clickEvent = PayLaterCtaClick().apply {
            productId = ""
            userStatus = detail.userState ?: ""
            tenureOption = detail.tenure ?: 0
            payLaterPartnerName = detail.gatewayDetail?.name ?: ""
            action = PdpSimulationAnalytics.CLICK_CTA_HOW_TO_USE
            emiAmount = detail.installment_per_month_ceil?.toString() ?: ""
            limit = detail.limit?.toString() ?: ""
            redirectLink = detail.cta.android_url ?: " "
            ctaWording = "Cari Tahu Lebih Lanjut"
        }
        val impression = PayLaterBottomSheetImpression().apply {
            productId = clickEvent.productId
            userStatus = clickEvent.userStatus
            tenureOption = clickEvent.tenureOption
            payLaterPartnerName = clickEvent.payLaterPartnerName
            action = PdpSimulationAnalytics.IMPRESSION_HOW_TO_USE
            emiAmount = clickEvent.emiAmount
            limit = clickEvent.limit
            redirectLink = clickEvent.redirectLink
        }
        val bundle = Bundle()
        bundle.putParcelable(PayLaterActionStepsBottomSheet.CLICK, clickEvent)
        bundle.putParcelable(PayLaterActionStepsBottomSheet.IMPRESSION, impression)
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, detail)
        return bundle
    }

    fun getGoPayBundle(productId: String, detail: Detail) =
        Bundle().apply {
            putParcelable(PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL, detail.cta)
            putString(
                PayLaterTokopediaGopayBottomsheet.PARTER_NAME,
                detail.gatewayDetail?.name ?: ""
            )
            putInt(PayLaterTokopediaGopayBottomsheet.TENURE, detail.tenure ?: 0)
            putString(PayLaterTokopediaGopayBottomsheet.PRODUCT_ID, productId)
            putInt(
                PayLaterTokopediaGopayBottomsheet.EMI_AMOUNT,
                detail.installment_per_month_ceil ?: 0
            )
        }

    fun getInstallmentBundle(detail: Detail): Bundle {
        val eventImpression = PayLaterBottomSheetImpression().apply {
            tenureOption = detail.tenure ?: 0
            payLaterPartnerName = detail.gatewayDetail?.name ?: ""
            action = PdpSimulationAnalytics.IMPRESSION_BOTTOMSHEET
            emiAmount = detail.installment_per_month_ceil?.toString() ?: ""
            userStatus = detail.userState ?: ""
            limit = detail.limit?.toString() ?: ""
            redirectLink = ""
        }
        return Bundle().apply {
            putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, detail.installementDetails)
            putParcelable(PayLaterInstallmentFeeInfo.IMPRESSION_DETAIL, eventImpression)
        }
    }

}