package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterGetSimulationResponse
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.paylater.domain.model.SimulationItemDetail

object PayLaterSimulationResponseMapper {

    fun handleSimulationResponse(payLaterSimulationResponse: PayLaterGetSimulationResponse?): ArrayList<PayLaterSimulationGatewayItem> {
        payLaterSimulationResponse?.also {
            it.payLaterGetSimulationGateway?.also { gatewayResponse ->
                if (gatewayResponse.payLaterGatewayList.isNullOrEmpty()) {
                    arrayListOf<PayLaterSimulationGatewayItem>()
                } else {
                    for (gatewayItem in gatewayResponse.payLaterGatewayList) {
                        val tenureMap = HashMap<Int, SimulationItemDetail>()
                        for (itemDetail in gatewayItem.simulationDetailList) {
                            tenureMap[itemDetail.tenure?: 0] = itemDetail
                        }
                        gatewayItem.installmentMap = tenureMap
                    }
                    return gatewayResponse.payLaterGatewayList
                }
            } ?: return arrayListOf()
        } ?: return arrayListOf()
        return arrayListOf()
    }
}