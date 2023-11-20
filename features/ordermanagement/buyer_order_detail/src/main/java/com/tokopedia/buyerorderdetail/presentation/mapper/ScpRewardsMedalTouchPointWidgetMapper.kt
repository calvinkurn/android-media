package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.presentation.uistate.ScpRewardsMedalTouchPointWidgetUiState
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder

object ScpRewardsMedalTouchPointWidgetMapper {
    fun map(
        data: MedaliTouchpointOrder,
        marginLeft: Int,
        marginTop: Int,
        marginRight: Int
    ): ScpRewardsMedalTouchPointWidgetUiState = ScpRewardsMedalTouchPointWidgetUiState.HasData.Showing(
        uiModel = ScpRewardsMedalTouchPointWidgetUiModel(
            title = data.infoMessage.title,
            subtitle = data.infoMessage.subtitle,
            iconImage = data.medaliIconImageURL,
            sunburstImage = data.medaliSunburstImageURL,
            backgroundIconImage = data.medaliIconImageURLWidget,
            backgroundWidgetImage = data.backgroundImageURL,
            ctaAppLink = data.cta.appLink,
            ctaIsShown = data.cta.isShown,
            marginLeft = marginLeft,
            marginTop = marginTop,
            marginRight = marginRight,
        )
    )
}
