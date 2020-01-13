package com.rahullohra.fakeresponse.presentaiton.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rahullohra.fakeresponse.presentaiton.fragments.GqlFragment

class PagerAdapter : FragmentStatePagerAdapter {

    val fragmentList :ArrayList<Fragment> = arrayListOf()
    val tabTitles :ArrayList<String> = arrayListOf()

    constructor(fm: FragmentManager) : super(fm) {

        fragmentList.add(GqlFragment())
        fragmentList.add(GqlFragment())
        
        tabTitles.add("FakeGql")
        tabTitles.add("Custom")
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getCount() = 1

}
