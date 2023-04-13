package com.tokopedia.manageaddress.ui.debugbanner

import androidx.annotation.ColorRes
import com.tokopedia.manageaddress.R

data class Banner(
    val bannerGravity: BannerGravity = BannerGravity.START,
//    @ColorRes val bannerColorRes: Int = R.color.unify_G500,
    @ColorRes val bannerColorRes: Int = R.color.unify_Y500,
    @ColorRes val textColorRes: Int = android.R.color.white,
    val bannerText: String = "STAGING"
)
