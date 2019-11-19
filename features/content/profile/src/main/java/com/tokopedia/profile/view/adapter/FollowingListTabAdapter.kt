package com.tokopedia.profile.view.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.profile.view.viewmodel.FollowingListTabItem
import java.util.*

/**
 * @author by milhamj on 30/10/18.
 */
class FollowingListTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var itemList: MutableList<FollowingListTabItem> = ArrayList()

    fun setItemList(itemList: MutableList<FollowingListTabItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: FollowingListTabItem) {
        this.itemList.add(position, item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        this.itemList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) = itemList[position].fragment

    override fun getItemPosition(`object`: Any) = POSITION_NONE

    override fun getCount() = itemList.size

    override fun getPageTitle(position: Int) = itemList[position].title
}