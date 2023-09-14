package com.tokopedia.stories.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID

class StoriesGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
    private val shopId: String,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _groupData: StoriesGroupUiModel = StoriesGroupUiModel()
    private val groupData: StoriesGroupUiModel
        get() = _groupData

    fun setStoriesGroup(data: StoriesGroupUiModel) {
        _groupData = data
    }

    fun getCurrentPageGroupName(position: Int): String {
        return groupData.groupItems.getOrNull(position)?.groupName.orEmpty()
    }

    private fun getCurrentPageGroupId(instancePosition: Int): String {
        return groupData.groupItems.getOrNull(instancePosition)?.groupId.orEmpty()
    }

    override fun getItemCount(): Int = groupData.groupItems.size

    override fun createFragment(position: Int): Fragment {
        return StoriesDetailFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        ).apply {
            arguments = Bundle().apply {
                putString(STORIES_GROUP_ID, groupData.groupItems.getOrNull(position)?.groupId.orEmpty())
                putString(SHOP_ID, shopId)
            }
        }
    }
}
