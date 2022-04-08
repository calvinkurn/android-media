package com.tokopedia.tokomember_seller_dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TokomemberDashHomeViewpagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private final var fragmentList: ArrayList<Fragment> = ArrayList()
    private final var fragmentTitleList: ArrayList<String> = ArrayList()

    // returns which item is selected from arraylist of fragments.
    override fun getItem(position: Int): Fragment {
        return fragmentList[position];
    }

    // returns which item is selected from arraylist of titles.
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }

    // returns the number of items present in arraylist.
    override fun getCount(): Int {
        return 3;
    }

    // this function adds the fragment and title in 2 separate  arraylist.
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}