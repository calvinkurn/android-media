package com.tokopedia.discovery.categoryrevamp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.discovery.categoryrevamp.data.CategorySectionItem
import java.util.*

class CategoryNavigationPagerAdapter(
        fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var mCategorySectionItemList: List<CategorySectionItem> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return mCategorySectionItemList[position].fragment
    }

    override fun getCount(): Int {
        return mCategorySectionItemList.size
    }

    fun setData(categorySectionItemList: List<CategorySectionItem>) {
        this.mCategorySectionItemList = categorySectionItemList
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mCategorySectionItemList[position].title
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}