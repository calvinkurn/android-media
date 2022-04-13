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

    private val onScrollStateChangeListener = OnScrollStateChangeListener()
    private val onContentScrollListener = OnContentChangeListener()

    private var showJob: Job? = null

    private var isContentScrollListener = false

    init {
        addView(view)
    }

    fun setup(
        recyclerView: RecyclerView,
        items: List<Item>
    ) {
        if (items.isEmpty()) return

        this.recyclerView = recyclerView.apply {
            addContentScrollListener(this)

            tabLayout.addOnTabSelectedListener(onTabSelectedListener)
            addOnScrollListener(onScrollStateChangeListener)
        }

        tabLayout.removeAllTabs()
        this.items = items.onEach { item ->
            pdpNavTab.addNewTab(item.label)
        }
    }

    private fun scrollToContent(tabPosition: Int) {
        val position = items.getOrNull(tabPosition)?.position?.invoke() ?: -1
        smoothScrollToPosition(position)
    }

    private fun smoothScrollToPosition(position: Int) {
        if (position == -1) return

        recyclerView?.apply {
            removeContentScrollListener(this)
            smoothScroller.targetPosition = position
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun addContentScrollListener(recyclerView: RecyclerView) {
        if (!isContentScrollListener) {
            recyclerView.addOnScrollListener(onContentScrollListener)
            isContentScrollListener = true
        }
    }

    private fun removeContentScrollListener(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onContentScrollListener)
        isContentScrollListener = false
    }

    data class Item(
        val label: String,
        val position: () -> Int,
        val viewId: Int
    )

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
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is LinearLayoutManager) return

            /**
             * ProductDetailNavigation will render front of recyclerview
             * layoutManager.findFirstVisibleItemPosition -> is doesn't aware of nav tab
             *
             * we should manually determine if the item position if visible in screen
             * (with nav tab in from of recyclerview)
             */
            val position = layoutManager.findFirstVisibleItemPosition()
            val someItem = layoutManager.findViewByPosition(position)
            val rectItem = Rect()
            someItem?.getGlobalVisibleRect(rectItem)
            val rectRv = Rect()
            recyclerView.getGlobalVisibleRect(rectRv)
            val firstVisibleItemPosition = if ((rectItem.bottom - rectRv.top) <= view.height) {
                position + 1
            } else position
            /**
             * end
             */

            val indexTab = items.indexOfFirst { item ->
                firstVisibleItemPosition == item.position.invoke()
//                val view1 = layoutManager.findViewByPosition(firstVisibleItemPosition)
//                view1 != null && (view1.id == item.viewId)
            }
            pdpNavTab.tabLayout.getTabAt(indexTab)?.run {
                tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
                select()
                tabLayout.addOnTabSelectedListener(onTabSelectedListener)
            }

            if (layoutManager.findFirstVisibleItemPosition() == 0 || dy < 0) {
                view.hide()
            } else {
                view.show()
            }
        }
    }

    private inner class OnScrollStateChangeListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                        if (it.findFirstVisibleItemPosition() != 0) {
                            showJob?.cancel()
                            showJob = launch(Dispatchers.IO) {
                                delay(2000)
                                withContext(Dispatchers.Main) {
                                    view.show()
                                }
                            }
                        }
                    }
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> addContentScrollListener(recyclerView)
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
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main
}