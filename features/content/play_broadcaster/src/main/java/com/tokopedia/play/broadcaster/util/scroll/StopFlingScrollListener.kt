package com.tokopedia.play.broadcaster.util.scroll

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 05/06/20
 */
class StopFlingScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_SETTLING) recyclerView.stopScroll()
    }
}