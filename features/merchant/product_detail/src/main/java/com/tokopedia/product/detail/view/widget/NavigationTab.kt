package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.databinding.WidgetNavigationTabBinding
import com.tokopedia.product.detail.view.widget.ProductDetailNavigation.Companion.calculateFirstVisibleItemPosition
import com.tokopedia.unifycomponents.TabsUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NavigationTab : FrameLayout, CoroutineScope {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        init()
    }

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrSet,
        defStyleAttr
    ) {
        init()
    }

    companion object {
        private const val NAVIGATION_ANIMATION_DURATION = 300L
        private const val NAVIGATION_DELAYED_SHOW_DURATION = 2000L
        private const val SELECT_TAB_THRESHOLD = 300L
        private const val NAVIGATION_SHOW_THRESHOLD = 75f
        private const val HIDE_THRESHOLD_PX = -1
        private const val SHOW_THRESHOLD_PX = 0
    }

    private var binding : WidgetNavigationTabBinding? = null
    private var pdpNavTab : TabsUnify? = null
    private var tabLayout : TabLayout? = null

    private var recyclerView: RecyclerView? = null
    private var items: List<Item> = emptyList()
    private var listener: NavigationListener? = null
    private var config: ProductDetailNavigation.Configuration? = null

    private val smoothScroller = SmoothScroller(context)
    private val onTabSelectedListener = OnTabSelected()

    private val onScrollListener = OnScrollListener()
    private val onContentScrollListener = OnContentChangeListener()

    private var showJob: Job? = null
    private var selectTabJob: Job? = null

    private var enableTabSelectedListener = true
    private var enableScrollUpListener = true
    private var enableContentChangeListener = true
    private var impressNavigation = false
    private var isVisible = false
    private var enableBlockingTouch = true

    private fun init() {
        WidgetNavigationTabBinding.inflate(LayoutInflater.from(context)).also {
            binding = it
            pdpNavTab = it.pdpNavTab
            tabLayout = it.pdpNavTab.tabLayout
            addView(it.root)
            tabLayout?.addOnTabSelectedListener(onTabSelectedListener)
        }
    }

    fun start(
        recyclerView: RecyclerView,
        items: List<Item>,
        enableBlockingTouch: Boolean,
        listener: NavigationListener,
        config: ProductDetailNavigation.Configuration
    ) {
        recyclerView.removeOnScrollListener(onScrollListener)
        recyclerView.removeOnScrollListener(onContentScrollListener)

        this.config = config
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
        binding?.root?.visibility = INVISIBLE
    }

    fun updateItemPosition() {
        this.items.forEach { item ->
            item.updatePosition()
        }
    }

    fun onClickBackToTop() {
        enableContentChangeListener = true
        toggle(false, false)
    }

    fun disableAutoShowHide() {
        enableScrollUpListener = false
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
            tabLayout?.removeAllTabs()
            this.items = items.onEach { item ->
                pdpNavTab?.addNewTab(item.label)
            }
            enableTabSelectedListener = true
        }
    }

    private fun toggle(show: Boolean, animate: Boolean = true) {
        if (isVisible == show) return

        val showY = 0f
        val hideY = -1f * (binding?.root?.height ?: 0)

        if (show) {
            binding?.root?.show()
            if (!impressNavigation) {
                listener?.onImpressionNavigationTab(
                    items.map { it.label }
                )
                impressNavigation = true
            }
        }

        val y = if (show) showY else hideY
        val duration = if (animate) NAVIGATION_ANIMATION_DURATION else 0L
        binding?.root?.animate()?.translationY(y)?.duration = duration
        isVisible = show
    }

    private fun enableTouchScroll(isEnable: Boolean) {
        if (enableBlockingTouch) {
            recyclerView?.suppressLayout(!isEnable)
        } else recyclerView?.suppressLayout(false)
    }

    override fun onDetachedFromWindow() {
        showJob?.cancel()
        selectTabJob?.cancel()
        super.onDetachedFromWindow()
    }

    data class Item(
        val label: String,
        val componentName: String,
        private val positionUpdater: () -> Int
    ) {
        private var mutablePosition = -1

        val position: Int
            get() = mutablePosition

        fun updatePosition() {
            mutablePosition = positionUpdater.invoke()
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        val threshold = NAVIGATION_SHOW_THRESHOLD.toPx().toInt()

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                enableScrollUpListener = true
                showHide(recyclerView)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!enableScrollUpListener) return
            val shouldHide = shouldHide(recyclerView)
            if (shouldHide || dy < HIDE_THRESHOLD_PX) {
                toggle(false)
            } else if (dy > SHOW_THRESHOLD_PX) {
                toggle(true)
            }
        }

        private fun getFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
            return calculateFirstVisibleItemPosition(
                recyclerView,
                offsetY = config?.offsetY.orZero()
            )
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
            if (shouldHide(recyclerView)) {
                toggle(false)
            } else if (!isVisible) delayedShow()
        }

        private fun shouldHide(recyclerView: RecyclerView): Boolean {
            return if (config is ProductDetailNavigation.Configuration.Navbar4) {
                val scrollOffset = recyclerView.computeVerticalScrollOffset()
                scrollOffset < threshold
            } else getFirstVisibleItemPosition(recyclerView) == 0
        }
    }

    private inner class OnTabSelected : TabLayout.OnTabSelectedListener {

        private var lastTimeClick = System.currentTimeMillis()

        override fun onTabSelected(tab: TabLayout.Tab) {
            lastTimeClick = System.currentTimeMillis()
            selectTab(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab) {
            if (shouldProcessClick()) {
                selectTab(tab.position)
            }
        }

        private fun selectTab(position: Int) {
            if (!enableTabSelectedListener) return
            scrollToContent(position)
            trackOnClickTab(position)
        }

        private fun scrollToContent(tabPosition: Int) {
            val position = items.getOrNull(tabPosition)?.position ?: -1
            if (position == -1) return

            enableTouchScroll(false)
            selectTabJob?.cancel()
            selectTabJob = launch(Dispatchers.IO) {
                smoothScrollToPosition(position)
            }
        }

        private suspend fun smoothScrollToPosition(position: Int) {
            recyclerView?.apply {
                smoothScroller.targetPosition = position
                layoutManager?.startSmoothScroll(smoothScroller)
                if (position == 0) {
                    withContext(Dispatchers.Main) { onClickBackToTop() }
                }
            }
        }

        private fun trackOnClickTab(position: Int) {
            val label = items.getOrNull(position)?.label ?: ""
            listener?.onClickNavigationTab(position, label)
        }

        private fun shouldProcessClick(): Boolean {
            val currentTimeMillis = System.currentTimeMillis()
            val result = (currentTimeMillis - lastTimeClick) >= SELECT_TAB_THRESHOLD
            lastTimeClick = currentTimeMillis
            return result
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
            val offsetY = (binding?.root?.height ?: 0)  + config?.offsetY.orZero()
            val firstVisibleItemPosition = calculateFirstVisibleItemPosition(
                recyclerView = recyclerView,
                offsetY = offsetY
            )
            val indexTab = if (firstVisibleItemPosition == 0) 0
            else items.indexOfFirst { firstVisibleItemPosition == it.position }
            changeTab(indexTab)
        }

        private fun changeTab(position: Int) {
            if (position == -1) return
            pdpNavTab?.tabLayout?.getTabAt(position)?.run {
                if (isSelected) return@run
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
            ) + (this@NavigationTab.binding?.root?.height ?: 0) + config?.offsetY.orZero()
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
