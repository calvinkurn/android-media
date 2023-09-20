package com.tokopedia.stories.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.model.StoriesUiModel

class StoriesGroupPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
    private val shopId: String,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _groupData: StoriesUiModel = StoriesUiModel()
    private val groupData: StoriesUiModel
        get() = _groupData

    fun setStoriesGroup(data: StoriesUiModel) {
        _groupData = data
    }

    fun getCurrentPageGroupName(position: Int): String {
        return groupData.groupItems.getOrNull(position)?.groupName.orEmpty()
    }

    fun getCurrentData() = groupData.groupItems

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
                putString(STORIES_GROUP_ID, getCurrentPageGroupId(position))
                putString(SHOP_ID, shopId)
            }
        }
    }
}
