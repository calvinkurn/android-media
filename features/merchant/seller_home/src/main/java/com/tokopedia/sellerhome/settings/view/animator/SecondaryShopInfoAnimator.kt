package com.tokopedia.sellerhome.settings.view.animator

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class SecondaryShopInfoAnimator(private val recyclerView: RecyclerView?) {

    companion object {
        private const val SCROLLING_POSITION = 100
        private const val ZERO_Y_DELTA = 0
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
                if (offset >= SCROLLING_POSITION) {
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
        if (!isRecyclerViewHasScrolled) {
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

}