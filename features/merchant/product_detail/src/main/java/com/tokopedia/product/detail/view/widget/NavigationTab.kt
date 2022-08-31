package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.databinding.WidgetNavigationTabBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class NavigationTab(
    context: Context, attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), CoroutineScope {

    companion object {
        private const val NAVIGATION_ANIMATION_DURATION = 300L
        private const val NAVIGATION_DELAYED_SHOW_DURATION = 2000L
    }

    private val binding = WidgetNavigationTabBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root
    private val pdpNavTab = binding.pdpNavTab
    private val tabLayout = pdpNavTab.tabLayout

    private var recyclerView: RecyclerView? = null
    private var items: List<Item> = emptyList()
    private var listener: NavigationListener? = null

    private val smoothScroller = SmoothScroller(context)
    private val onTabSelectedListener = OnTabSelected()

    private val onScrollListener = OnScrollListener()
    private val onContentScrollListener = OnContentChangeListener()

    private var showJob: Job? = null

    private var enableTabSelectedListener = true
    private var enableScrollUpListener = true
    private var enableContentChangeListener = true
    private var impressNavigation = false
    private var isVisible = false
    private var enableBlockingTouch = true
    private var navTabPositionOffsetY = 0

    init {
        addView(view)
        binding.pdpNavTab.tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    fun start(
        recyclerView: RecyclerView,
        items: List<Item>,
        enableBlockingTouch: Boolean,
        listener: NavigationListener,
        offsetY: Int = 0
    ) {
        navTabPositionOffsetY = offsetY
        recyclerView.removeOnScrollListener(onScrollListener)
        recyclerView.removeOnScrollListener(onContentScrollListener)

        this.listener = listener
        recyclerView.addOnScrollListener(onScrollListener)
        recyclerView.addOnScrollListener(onContentScrollListener)
        this.recyclerView = recyclerView
        this.enableBlockingTouch = enableBlockingTouch

        updateItems(items)
    }

    fun stop(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        recyclerView.removeOnScrollListener(onContentScrollListener)
        toggle(false)
        view.visibility = INVISIBLE
    }

    fun updateItemPosition() {
        this.items.forEach { item ->
            item.updatePosition()
        }
    }

    fun onClickBackToTop() {
        enableContentChangeListener = true
    }

    private fun updateItems(items: List<Item>) {
        var shouldUpdateTab = false
        items.forEachIndexed { index, item ->
            val currentItem = this.items.getOrNull(index)
            if (currentItem?.label != item.label) {
                shouldUpdateTab = true
            }
        }

        if (shouldUpdateTab) {
            enableTabSelectedListener = false
            tabLayout.removeAllTabs()
            this.items = items.onEach { item ->
                pdpNavTab.addNewTab(item.label)
            }
            enableTabSelectedListener = true
        }
    }

    private fun toggle(show: Boolean) {
        if (isVisible == show) return

        val showY = 0f
        val hideY = -1f * view.height

        if (show) {
            view.show()
            if (!impressNavigation) {
                listener?.onImpressionNavigationTab(
                    items.map { it.label }
                )
                impressNavigation = true
            }
        }

        val y = if (show) showY else hideY
        view.animate().translationY(y).duration = NAVIGATION_ANIMATION_DURATION
        isVisible = show
    }

    private fun enableTouchScroll(isEnable: Boolean) {
        if (enableBlockingTouch) {
            recyclerView?.suppressLayout(!isEnable)
        } else recyclerView?.suppressLayout(false)
    }

    /**
     * ProductDetailNavigation will render front of recyclerview
     * layoutManager.findFirstVisibleItemPosition -> is doesn't aware of actual visible item
     *
     * we should manually determine if the item position if visible in screen
     * (with nav tab in from of recyclerview)
     */
    private fun calculateFirstVisibleItemPosition(recyclerView: RecyclerView, offsetY: Int): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager !is LinearLayoutManager) return -1
        val position = layoutManager.findFirstVisibleItemPosition()
        val someItem = layoutManager.findViewByPosition(position)
        val rectItem = Rect()
        someItem?.getGlobalVisibleRect(rectItem)
        val rectRv = Rect()
        recyclerView.getGlobalVisibleRect(rectRv)
        return if ((rectItem.bottom - rectRv.top) <= offsetY) {
            position + 1
        } else position
    }

    data class Item(
        val label: String,
        private val positionUpdater: () -> Int
    ) {
        private var position: Int = -1

        fun getPosition() = positionUpdater.invoke()

        fun updatePosition() {
            position = positionUpdater.invoke()
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                enableScrollUpListener = true
                showHide(recyclerView)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (enableScrollUpListener && (getFirstVisibleItemPosition(recyclerView) == 0 || dy < 0)) {
                toggle(false)
            } else {
                toggle(true)
            }
        }

        private fun getFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is LinearLayoutManager) return -1
            return calculateFirstVisibleItemPosition(recyclerView, offsetY = navTabPositionOffsetY)
        }

        private fun delayedShow() {
            showJob?.cancel()
            showJob = launch(Dispatchers.IO) {
                delay(NAVIGATION_DELAYED_SHOW_DURATION)
                withContext(Dispatchers.Main) {
                    toggle(true)
                }
            }
        }

        private fun showHide(recyclerView: RecyclerView) {
            showJob?.cancel()
            val firstPosition = getFirstVisibleItemPosition(recyclerView)
            if (firstPosition == 0) {
                toggle(false)
            } else if (!isVisible) delayedShow()
        }
    }

    private inner class OnTabSelected : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            selectTab(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab) {
            selectTab(tab.position)
        }

        private fun selectTab(position: Int) {
            if (!enableTabSelectedListener) return
            scrollToContent(position)
            trackOnClickTab(position)
        }

        private fun scrollToContent(tabPosition: Int) {
            val position = items.getOrNull(tabPosition)?.getPosition() ?: -1
            smoothScrollToPosition(position)
        }

        private fun smoothScrollToPosition(position: Int) {
            if (position == -1) return

            recyclerView?.apply {
                enableTouchScroll(false)
                smoothScroller.targetPosition = position
                layoutManager?.startSmoothScroll(smoothScroller)
            }
        }

        private fun trackOnClickTab(position: Int) {
            val label = items.getOrNull(position)?.label ?: ""
            listener?.onClickNavigationTab(position, label)
        }
    }

    private inner class OnContentChangeListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                enableContentChangeListener = true
                enableTouchScroll(true)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (enableContentChangeListener) updateSelectedTab(recyclerView)
        }

        private fun updateSelectedTab(recyclerView: RecyclerView) {
            val firstVisibleItemPosition = calculateFirstVisibleItemPosition(recyclerView, offsetY = view.height)
            val indexTab = if (firstVisibleItemPosition == 0) 0
            else items.indexOfFirst { firstVisibleItemPosition == it.getPosition() }

            pdpNavTab.tabLayout.getTabAt(indexTab)?.run {
                if (isSelected) return
                enableTabSelectedListener = false
                select()
                enableTabSelectedListener = true
            }
        }

    }

    private inner class SmoothScroller(context: Context) : LinearSmoothScroller(context) {

        private var isScroll = false

        override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
            return super.calculateDyToMakeVisible(
                view,
                snapPreference
            ) + this@NavigationTab.view.height + navTabPositionOffsetY
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun onStart() {
            super.onStart()
            enableScrollUpListener = false
            enableContentChangeListener = false
        }

        override fun onSeekTargetStep(dx: Int, dy: Int, state: RecyclerView.State, action: Action) {
            super.onSeekTargetStep(dx, dy, state, action)
            isScroll = true
        }

        override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
            super.onTargetFound(targetView, state, action)
            if (!isScroll) enableTouchScroll(true)
            isScroll = false
        }
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main
}