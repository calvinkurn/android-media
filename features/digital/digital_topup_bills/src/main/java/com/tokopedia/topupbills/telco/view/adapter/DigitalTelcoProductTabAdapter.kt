package com.tokopedia.topupbills.telco.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductTabAdapter(val tabList: List<DigitalTabTelcoItem>, fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        return tabList[position].fragment
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].title
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        registeredFragments.put(position, fragment as Fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}