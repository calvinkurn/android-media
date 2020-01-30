package com.tokopedia.notifcenter.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.notifcenter.data.entity.NotificationTabItem
import java.util.*

/**
 * @author : Steven 09/04/19
 */
class NotificationFragmentAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var itemList: MutableList<NotificationTabItem> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList[position].fragment?: throw Exception("no fragment attach found")
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<NotificationTabItem>) {
        itemList = tabList
        notifyDataSetChanged()
    }

}