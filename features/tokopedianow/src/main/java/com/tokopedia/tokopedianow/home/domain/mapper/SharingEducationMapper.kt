package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingEducationWidgetUiModel

object SharingEducationMapper {
    fun mapSharingEducationUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        serviceType: String
    ): HomeLayoutItemUiModel {
        val layout = HomeSharingEducationWidgetUiModel(
            id = response.id,
            state = state,
            serviceType = serviceType,
        )
        return HomeLayoutItemUiModel(layout, state)
    }
}
