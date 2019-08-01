package com.tokopedia.onboarding.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup

/**
 * @author by stevenfredian on 14/05/19.
 */
class OnboardingPagerAdapter(fm: FragmentManager?,
                             var fragmentList : ArrayList<Fragment>) : FragmentPagerAdapter(fm) {


    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment? {
        return registeredFragments.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE;
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }
}