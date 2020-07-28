package com.tokopedia.seller_migration_common.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class SellerFeatureFragmentAdapter(fm: FragmentManager):
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var itemList: MutableList<SellerFeatureFragmentItem> = ArrayList()

    override fun getItem(position: Int): Fragment = itemList[position].fragment

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<SellerFeatureFragmentItem>) {
        itemList = tabList
    }

    data class SellerFeatureFragmentItem (
            val fragment: Fragment,
            val tabName: String
    )
}