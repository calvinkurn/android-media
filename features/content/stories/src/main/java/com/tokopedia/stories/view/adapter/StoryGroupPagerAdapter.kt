package com.tokopedia.stories.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoryDetailFragment
import com.tokopedia.stories.view.model.StoryGroupUiModel

class StoryGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _group: List<StoryGroupUiModel> = emptyList()
    private val group: List<StoryGroupUiModel>
        get() = _group

    fun setStoryGroup(group: List<StoryGroupUiModel>) {
        _group = group
    }

    override fun getItemCount(): Int = group.size

    override fun createFragment(position: Int): Fragment {
        return StoryDetailFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        )
    }

}
