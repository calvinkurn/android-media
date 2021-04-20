package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem

/**
 * Created by Pika on 14/5/20.
 */
class TopAdsDashboardBasePagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    private val itemTabList: MutableList<FragmentTabItem> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return itemTabList[position].fragment
    }

    override fun getCount(): Int {
        return itemTabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return itemTabList[position].title
    }

    fun getList(): MutableList<FragmentTabItem> = itemTabList

    fun setList(item: List<FragmentTabItem>) {
        item.let {
            itemTabList.clear()
            itemTabList.addAll(item)
        }
    }

    fun setTitle(title: String, position: Int) {
        itemTabList[position].title = title
        notifyDataSetChanged()
    }
}