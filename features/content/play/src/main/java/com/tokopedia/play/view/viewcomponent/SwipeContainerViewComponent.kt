package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.play.view.adapter.SwipeContainerStateAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 19/01/21
 */
class SwipeContainerViewComponent(
        container: ViewGroup,
        @IdRes rootId: Int,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        dataSource: DataSource,
        listener: Listener
) : ViewComponent(container, rootId) {

    val scrollState: Int
        get() = vpFragment.scrollState

    private val vpFragment = rootView as ViewPager2

    private val adapter = SwipeContainerStateAdapter(fragmentManager, lifecycle, dataSource::getFragment)

    private var isLoading: Boolean = false

    init {
        vpFragment.offscreenPageLimit = 1
        vpFragment.adapter = adapter

        vpFragment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                val totalItem = adapter.itemCount
                if (!isLoading && totalItem + PAGE_LOAD_THRESHOLD >= position) {
                    isLoading = true
                    listener.onShouldLoadNextPage()
                }
            }
        })
    }

    fun setChannelIds(channelIds: List<String>) {
        adapter.setChannelList(channelIds)
        isLoading = false
    }

    fun getCurrentPos(): Int {
        return vpFragment.currentItem
    }

    fun refocusFragment() {
        //TODO("Research the best way to handle this")

        val destItemPos = vpFragment.currentItem
        val recyclerView = vpFragment.getChildAt(0) as RecyclerView
        recyclerView.scrollToPosition(destItemPos)
    }

    fun setEnableSwiping(shouldEnable: Boolean) {
        vpFragment.isUserInputEnabled = shouldEnable
    }

    interface DataSource {

        fun getFragment(channelId: String): Fragment
    }

    interface Listener {

        fun onShouldLoadNextPage()
    }

    companion object {
        private const val PAGE_LOAD_THRESHOLD = 2
    }
}