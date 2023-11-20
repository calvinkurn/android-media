package com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model

import android.graphics.Bitmap
import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpToasterData(
    val title: String = String.EMPTY,
    val subtitle: String = String.EMPTY,
    val ctaText: String = String.EMPTY,
    val iconImage: Bitmap? = null,
    val sunburstImage: Bitmap? = null,
    val ctaIsShown: Boolean = false
)
