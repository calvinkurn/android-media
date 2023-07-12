package com.tokopedia.scp_rewards_touchpoints.touchpoints.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpToasterData(
    val title:String = String.EMPTY,
    val subtitle:String = String.EMPTY,
    val ctaTitle:String = String.EMPTY,
    val badgeImage: String = String.EMPTY,
    val sunflare: String = String.EMPTY,
    val ctaIsShown:Boolean = false
)
