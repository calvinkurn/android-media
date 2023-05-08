package com.tokopedia.abstraction.base.view.debugbanner

import androidx.annotation.ColorRes
import com.tokopedia.abstraction.R

data class Banner(
    val bannerGravity: BannerGravity = BannerGravity.START,
    @ColorRes val textColorRes: Int = android.R.color.white,
    val bannerText: String = "STAGING"
)
