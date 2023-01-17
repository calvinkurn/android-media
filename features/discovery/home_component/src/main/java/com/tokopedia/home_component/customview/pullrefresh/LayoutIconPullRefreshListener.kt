package com.tokopedia.home_component.customview.pullrefresh

/**
 * Created by dhaba
 */
interface LayoutIconPullRefreshListener {
    fun maxOffsetTop(maxOffsetTop: Int)
    fun offsetView(offset: Float)
    fun startRefreshing()
    fun stopRefreshing(isAfterRefresh: Boolean)
    fun progressRefresh(progress: Float)
}
