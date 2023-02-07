package com.tokopedia.pdpsimulation.paylater.helper

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.common.analytics.PayLaterBottomSheetImpression
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Cta
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet

object PayLaterBundleGenerator {

    fun getHowToUseBundle(detail: Detail) = Bundle().apply {
        putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, detail)
    }

    fun getGoPayBundle(cta: Cta) = Bundle().apply {
        putParcelable(PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL, cta)
    }


    @SuppressLint("PII Data Exposure")
    fun getPayLaterImpressionEvent(
        data: ArrayList<SimulationUiModel>,
        defaultSelectedSimulation: Int
    ): PayLaterAnalyticsBase {
        val simulationList = data[defaultSelectedSimulation].simulationList
        val firstDetail: BasePayLaterWidgetUiModel? = simulationList?.getOrNull(0)
        val (userState, linkStatus) = if (firstDetail is Detail)
            Pair(firstDetail.userState ?: "", firstDetail.linkingStatus ?: "")
        else Pair("", "")

        val partnerList =
            if (data.isNotEmpty()) PayLaterHelper.getProductNameList(simulationList) else ""
        val promoName = data.getOrNull(defaultSelectedSimulation)?.promoName ?: ""

        return PayLaterAnalyticsBase().apply {
            tenureOption = defaultSelectedSimulation
            action = PdpSimulationAnalytics.IMPRESSION_PAYLATER
            userStatus = userState
            payLaterPartnerName = partnerList
            linkingStatus = linkStatus
            this.promoName = promoName
        }

    }


    fun getInstallmentBundle(detail: Detail): Bundle {
        val eventImpression = PayLaterBottomSheetImpression().apply {
            tenureOption = detail.tenure ?: 0
            payLaterPartnerName = detail.gatewayDetail?.name ?: ""
            action = PdpSimulationAnalytics.BOTTOMSHEET_INSTALLMENT_ACTION
            emiAmount = detail.installment_per_month_ceil?.toString() ?: ""
            userStatus = detail.userState ?: ""
            limit = detail.limit ?: ""
        }
        return Bundle().apply {
            putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, detail.installementDetails)
            putParcelable(PayLaterInstallmentFeeInfo.IMPRESSION_DETAIL, eventImpression)
        }
    }

}
