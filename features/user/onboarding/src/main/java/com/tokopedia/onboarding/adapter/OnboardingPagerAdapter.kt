package com.tokopedia.onboarding.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author by stevenfredian on 14/05/19.
 */
class OnboardingPagerAdapter(fm: FragmentManager?,
                             var fragmentList : ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}