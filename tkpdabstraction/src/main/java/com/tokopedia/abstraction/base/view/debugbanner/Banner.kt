package com.tokopedia.abstraction.base.view.debugbanner

import androidx.annotation.ColorRes
import com.tokopedia.abstraction.R
import com.tokopedia.manageaddress.ui.debugbanner.BannerGravity

data class Banner(
    val bannerGravity: BannerGravity = BannerGravity.START,
//    @ColorRes val bannerColorRes: Int = R.color.unify_G500,
    @ColorRes val bannerColorRes: Int = android.R.color.holo_red_dark,
    @ColorRes val textColorRes: Int = android.R.color.white,
    val bannerText: String = "STAGING"
)
