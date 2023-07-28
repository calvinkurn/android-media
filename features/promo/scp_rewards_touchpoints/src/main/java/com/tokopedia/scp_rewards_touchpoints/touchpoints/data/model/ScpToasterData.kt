package com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpToasterData(
    val title:String = String.EMPTY,
    val subtitle:String = String.EMPTY,
    val ctaText:String = String.EMPTY,
    val iconImage: String = String.EMPTY,
    val sunburstImage: String = String.EMPTY,
    val ctaIsShown:Boolean = false
)
