package com.tokopedia.fakeresponse.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.fakeresponse.presentation.fragments.HomeFragment

class PagerAdapter : FragmentStatePagerAdapter {

    val fragmentList :ArrayList<Fragment> = arrayListOf()
    val tabTitles :ArrayList<String> = arrayListOf()

    constructor(fm: FragmentManager) : super(fm) {

        fragmentList.add(HomeFragment())
        
        tabTitles.add("Records")
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getCount() = 1

}
