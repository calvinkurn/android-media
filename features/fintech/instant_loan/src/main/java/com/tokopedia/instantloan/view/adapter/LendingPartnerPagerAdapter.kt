package com.tokopedia.instantloan.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager
import com.tokopedia.instantloan.view.ui.PartnerDataPageItem
import java.util.*

class LendingPartnerPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var partnerDataPageItem: List<PartnerDataPageItem> = ArrayList()
    private var mCurrentPosition: Int = 0

    fun setData(partnerDataPageItem: ArrayList<PartnerDataPageItem>) {
        this.partnerDataPageItem = partnerDataPageItem
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment? {
        mCurrentPosition = position
        return partnerDataPageItem[position].fragment
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return partnerDataPageItem.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
        super.setPrimaryItem(container, position, any)

        if (container !is HeightWrappingViewPager) {
            throw UnsupportedOperationException("ViewPager is not a WrappingViewPager")
        }

        if (position != mCurrentPosition) {
            val fragment = any as Fragment
            val pager = container as HeightWrappingViewPager?
            if (fragment != null && fragment.view != null) {
                mCurrentPosition = position
                pager!!.onPageChanged(fragment.view!!)
            }
        }
    }


}

