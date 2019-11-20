package com.tokopedia.product.detail.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
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

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        val f = `object` as Fragment
        val view = f.view

        if (view != null) {
            val nestedView = view.findViewWithTag<View>("nested")
            if (nestedView != null && nestedView is NestedScrollView) {
                nestedView.isNestedScrollingEnabled = true
            }
        }

        for (i in 0 until count) {
            if (i != position) {
                val otherScrollView = container.findViewWithTag<View>("nested")
                if (otherScrollView != null && otherScrollView is NestedScrollView)
                    otherScrollView.isNestedScrollingEnabled = false
            }
        }

        container.requestLayout()
    }
}
