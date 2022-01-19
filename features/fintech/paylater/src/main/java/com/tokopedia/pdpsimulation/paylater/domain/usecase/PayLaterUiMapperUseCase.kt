package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.usecase.coroutines.UseCase

class PayLaterUiMapperUseCase: UseCase<ArrayList<SimulationUiModel>>() {

    private var payLaterGetSimulation: PayLaterGetSimulation? = null

    fun mapResponseToUi(
        onSuccess: (ArrayList<SimulationUiModel>) -> Unit,
        onError: (Throwable) -> Unit,
        payLaterGetSimulation: PayLaterGetSimulation?,
    ) {
        this.payLaterGetSimulation = payLaterGetSimulation
        this.execute({
            onSuccess(it)
        }, {
            onError(PdpSimulationException.PayLaterNullDataException("NO data"))
        })

    }
    override suspend fun executeOnBackground(): ArrayList<SimulationUiModel> {
        return mapTenureToUi(payLaterGetSimulation)
    }

    private fun mapSimulationToUi(gatewaySectionList: List<GatewaySection>): ArrayList<BasePayLaterWidgetUiModel> {
        val simulationUiList = arrayListOf<BasePayLaterWidgetUiModel>()
        gatewaySectionList.forEach { gatewaySection ->
            if (gatewaySection.title?.isNotEmpty() == false) {
                simulationUiList.add(SectionTitleUiModel(gatewaySection.title.orEmpty()))
            }
            if (gatewaySection.isCollapsible == true && gatewaySection.detail.isNotEmpty()) {
                // add first item
                gatewaySection.detail.getOrNull(0)?.let { firstItem ->
                    simulationUiList.add(firstItem)
                }
                // add see more widget
                val detailListSize = gatewaySection.detail.size
                if (detailListSize > 1) {
                    val remainingList = gatewaySection.detail.subList(1, detailListSize)
                    simulationUiList.add(SeeMoreOptionsUiModel(remainingList))
                }

            } else {
                simulationUiList.addAll(gatewaySection.detail)
            }

        }
        return simulationUiList
    }

    private fun mapTenureToUi(payLaterGetSimulation: PayLaterGetSimulation?): ArrayList<SimulationUiModel> {
        val uiList = arrayListOf<SimulationUiModel>()
        payLaterGetSimulation?.let {
            it.productList?.forEach { data ->
                val getPayLaterList = mapSimulationToUi(data.detail)
                val simulationUiModel = SimulationUiModel(
                    tenure = data.tenure,
                    text = data.text,
                    smallText = data.smallText,
                    simulationList = getPayLaterList
                )
                uiList.add(simulationUiModel)
            }
        }
        return uiList
    }
}