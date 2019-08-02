package com.tokopedia.onboarding.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * @author by stevenfredian on 14/05/19.
 */
class OnboardingPagerAdapter(fm: FragmentManager?, var fragmentList: ArrayList<Fragment>) :
        FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}