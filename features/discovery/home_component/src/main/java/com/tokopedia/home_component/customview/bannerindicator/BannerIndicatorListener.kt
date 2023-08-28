package com.tokopedia.home_component.customview.bannerindicator

/**
 * Created by dhaba
 */
interface BannerIndicatorListener {
    fun onChangePosition(position: Int)
    fun onChangeCurrentPosition(position: Int)
    fun getCurrentPosition(position: Int)
}
