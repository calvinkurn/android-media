package com.tokopedia.profile.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.tokopedia.profile.view.viewmodel.FollowingListTabItem
import java.util.*

/**
 * @author by milhamj on 30/10/18.
 */
class FollowingListTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var itemList: MutableList<FollowingListTabItem> = ArrayList()

    fun setItemList(itemList: MutableList<FollowingListTabItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: FollowingListTabItem) {
        this.itemList.add(position, item)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) = itemList[position].fragment

    override fun getItemPosition(`object`: Any): Int {
        return itemList.indices.firstOrNull { `object` == itemList[it] }
                ?: PagerAdapter.POSITION_NONE
    }

    override fun getCount() = itemList.size

    override fun getPageTitle(position: Int) = itemList[position].title
}