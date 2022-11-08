package com.tokopedia.home_component.customview.pullrefresh2

/**
 * Created by dhaba
 */
interface LayoutIconPullRefreshListener {
    fun offsetView(offset: Float)
    fun startRefreshing()
    fun stopRefreshing()
    fun progressRefresh(progress: Float)
}
