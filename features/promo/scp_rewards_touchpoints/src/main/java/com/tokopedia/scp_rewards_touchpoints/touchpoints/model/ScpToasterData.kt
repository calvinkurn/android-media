package com.tokopedia.scp_rewards_touchpoints.touchpoints.model

import android.graphics.Bitmap

data class ScpToasterData(
    val title:String = "",
    val subtitle:String = "",
    val ctaTitle:String = "",
    val badgeImage: Bitmap? = null,
    val sunflare:Bitmap? = null,
    val ctaIsShown:Boolean = false
)
