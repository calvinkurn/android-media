package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel

object SharingMapper {
    fun mapSharingEducationUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        serviceType: String
    ): HomeLayoutItemUiModel {
        val layout = HomeSharingEducationWidgetUiModel(
            id = response.id,
            state = state,
            serviceType = serviceType,
            btnTextRes = R.string.tokopedianow_home_sharing_education_button
        )
        return HomeLayoutItemUiModel(layout, state)
    }

    fun mapSharingReferralUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        warehouseId: String
    ): HomeLayoutItemUiModel {
        val layout = HomeSharingReferralWidgetUiModel(
            id = response.id,
            state = state,
            slug = response.widgetParam,
            campaignCode = response.campaignCode,
            warehouseId = warehouseId,
            titleSection = response.header.name
        )
        return HomeLayoutItemUiModel(layout, state)
    }
}
