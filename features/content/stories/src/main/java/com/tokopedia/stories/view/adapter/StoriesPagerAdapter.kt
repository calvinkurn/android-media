package com.tokopedia.stories.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.stories.view.fragment.StoriesContentFragment
import com.tokopedia.stories.view.model.StoriesUiModel

class StoriesPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _storiesData: StoriesUiModel = StoriesUiModel.Empty
    private val storiesData: StoriesUiModel
        get() = _storiesData

    fun setStoriesData(storiesData: StoriesUiModel) {
        _storiesData = storiesData
    }

    override fun getItemCount(): Int = storiesData.stories.size

    override fun createFragment(position: Int): Fragment {
        return StoriesContentFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        )
    }

}
