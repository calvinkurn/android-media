package com.tokopedia.topchat.chatlist.adapter

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * @author : Steven 2019-08-06
 */
class ChatListPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private var itemList: MutableList<ChatListTab> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList[position].fragment
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ChatListTab>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    data class ChatListTab (
            val title: String,
            val counter: String,
            val fragment: Fragment,
            @DrawableRes val icon: Int
    )
}