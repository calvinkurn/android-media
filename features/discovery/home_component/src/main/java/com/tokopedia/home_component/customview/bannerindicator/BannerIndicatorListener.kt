package com.tokopedia.home_component.customview.bannerindicator

/**
 * Created by dhaba
 */
interface BannerIndicatorListener {
    fun onChangePosition(actualPosition: Int, bannerPosition: Int)
    fun getCurrentPosition(position: Int)
}
