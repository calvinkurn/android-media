package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val onScrollDown: () -> Unit = {},
    private val onScrollUp: () -> Unit = {},
    private val onScroll: () -> Unit = {}
) : RecyclerView.OnScrollListener() {

    private var scrollYDistance = 0

    private var previousScroll = Scroll.UP

    enum class Scroll {
        DOWN,
        UP
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        scrollYDistance = dy
        onScroll()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val currentScroll =  if (scrollYDistance.isScrollUp()) Scroll.UP else Scroll.DOWN

        if (newState == RecyclerView.SCROLL_STATE_IDLE && currentScroll != previousScroll) {
            if (scrollYDistance.isScrollUp()) {
                onScrollUp()
                previousScroll = Scroll.UP
            } else {
                scrollYDistance = 0
                onScrollDown()
                previousScroll = Scroll.DOWN
            }
        }
    }

    private fun Int.isScrollUp() : Boolean {
        return this <= 0
    }
}
