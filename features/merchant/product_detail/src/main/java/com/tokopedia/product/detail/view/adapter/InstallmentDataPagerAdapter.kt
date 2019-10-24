package com.tokopedia.product.detail.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.tokopedia.product.detail.view.util.FtInstallmentListItem
import java.util.*

class InstallmentDataPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var searchSectionItemList: List<FtInstallmentListItem> = ArrayList()
    private var mCurrentPosition: Int = 0

    fun setData(searchSectionItemList: List<FtInstallmentListItem>) {
        this.searchSectionItemList = searchSectionItemList
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment? {
        mCurrentPosition = position
        return searchSectionItemList[position].fragment
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return searchSectionItemList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return searchSectionItemList[position].title
    }

    /*override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
        super.setPrimaryItem(container, position, any)

        if (container !is HeightWrappingViewPager) {
            throw UnsupportedOperationException("ViewPager is not a WrappingViewPager")
        }
        val fragment = any as Fragment
        val pager = container as HeightWrappingViewPager?
        if (fragment.view != null) {
            mCurrentPosition = position
            pager!!.onPageChanged(fragment.view!!)
        }
    }*/


}
