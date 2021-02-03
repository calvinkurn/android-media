package com.tokopedia.pdpsimulation.common.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PayLaterPagerAdapter(val context: Context, fm: FragmentManager, behaviour: Int) : FragmentStatePagerAdapter(fm, behaviour) {
    private val itemTabList: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return itemTabList[position]
    }

    override fun getCount(): Int {
        return itemTabList.size
    }

    fun setList(item: List<Fragment>) {
        item.let {
            itemTabList.clear()
            itemTabList.addAll(item)
        }
    }
}