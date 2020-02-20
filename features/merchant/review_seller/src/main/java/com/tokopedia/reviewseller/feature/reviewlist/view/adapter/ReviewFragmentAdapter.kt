package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class ReviewFragmentAdapter(fm: FragmentManager):
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var itemList: MutableList<ReviewFragmentItem> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return itemList[position].title
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ReviewFragmentItem>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    data class ReviewFragmentItem (
            val title: String,
            val fragment: Fragment
    )
}