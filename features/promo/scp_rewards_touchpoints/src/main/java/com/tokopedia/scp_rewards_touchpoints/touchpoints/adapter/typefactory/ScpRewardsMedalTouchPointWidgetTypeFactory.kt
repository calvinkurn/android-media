package com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.typefactory

import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel

interface ScpRewardsMedalTouchPointWidgetTypeFactory {
    fun type(uiModel: ScpRewardsMedalTouchPointWidgetUiModel): Int
}
