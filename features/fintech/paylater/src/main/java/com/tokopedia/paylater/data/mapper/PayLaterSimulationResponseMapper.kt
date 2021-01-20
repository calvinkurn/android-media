package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterGetSimulationResponse
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.paylater.domain.model.SimulationItemDetail

const val ONE_MONTH_TENURE = 1
const val THREE_MONTH_TENURE = 3
const val SIX_MONTH_TENURE = 6
const val NINE_MONTH_TENURE = 9
const val TWELVE_MONTH_TENURE = 12
const val EMPTY_TENURE = -1

sealed class PayLaterSimulationTenureType(val tenure: Int)
object OneMonthInstallment : PayLaterSimulationTenureType(ONE_MONTH_TENURE)
object ThreeMonthlyInstallment : PayLaterSimulationTenureType(THREE_MONTH_TENURE)
object SixMonthlyInstallment : PayLaterSimulationTenureType(SIX_MONTH_TENURE)
object NineMonthlyInstallment : PayLaterSimulationTenureType(NINE_MONTH_TENURE)
object TwelveMonthlyInstallment : PayLaterSimulationTenureType(TWELVE_MONTH_TENURE)
object EmptyInstallment : PayLaterSimulationTenureType(EMPTY_TENURE)

object PayLaterSimulationResponseMapper {

    fun handleSimulationResponse(payLaterSimulationResponse: PayLaterGetSimulationResponse?): ArrayList<PayLaterSimulationGatewayItem> {
        payLaterSimulationResponse?.also {
            it.payLaterGetSimulationGateway?.also { gatewayResponse ->
                if (gatewayResponse.payLaterGatewayList.isNullOrEmpty()) {
                    arrayListOf<PayLaterSimulationGatewayItem>()
                } else {
                    for (gatewayItem in gatewayResponse.payLaterGatewayList) {
                        val tenureMap = HashMap<PayLaterSimulationTenureType, SimulationItemDetail>()
                        var isRecommended = false
                        for (itemDetail in gatewayItem.simulationDetailList) {
                            val tenureType = getSimulationTenureType(itemDetail.tenure)
                            if (itemDetail.isRecommended == true) isRecommended = true
                            tenureMap[tenureType] = itemDetail
                        }
                        gatewayItem.isRecommended = isRecommended
                        gatewayItem.installmentMap = tenureMap
                    }
                    return gatewayResponse.payLaterGatewayList
                }
            } ?: return arrayListOf()
        } ?: return arrayListOf()
        return arrayListOf()
    }

    fun getSimulationTenureType(tenure: Int?): PayLaterSimulationTenureType {
        return when (tenure) {
            1 -> OneMonthInstallment
            3 -> ThreeMonthlyInstallment
            6 -> SixMonthlyInstallment
            9 -> NineMonthlyInstallment
            12 -> TwelveMonthlyInstallment
            else -> EmptyInstallment
        }
    }
}