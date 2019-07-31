package com.tokopedia.navigation.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * @author : Steven 09/04/19
 */
class NotificationFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm){

    private var itemList: MutableList<NotificationFragmentItem> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList.get(position).fragment
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<NotificationFragmentAdapter.NotificationFragmentItem>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    data class NotificationFragmentItem (
            val title: String,
            val fragment: Fragment
    )
}