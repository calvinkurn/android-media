package com.tokopedia.stories.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoryDetailFragment
import com.tokopedia.stories.view.fragment.StoryDetailFragment.Companion.STORY_GROUP_ID
import com.tokopedia.stories.view.model.StoryGroupUiModel

class StoryGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _groupData: StoryGroupUiModel = StoryGroupUiModel()
    private val groupData: StoryGroupUiModel
        get() = _groupData

    fun setStoryGroup(data: StoryGroupUiModel) {
        _groupData = data
    }

    override fun getItemCount(): Int = groupData.groupItems.size

    override fun createFragment(position: Int): Fragment {
        return StoryDetailFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        ).apply { 
            arguments = Bundle().apply {
                putString(STORY_GROUP_ID, groupData.groupItems[position].groupId)
            }
        }
    }

}
