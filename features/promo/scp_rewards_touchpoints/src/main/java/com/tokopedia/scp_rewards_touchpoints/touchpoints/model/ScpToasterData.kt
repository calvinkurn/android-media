package com.tokopedia.scp_rewards_touchpoints.touchpoints.model

import android.graphics.Bitmap
import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpToasterData(
    val title: String = String.EMPTY,
    val subtitle: String = String.EMPTY,
    val ctaTitle: String = String.EMPTY,
    val badgeImage: Bitmap? = null,
    val sunflare: Bitmap? = null,
    val ctaIsShown: Boolean = false
)
