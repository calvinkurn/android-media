package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.presentation.uistate.ScpRewardsMedalTouchPointWidgetUiState
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder
import com.tokopedia.unifycomponents.toPx

object ScpRewardsMedalTouchPointWidgetMapper {
    fun map(
        data: MedaliTouchpointOrder
    ): ScpRewardsMedalTouchPointWidgetUiState = ScpRewardsMedalTouchPointWidgetUiState.HasData.Showing(
        uiModel = mapToScpRewardsMedalTouchPointWidgetUiModel(
            data = data
        )
    )

    private fun mapToScpRewardsMedalTouchPointWidgetUiModel(
        data: MedaliTouchpointOrder
    ): ScpRewardsMedalTouchPointWidgetUiModel = ScpRewardsMedalTouchPointWidgetUiModel(
        title = data.infoMessage.title,
        subtitle = data.infoMessage.subtitle,
        iconImage = data.medaliIconImageURL,
        sunburstImage = data.medaliSunburstImageURL,
        backgroundIconImage = data.medaliIconImageURLWidget,
        backgroundWidgetImage = data.backgroundImageURL,
        ctaAppLink = data.cta.appLink,
        ctaIsShown = data.cta.isShown,
        marginLeft = 16.toPx(),
        marginTop = 22.toPx(),
        marginRight = 16.toPx(),
    )
}
