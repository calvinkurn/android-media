package com.tokopedia.tokopoints.view.couponlisting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

import com.tokopedia.tokopoints.view.model.CouponFilterItem

class StackedCouponFilterPagerAdapter(fm: FragmentManager, private val mItems: List<CouponFilterItem>) : FragmentStatePagerAdapter(fm) {
    private val mrRegisteredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment {
        return CouponListingStackedFragment.newInstance()
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mItems[position].name
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        mrRegisteredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mrRegisteredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return mrRegisteredFragments.get(position)
    }
}
