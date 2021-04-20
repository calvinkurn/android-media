package com.tokopedia.pdpsimulation.creditcard.domain.usecase

import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardSimulationResult
import com.tokopedia.pdpsimulation.creditcard.domain.model.PdpCreditCardSimulation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CreditCardSimulationMapperUseCase @Inject constructor() : UseCase<CCSimulationDataStatus>() {

    private val PARAM_SIMULATION_DATA = "param_simulation_data"
    private val SIMULATION_DATA_FAILURE = "NULL DATA"

    fun parseSimulationData(
            pdpCreditCardSimulationData: PdpCreditCardSimulation?,
            onSuccess: (CCSimulationDataStatus) -> Unit, onError: (Throwable) -> Unit,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_SIMULATION_DATA, pdpCreditCardSimulationData)
        }
        execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): CCSimulationDataStatus {
        val ccSimulationData: PdpCreditCardSimulation = (useCaseRequestParams.getObject(PARAM_SIMULATION_DATA)
                ?: throw NullPointerException(SIMULATION_DATA_FAILURE)) as PdpCreditCardSimulation
        return handleResponse(ccSimulationData)
    }

    private fun handleResponse(ccSimulationData: PdpCreditCardSimulation): CCSimulationDataStatus {
        ccSimulationData.creditCardGetSimulationResult?.let { simulationResult ->
            if (simulationResult.creditCardInstallmentList.isNullOrEmpty()) {
                return StatusApiFail
            } else {
                val isCreditCardSimulationAvailable = simulationResult.creditCardInstallmentList.any { installment ->
                    installment.isDisabled == false
                }
                simulationResult.creditCardInstallmentList[0].isSelected = true
                return if (isCreditCardSimulationAvailable) return StatusApiSuccess(simulationResult)
                else StatusCCNotAvailable
            }
        } ?: return StatusApiFail
    }

}

sealed class CCSimulationDataStatus
data class StatusApiSuccess(val data: CreditCardSimulationResult) : CCSimulationDataStatus()
object StatusApiFail : CCSimulationDataStatus()
object StatusCCNotAvailable : CCSimulationDataStatus()

