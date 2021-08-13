package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulationResponse
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationItemDetail
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationResponseMapper
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationTenureType
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class PayLaterTenureMapperUseCase @Inject constructor() : UseCase<PayLaterSimulationDataStatus>() {
    private val PARAM_SIMULATION_DATA = "param_simulation_data"
    private val SIMULATION_DATA_FAILURE = "NULL DATA"

    fun mapTenureToSimulation(
            payLaterSimulationResponse: PayLaterGetSimulationResponse?,
            onSuccess: (PayLaterSimulationDataStatus) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_SIMULATION_DATA, payLaterSimulationResponse)
        }
        execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): PayLaterSimulationDataStatus {
        val payLaterSimulationResponse: PayLaterGetSimulationResponse = (useCaseRequestParams.getObject(PARAM_SIMULATION_DATA)
                ?: throw NullPointerException(SIMULATION_DATA_FAILURE)) as PayLaterGetSimulationResponse
        return handleResponse(payLaterSimulationResponse)
    }

    private fun handleResponse(payLaterSimulationResponse: PayLaterGetSimulationResponse): PayLaterSimulationDataStatus {
        payLaterSimulationResponse.payLaterGetSimulationGateway?.also { gatewayResponse ->
            if (gatewayResponse.payLaterGatewayList.isNullOrEmpty()) {
                return StatusDataFailure
            } else {
                var isPayLaterApplicable = false
                for (gatewayItem in gatewayResponse.payLaterGatewayList) {
                    val tenureMap = HashMap<PayLaterSimulationTenureType, SimulationItemDetail>()
                    var isRecommended = false
                    if (!gatewayItem.simulationDetailList.isNullOrEmpty()) {
                        isPayLaterApplicable = true
                        for (itemDetail in gatewayItem.simulationDetailList) {
                            val tenureType = PayLaterSimulationResponseMapper.getSimulationTenureType(itemDetail.tenure)
                            if (itemDetail.isRecommended == true) isRecommended = true
                            tenureMap[tenureType] = itemDetail
                        }
                    }
                    gatewayItem.isRecommended = isRecommended
                    gatewayItem.installmentMap = tenureMap
                }
                return if (isPayLaterApplicable) StatusSuccess(gatewayResponse.payLaterGatewayList)
                else StatusPayLaterNotAvailable
            }
        } ?: return StatusDataFailure
        return StatusDataFailure
    }
}

sealed class PayLaterSimulationDataStatus
data class StatusSuccess(val data: ArrayList<PayLaterSimulationGatewayItem>) : PayLaterSimulationDataStatus()
object StatusDataFailure : PayLaterSimulationDataStatus()
object StatusPayLaterNotAvailable : PayLaterSimulationDataStatus()
