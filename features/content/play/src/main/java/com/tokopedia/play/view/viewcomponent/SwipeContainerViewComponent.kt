package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tokopedia.play.util.findCurrentFragment
import com.tokopedia.play.view.adapter.SwipeContainerStateAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 19/01/21
 */
class SwipeContainerViewComponent(
        container: ViewGroup,
        @IdRes rootId: Int,
        private val fragmentManager: FragmentManager,
        private val lifecycle: Lifecycle,
        private val dataSource: DataSource,
        listener: Listener
) : ViewComponent(container, rootId) {

    val scrollState: Int
        get() = vpFragment.scrollState

    private val vpFragment = rootView as ViewPager2

    private var adapter = SwipeContainerStateAdapter(fragmentManager, lifecycle, dataSource::getFragment)

    private var isLoading: Boolean = false

    init {
        vpFragment.offscreenPageLimit = 1
        vpFragment.adapter = adapter

        vpFragment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                val totalItem = adapter.itemCount
                if (totalItem == 0) return
                if (!isLoading && position + PAGE_LOAD_THRESHOLD >= totalItem) {
                    isLoading = true
                    listener.onShouldLoadNextPage()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == SCROLL_STATE_IDLE) listener.onSwipeNextPage()
            }
        })
    }

    fun setChannelIds(channelIds: List<String>) {
        adapter.setChannelList(channelIds)
        isLoading = false
    }

    fun getActiveFragment(): Fragment? {
        return vpFragment.findCurrentFragment(fragmentManager)
    }

    fun refocusFragment() {
        //TODO("Research the best way to handle this")

        val destItemPos = vpFragment.currentItem
        scrollToPosition(destItemPos)
    }

    fun setEnableSwiping(shouldEnable: Boolean) {
        vpFragment.isUserInputEnabled = shouldEnable
    }

    fun hasNextPage(): Boolean {
        return getCurrentPos() < adapter.itemCount - 1
    }

    fun scrollTo(direction: ScrollDirection, isSmoothScroll: Boolean = false) {
        scrollToPosition(
                position = when (direction) {
                    ScrollDirection.Prev -> getCurrentPos() - 1
                    ScrollDirection.Next -> getCurrentPos() + 1
                },
                isSmoothScroll = isSmoothScroll
        )
    }

    fun reset() {
        adapter = SwipeContainerStateAdapter(fragmentManager, lifecycle, dataSource::getFragment)
        vpFragment.adapter = adapter
    }

    private fun scrollToPosition(position: Int, isSmoothScroll: Boolean = false) {
        val recyclerView = vpFragment.getChildAt(0) as RecyclerView
        if (isSmoothScroll) recyclerView.smoothScrollToPosition(position)
        else recyclerView.scrollToPosition(position)
    }

    private fun getCurrentPos(): Int {
        return vpFragment.currentItem
    }

    interface DataSource {

        fun getFragment(channelId: String): Fragment
    }

    interface Listener {

        fun onShouldLoadNextPage()
        fun onSwipeNextPage()
    }

    enum class ScrollDirection {

        Prev,
        Next
    }

    companion object {
        private const val PAGE_LOAD_THRESHOLD = 2
    }
}