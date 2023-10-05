package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object EducationalInformationMapper {
    fun mapEducationalInformationUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState, serviceType: String): HomeLayoutItemUiModel {
        return HomeLayoutItemUiModel(
            HomeEducationalInformationWidgetUiModel(
                id = response.id,
                serviceType = serviceType
            ),
            state
        )
    }
}
