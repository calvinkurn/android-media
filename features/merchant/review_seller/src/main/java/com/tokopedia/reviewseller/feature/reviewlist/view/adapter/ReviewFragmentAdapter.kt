package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ReviewFragmentAdapter(fm: FragmentManager):
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var itemList: MutableList<ReviewFragmentItem> = ArrayList()

    override fun getItem(position: Int): Fragment = itemList[position].fragment

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ReviewFragmentItem>) {
        itemList = tabList
    }

    data class ReviewFragmentItem (
            val fragment: Fragment
    )
}