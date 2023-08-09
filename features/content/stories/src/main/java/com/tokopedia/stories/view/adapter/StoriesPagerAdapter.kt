package com.tokopedia.stories.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.stories.view.fragment.StoriesContentFragment
import com.tokopedia.stories.view.model.StoriesUiModel

class StoriesPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
    private val viewPager: ViewPager2,
    private val selectedPage: (Int) -> Unit,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _storiesData: StoriesUiModel = StoriesUiModel.Empty
    private val storiesData: StoriesUiModel
        get() = _storiesData

    fun setStoriesData(storiesData: StoriesUiModel) {
        _storiesData = storiesData
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedPage.invoke(position)
            }
        })
    }

    override fun getItemCount(): Int = storiesData.stories.size

    override fun createFragment(position: Int): Fragment {
        return StoriesContentFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        )
    }

}
