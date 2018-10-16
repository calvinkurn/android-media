package com.tokopedia.flashsale.management.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup

class CampaignDetailFragmentPagerAdapter(val fragmentManager: FragmentManager,
                                         private val titles: Array<String>)
    : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int) = Fragment()

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}