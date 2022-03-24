package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel

object SharingEducationMapper {
    fun mapSharingEducationUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        serviceType: String
    ): HomeLayoutItemUiModel {
        val layout = HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel(
            id = response.id,
            state = state,
            serviceType = serviceType,
            btnTextRes = R.string.tokopedianow_home_sharing_education_button
        )
        return HomeLayoutItemUiModel(layout, state)
    }
}
