package com.tokopedia.home_component.customview.pullrefresh2

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.home_component.R

class LottieSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : SimpleSwipeRefreshLayout(context, attrs, defStyle) {

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LottieSwipeRefreshLayout, defStyle, 0).let { style ->
            style.recycle()
        }

        setOnRefreshListener {
            layoutIconPullRefreshView?.startRefreshing()
        }

        addProgressListener {
            layoutIconPullRefreshView?.progressRefresh(it)
        }

        addTriggerListener {
        }

        removeOnTriggerListener {
        }
    }
}
