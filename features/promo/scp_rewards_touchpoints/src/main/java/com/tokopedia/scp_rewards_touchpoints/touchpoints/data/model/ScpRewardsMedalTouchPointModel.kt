package com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpRewardsMedalTouchPointModel(
    val title: String = String.EMPTY,
    val subtitle: String = String.EMPTY,
    val iconImage: String = String.EMPTY,
    val sunburstImage: String = String.EMPTY,
    val backgroundIconImage: String = String.EMPTY,
    val backgroundWidgetImage: String = String.EMPTY,
    val chevronIsShown: Boolean = false
)
