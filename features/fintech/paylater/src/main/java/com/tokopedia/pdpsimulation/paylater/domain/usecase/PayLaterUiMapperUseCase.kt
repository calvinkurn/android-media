package com.tokopedia.pdpsimulation.paylater.domain.usecase

import android.annotation.SuppressLint
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class PayLaterUiMapperUseCase @Inject constructor() : UseCase<ArrayList<SimulationUiModel>>() {

    private var payLaterGetSimulation: PayLaterGetSimulation? = null
    private var defaultTenure: Int = 0

    fun mapResponseToUi(
        onSuccess: (ArrayList<SimulationUiModel>) -> Unit,
        payLaterGetSimulation: PayLaterGetSimulation?,
        defaultTenure: Int,
    ) {
        this.payLaterGetSimulation = payLaterGetSimulation
        this.defaultTenure = defaultTenure
        this.execute({
            onSuccess(it)
        }, { onSuccess(arrayListOf()) })
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

    @SuppressLint("PII Data Exposure")
    private fun mapTenureToUi(payLaterGetSimulation: PayLaterGetSimulation?): ArrayList<SimulationUiModel> {
        val uiList = arrayListOf<SimulationUiModel>()
        payLaterGetSimulation?.let {
            var isTenureFound = false
            var maxTenureItemIndex = -1
            var maxTenureSoFar = -1
            if (it.productList.isNullOrEmpty()) return uiList

            it.productList.forEachIndexed { index, data ->
                val getPayLaterList = mapSimulationToUi(data.detail)
                if (defaultTenure == data.tenure)
                    isTenureFound = true
                else if (maxTenureSoFar < data.tenure ?: -1) {
                    maxTenureItemIndex = index
                    maxTenureSoFar = data.tenure ?: -1
                }

                val simulationUiModel = SimulationUiModel(
                    tenure = data.tenure,
                    text = data.text,
                    smallText = data.smallText,
                    simulationList = getPayLaterList,
                    isSelected = defaultTenure == data.tenure,
                    label = SimulationUiModel.LabelUiModel.create(data.label),
                    promoName = data.promoName ?: ""
                )
                uiList.add(simulationUiModel)
            }
            // if no matching tenure set max tenure as selected
            if (uiList.isNotEmpty() && isTenureFound.not()) {
                val selectionIndex = if(maxTenureItemIndex >= 0)
                    maxTenureItemIndex
                else uiList.size -1
                uiList[selectionIndex].isSelected = true
            }
        }
        return uiList
    }
}
