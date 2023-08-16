package com.tokopedia.stories.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID

class StoriesGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _group: List<StoriesGroupUiModel> = emptyList()
    private val group: List<StoriesGroupUiModel>
        get() = _group

    fun setStoriesGroup(group: List<StoriesGroupUiModel>) {
        _group = group
    }

    override fun getItemCount(): Int = group.size

    override fun createFragment(position: Int): Fragment {
        return StoriesDetailFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        ).apply {
            arguments = Bundle().apply {
                putString(STORIES_GROUP_ID, group.getOrNull(position)?.id.orEmpty())
            }
        }
    }

}
