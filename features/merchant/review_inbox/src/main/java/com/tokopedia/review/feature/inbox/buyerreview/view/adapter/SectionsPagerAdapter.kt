package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout

/**
 * @author by Nisie on 20/01/16.
 */
class SectionsPagerAdapter constructor(
    fm: FragmentManager,
    private val fragmentList: List<Fragment>,
    private val indicator: TabLayout?
) : FragmentPagerAdapter(
    fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return indicator?.getTabAt(position)?.text ?: ""
    }
}