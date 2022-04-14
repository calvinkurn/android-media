package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.databinding.WidgetProductDetailNavigationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class ProductDetailNavigation(
    context: Context, attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), CoroutineScope {

    private val binding = WidgetProductDetailNavigationBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root
    private val pdpNavTab = binding.pdpNavTab
    private val tabLayout = pdpNavTab.tabLayout

    private var recyclerView: RecyclerView? = null
    private var items: List<Item> = emptyList()

    private val smoothScroller = SmoothScroller(context)
    private val onTabSelectedListener = OnTabSelected()

    private val onScrollListener = OnScrollListener()
    private val onContentScrollListener = OnContentChangeListener()

    private var showJob: Job? = null

    private var translateInProgress = false

    init {
        addView(view)
        binding.pdpNavTab.tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView.apply {
            addOnScrollListener(onScrollListener)
            addOnScrollListener(onContentScrollListener)
        }
    }

    fun setTabItems(items: List<Item>) {
        var shouldUpdateTab = false
        items.forEachIndexed { index, item ->
            val currentItem = this.items.getOrNull(index)
            if (currentItem?.label != item.label) {
                shouldUpdateTab = true
            }
            currentItem?.position = item.position
        }

        if (shouldUpdateTab) {
            tabLayout.removeAllTabs()
            this.items = items.onEach { item ->
                pdpNavTab.addNewTab(item.label)
            }
        }

        if (this.items.isEmpty()) view.hide()
    }

    private fun scrollToContent(tabPosition: Int) {
        val position = items.getOrNull(tabPosition)?.position ?: -1
        smoothScrollToPosition(position)
    }

    private fun smoothScrollToPosition(position: Int) {
        if (position == -1) return

        recyclerView?.apply {
            smoothScroller.targetPosition = position
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun toggle(show: Boolean) {
        if (translateInProgress) return
        translateInProgress = true
        val showY = 0f
        val hideY = -1f * view.height

        if (view.translationY < showY && view.translationY > hideY) return

        if (show) view.show()
        val y = if (show) showY else hideY
        view.animate().translationY(y).setDuration(300).withEndAction {
            translateInProgress = false
        }
    }

    data class Item(
        val label: String,
        var position: Int
    )

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        private var state = RecyclerView.SCROLL_STATE_IDLE

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    showJob?.cancel()
                    val firstPosition = getFirstVisibleItemPosition(recyclerView)
                    if (firstPosition == 0) {
                        toggle(false)
                    } else delayedShow()
                }
            }
            state = newState

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            if (state == RecyclerView.SCROLL_STATE_SETTLING) return

            if (getFirstVisibleItemPosition(recyclerView) == 0 || dy < 0) {
                toggle(false)
            } else {
                toggle(true)
            }
        }

        private fun getFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is LinearLayoutManager) return -1
            return layoutManager.findFirstVisibleItemPosition()
        }

        private fun delayedShow() {
            showJob?.cancel()
            showJob = launch(Dispatchers.IO) {
                delay(2000)
                withContext(Dispatchers.Main) {
                    toggle(true)
                }
            }
        }
    }


    private inner class OnTabSelected : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            scrollToContent(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            scrollToContent(tab.position)
        }
    }

    private inner class OnContentChangeListener : RecyclerView.OnScrollListener() {

        private var state = RecyclerView.SCROLL_STATE_IDLE

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                updateSelectedTab(recyclerView)
            }
            state = newState
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if(state == RecyclerView.SCROLL_STATE_SETTLING) return
            updateSelectedTab(recyclerView)
        }

        /**
         * ProductDetailNavigation will render front of recyclerview
         * layoutManager.findFirstVisibleItemPosition -> is doesn't aware of nav tab
         *
         * we should manually determine if the item position if visible in screen
         * (with nav tab in from of recyclerview)
         */
        private fun calculateFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is LinearLayoutManager) return -1
            val position = layoutManager.findFirstVisibleItemPosition()
            val someItem = layoutManager.findViewByPosition(position)
            val rectItem = Rect()
            someItem?.getGlobalVisibleRect(rectItem)
            val rectRv = Rect()
            recyclerView.getGlobalVisibleRect(rectRv)
            return if ((rectItem.bottom - rectRv.top) <= view.height) {
                position + 1
            } else position
        }

        private fun updateSelectedTab(recyclerView: RecyclerView) {
            val firstVisibleItemPosition = calculateFirstVisibleItemPosition(recyclerView)
            val indexTab = items.indexOfFirst { firstVisibleItemPosition == it.position }

            pdpNavTab.tabLayout.getTabAt(indexTab)?.run {
                tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
                select()
                tabLayout.addOnTabSelectedListener(onTabSelectedListener)
            }
        }

    }

    private inner class SmoothScroller(context: Context) : LinearSmoothScroller(context) {

        override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
            return super.calculateDyToMakeVisible(
                view,
                snapPreference
            ) + this@ProductDetailNavigation.view.height
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 50f / displayMetrics.densityDpi
        }
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main
}