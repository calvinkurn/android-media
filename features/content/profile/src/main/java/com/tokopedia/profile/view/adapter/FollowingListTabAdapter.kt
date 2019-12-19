package com.tokopedia.profile.view.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.profile.view.viewmodel.FollowingListTabItem
import java.util.*

/**
 * @author by milhamj on 30/10/18.
 */
class FollowingListTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var itemList: MutableList<FollowingListTabItem> = mutableListOf()

    fun setItemList(itemList: List<FollowingListTabItem>) {
        this.itemList.apply {
            clear()
            addAll(itemList)
        }
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

    fun <T>removeByInstance(javaClass: Class<T>) {
        itemList.removeAll { it.fragment::class.java == javaClass }
    }

    override fun getItem(position: Int) = itemList[position].fragment

    override fun getItemPosition(`object`: Any) = POSITION_NONE

    override fun getCount() = itemList.size

    override fun getPageTitle(position: Int) = itemList[position].title
}