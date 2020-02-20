package com.tokopedia.notifcenter.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

/**
 * @author : Steven 09/04/19
 */
class NotificationFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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