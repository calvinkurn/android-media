package com.tokopedia.stories.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.stories.view.fragment.StoriesContentFragment

class StoriesPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
    private val viewPager: ViewPager2,
    private val tabPosition: (Int) -> Unit,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var _categoryCount: Int = 0
    private val categoryCount: Int
        get() = _categoryCount

    fun setCategorySize(count: Int) {
        _categoryCount = count
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabPosition.invoke(position + 1)
            }
        })
    }

    override fun getItemCount(): Int = categoryCount

    override fun createFragment(position: Int): Fragment {
        return StoriesContentFragment.getFragment(
            fragmentManager = fragmentManager,
            classLoader = fragmentActivity.classLoader,
        )
    }

}
