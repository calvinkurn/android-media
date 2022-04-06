package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val onScrollDown: () -> Unit = {},
    private val onScrollUp: () -> Unit = {}
) : RecyclerView.OnScrollListener() {

    private var scrollYDistance = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        scrollYDistance = dy
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
            onScrollDown()
        }

        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
            if (scrollYDistance <= 0) {
                onScrollUp()
            } else {
                scrollYDistance = 0
                onScrollDown()
            }
        }
    }
}