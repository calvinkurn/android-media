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
        isSender: Boolean = true
    ): HomeLayoutItemUiModel {
        val descRes: Int
        val btnTextRes: Int

        if (isSender) {
            descRes = R.string.tokopedianow_home_referral_widget_desc_sender
            btnTextRes = R.string.tokopedianow_home_referral_widget_button_text_sender
        } else {
            descRes = R.string.tokopedianow_home_referral_widget_desc_receiver
            btnTextRes = R.string.tokopedianow_home_referral_widget_button_text_receiver
        }

        val layout = HomeSharingReferralWidgetUiModel(
            id = response.id,
            state = state,
            descRes = descRes,
            btnTextRes = btnTextRes,
            slug = response.widgetParam,
            isSender = isSender
        )
        return HomeLayoutItemUiModel(layout, state)
    }
}
