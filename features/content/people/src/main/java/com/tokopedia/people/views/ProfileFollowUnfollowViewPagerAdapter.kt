package com.tokopedia.people.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class ProfileFollowUnfollowViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private final var fragmentList1: ArrayList<Fragment> = ArrayList()
    private final var fragmentTitleList1: ArrayList<String> = ArrayList()

    // returns which item is selected from arraylist of fragments.
    override fun getItem(position: Int): Fragment {
        return fragmentList1[position];
    }

//    // returns which item is selected from arraylist of titles.
//    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList1[position]
    }

    // returns the number of items present in arraylist.
    override fun getCount(): Int {
        return 2;
    }

    // this function adds the fragment and title in 2 separate  arraylist.
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList1.add(fragment)
        fragmentTitleList1.add(title)
    }
}
