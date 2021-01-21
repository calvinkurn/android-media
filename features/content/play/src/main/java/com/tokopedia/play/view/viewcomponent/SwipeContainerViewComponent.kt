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

    private val vpFragment = rootView as ViewPager2

    private val adapter = SwipeContainerStateAdapter(fragmentManager, lifecycle, dataSource::getFragment)

    init {
        vpFragment.offscreenPageLimit = 1
        vpFragment.adapter = adapter
    }

    fun setChannelIds(channelIds: List<String>) {
        adapter.setChannelList(channelIds)
    }

    interface DataSource {

        fun getFragment(channelId: String): Fragment
    }

}