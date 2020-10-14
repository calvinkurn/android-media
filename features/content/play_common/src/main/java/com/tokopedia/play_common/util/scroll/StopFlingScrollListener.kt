package com.tokopedia.play_common.util.scroll

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 17/09/20
 */
class StopFlingScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_SETTLING) recyclerView.stopScroll()
    }
}