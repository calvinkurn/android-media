package com.tokopedia.stories.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoryDetailFragment

class StoryGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _groupSize: Int = 0
    private val groupSize: Int
        get() = _groupSize

    fun setStoryGroup(size: Int) {
        _groupSize = size
    }

    override fun getItemCount(): Int = groupSize

    override fun createFragment(position: Int): Fragment {
        return StoryDetailFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        )
    }

}
