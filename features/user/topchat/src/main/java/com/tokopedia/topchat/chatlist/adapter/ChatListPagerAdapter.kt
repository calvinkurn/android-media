package com.tokopedia.topchat.chatlist.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

/**
 * @author : Steven 2019-08-06
 */
class ChatListPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private var itemList: MutableList<ChatListTab> = ArrayList()

    override fun getItem(position: Int): Fragment? {
        if(position >= itemList.size) {
            return null
        }
        return itemList[position].fragment
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ChatListTab>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    data class ChatListTab (
            val title: String,
            var counter: String,
            val fragment: Fragment,
            @DrawableRes val icon: Int
    )
}