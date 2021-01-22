package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
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
        dataSource: DataSource
) : ViewComponent(container, rootId) {

    val scrollState: Int
        get() = vpFragment.scrollState

    private val vpFragment = rootView as ViewPager2

    private val adapter = SwipeContainerStateAdapter(fragmentManager, lifecycle, dataSource::getFragment)

    init {
        vpFragment.offscreenPageLimit = 1
        vpFragment.adapter = adapter
    }

    fun setChannelIds(channelIds: List<String>) {
        adapter.setChannelList(channelIds)
    }

    fun getCurrentPos(): Int {
        return vpFragment.currentItem
    }

    fun refocusFragment() {
        vpFragment.post {
            if (adapter.itemCount > 1) {
                val currentPos = vpFragment.currentItem
                val fakePos = if (currentPos == 0) 1 else vpFragment.currentItem - 1
                vpFragment.setCurrentItem(fakePos, false)
                vpFragment.setCurrentItem(currentPos, false)
            }
        }
    }

    fun setEnableSwiping(shouldEnable: Boolean) {
        vpFragment.isUserInputEnabled = shouldEnable
    }

    interface DataSource {

        fun getFragment(channelId: String): Fragment
    }

}