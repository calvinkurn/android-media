package com.tokopedia.home_component.customview.pullrefresh

import android.content.Context
import android.util.AttributeSet

class ParentIconSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SimpleSwipeRefreshLayout(context, attrs, defStyle) {

    init {
        setOnRefreshListener {
            layoutIconPullRefreshView?.startRefreshing()
        }

        addProgressListener {
            layoutIconPullRefreshView?.progressRefresh(it)
        }
    }
}
