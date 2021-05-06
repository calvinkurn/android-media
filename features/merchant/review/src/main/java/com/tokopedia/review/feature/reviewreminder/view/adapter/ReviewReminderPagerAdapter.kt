package com.tokopedia.review.feature.reviewreminder.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.review.feature.reviewreminder.view.fragment.ReminderMessageFragment
import com.tokopedia.review.feature.reviewreminder.view.fragment.ReminderPerformanceFragment

class ReviewReminderPagerAdapter(
        fragmentManager: FragmentManager,
        private val tabLayout: TabLayout?
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragments = mutableListOf<Fragment>()

    private val classes = listOf(
            ReminderMessageFragment::class.java,
            ReminderPerformanceFragment::class.java
    )

    override fun getItem(position: Int): Fragment {
        return classes[position].newInstance().also {
            fragments.add(position, it as Fragment)
        }
    }

    override fun getCount() = classes.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabLayout?.getTabAt(position)?.text ?: ""
    }

}