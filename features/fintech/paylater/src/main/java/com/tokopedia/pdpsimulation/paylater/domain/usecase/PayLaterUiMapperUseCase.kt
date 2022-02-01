package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel.Companion.DATA_FAILURE
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class PayLaterUiMapperUseCase @Inject constructor(): UseCase<ArrayList<SimulationUiModel>>() {

    private var payLaterGetSimulation: PayLaterGetSimulation? = null
    private var defaultTenure: Int = 0

    fun mapResponseToUi(
        onSuccess: (ArrayList<SimulationUiModel>) -> Unit,
        onError: (Throwable) -> Unit,
        payLaterGetSimulation: PayLaterGetSimulation?,
        defaultTenure: Int,
    ) {
        this.payLaterGetSimulation = payLaterGetSimulation
        this.defaultTenure = defaultTenure
        this.execute({
            onSuccess(it)
        }, {
            onError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
        })

    }
    override suspend fun executeOnBackground(): ArrayList<SimulationUiModel> {
        return mapTenureToUi(payLaterGetSimulation)
    }

    private fun mapSimulationToUi(gatewaySectionList: List<GatewaySection>): ArrayList<BasePayLaterWidgetUiModel> {
        val simulationUiList = arrayListOf<BasePayLaterWidgetUiModel>()
        gatewaySectionList.forEach { gatewaySection ->
            if (gatewaySection.title?.isNotEmpty() == true) {
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
        simulationUiList.add(SupervisorUiModel)
        return simulationUiList
    }

    private fun mapTenureToUi(payLaterGetSimulation: PayLaterGetSimulation?): ArrayList<SimulationUiModel> {
        val uiList = arrayListOf<SimulationUiModel>()
        payLaterGetSimulation?.let {
            var isTenureFound = false
            it.productList?.forEach { data ->
                val getPayLaterList = mapSimulationToUi(data.detail)
                if (defaultTenure == data.tenure)
                    isTenureFound = true

                val simulationUiModel = SimulationUiModel(
                    tenure = data.tenure,
                    text = data.text,
                    smallText = data.smallText,
                    simulationList = getPayLaterList,
                    isSelected = defaultTenure == data.tenure
                )
                uiList.add(simulationUiModel)
            }
            // if no matching tenure set last as selected
            if (uiList.isNotEmpty() && isTenureFound.not())
                uiList[uiList.size-1].isSelected = true
        }
        return uiList
    }
}