package com.tokopedia.pdpsimulation.paylater.helper

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.*
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

    fun getInstallmentBundle(installmentDetails: InstallmentDetails?) = Bundle().apply {
        putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, installmentDetails)
    }

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

        return PayLaterAnalyticsBase().apply {
            tenureOption = defaultSelectedSimulation
            action = PdpSimulationAnalytics.IMPRESSION_PAYLATER
            userStatus = userState
            payLaterPartnerName = partnerList
            linkingStatus = linkStatus
        }

    }

}