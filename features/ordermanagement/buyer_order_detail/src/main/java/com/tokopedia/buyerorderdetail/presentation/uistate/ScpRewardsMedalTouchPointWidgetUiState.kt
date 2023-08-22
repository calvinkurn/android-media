package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel

sealed interface ScpRewardsMedalTouchPointWidgetUiState {
    sealed interface HasData : ScpRewardsMedalTouchPointWidgetUiState {
        val uiModel: ScpRewardsMedalTouchPointWidgetUiModel

        data class Showing(
            override val uiModel: ScpRewardsMedalTouchPointWidgetUiModel
        ) : HasData

        object Hidden : HasData {
            override val uiModel: ScpRewardsMedalTouchPointWidgetUiModel = ScpRewardsMedalTouchPointWidgetUiModel()
        }
    }
}
