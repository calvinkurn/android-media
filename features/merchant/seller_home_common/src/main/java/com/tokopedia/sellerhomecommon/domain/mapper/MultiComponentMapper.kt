package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.GetMultiComponentDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.MultiComponentItemUiModel
import javax.inject.Inject

class MultiComponentMapper @Inject constructor() :
    BaseResponseMapper<GetMultiComponentDataResponse, List<MultiComponentDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: GetMultiComponentDataResponse,
        isFromCache: Boolean
    ): List<MultiComponentDataUiModel> {
        return listOf(
            MultiComponentDataUiModel(
                dataKey = response.fetchMultiComponentWidget.dataKey,
                tabs = response.fetchMultiComponentWidget.tabs.map {
                    MultiComponentTab(
                        id = it.id,
                        title = it.title,
                        components = it.components.map { component ->
                            MultiComponentData(
                                componentType = component.componentType,
                                dataKey = component.dataKey,
                                configuration = component.configuration,
                                metricParam = component.metricsParam,
                                data = null
                            )
                        },
                        isSelected = false,
                        isLoaded = false,
                        isError = it.components.all { multiComponent -> !multiComponent.error }
                    )
                }
            )
        )
    }

    fun mapToMultiComponentData(data: BaseDataUiModel): MultiComponentItemUiModel? {
        return when (data) {
            // TODO: Map to line chart later
            is BarChartDataUiModel -> null
            else -> null
        }
    }
}
