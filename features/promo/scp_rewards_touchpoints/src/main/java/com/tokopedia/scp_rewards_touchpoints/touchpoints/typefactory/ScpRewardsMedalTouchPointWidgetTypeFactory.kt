package com.tokopedia.scp_rewards_touchpoints.touchpoints.typefactory

import com.tokopedia.scp_rewards_touchpoints.touchpoints.uimodel.ScpRewardsMedalTouchPointWidgetUiModel

interface ScpRewardsMedalTouchPointWidgetTypeFactory {
    fun type(uiModel: ScpRewardsMedalTouchPointWidgetUiModel): Int
}
