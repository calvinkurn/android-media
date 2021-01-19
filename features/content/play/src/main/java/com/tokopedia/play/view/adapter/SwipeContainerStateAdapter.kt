package com.tokopedia.play.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by jegul on 19/01/21
 */
class SwipeContainerStateAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragmentCreator: (channelId: String) -> Fragment
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val channelids = listOf("12665", "12668", "12669", "12670", "12672")

    override fun getItemCount(): Int {
        return channelids.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentCreator(channelids[position])
    }
}