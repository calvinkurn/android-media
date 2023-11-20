package com.tokopedia.home_component.customview.bannerindicator

/**
 * Created by dhaba
 */
interface BannerIndicatorListener {
    fun onChangePosition(index: Int, position: Int)
    fun getCurrentPosition(position: Int)
}
