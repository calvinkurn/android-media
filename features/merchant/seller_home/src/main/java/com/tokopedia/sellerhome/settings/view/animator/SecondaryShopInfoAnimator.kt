package com.tokopedia.sellerhome.settings.view.animator

import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondaryShopInfoAnimator(private val recyclerView: RecyclerView?) {

    companion object {
        private const val SCROLLING_POSITION = 100
        private const val ZERO_Y_DELTA = 0

        private const val RV_RIGHT_DIRECTION = 1
    }

    private var isRecyclerViewHasScrolled = false
    private var hasRecyclerViewTouchedWhenAnimating = false

    private val onScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!isRecyclerViewHasScrolled) {
                    val hasScrolled =
                        !isRecyclerViewHasScrolled && newState == RecyclerView.SCROLL_STATE_DRAGGING
                    if (hasScrolled) {
                        isRecyclerViewHasScrolled = true
                        removeOnScrollListener()
                    }
                }
            }
        }
    }

    private val onAutomaticScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (hasRecyclerViewTouchedWhenAnimating) {
                    return
                }
                val offset = recyclerView.computeHorizontalScrollOffset()
                // Scroll back to initial state if the rv has scrolled to determined position or
                // if it has reached the end of rv
                val shouldScrollBack =
                    offset >= SCROLLING_POSITION || !recyclerView.canScrollHorizontally(
                        RV_RIGHT_DIRECTION
                    )
                if (shouldScrollBack) {
                    recyclerView.smoothScrollBy(-SCROLLING_POSITION, ZERO_Y_DELTA)
                }
            }
        }
    }

    private val onRecyclerViewTouchedListener by lazy {
        object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                hasRecyclerViewTouchedWhenAnimating = true
                removeOnTouchListener()
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }
    }

    init {
        recyclerView?.addOnScrollListener(onScrollListener)
    }

    fun swipeRecyclerViewGently() {
        val shouldScroll = !(isRecyclerViewHasScrolled || checkIfAllContentsHaveShown())
        if (shouldScroll) {
            removeOnScrollListener(onScrollListener)
            recyclerView?.run {
                addOnScrollListener(onAutomaticScrollListener)
                addOnItemTouchListener(onRecyclerViewTouchedListener)
                smoothScrollBy(SCROLLING_POSITION, ZERO_Y_DELTA)
            }
        }
    }

    private fun removeOnScrollListener(listener: RecyclerView.OnScrollListener = onScrollListener) {
        recyclerView?.removeOnScrollListener(listener)
    }

    private fun removeOnTouchListener() {
        recyclerView?.removeOnItemTouchListener(onRecyclerViewTouchedListener)
    }

    /**
     * Returns whether all contents in secondary are visible in the screen.
     * If true, then we should not need to scroll the rv
     *
     * @return  is all contents have shown
     */
    private fun checkIfAllContentsHaveShown(): Boolean {
        return (recyclerView?.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
            ?.let { lastVisiblePosition ->
                recyclerView.adapter?.itemCount?.minus(1)?.let { lastIndexPosition ->
                    val isNoPosition = lastVisiblePosition == RecyclerView.NO_POSITION
                    !isNoPosition && lastVisiblePosition >= lastIndexPosition
                }
            } ?: false
    }

}