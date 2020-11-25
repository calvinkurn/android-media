package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem

/**
 * Created by Pika on 14/5/20.
 */
class TopAdsDashInsightPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    var listFrag: ArrayList<Fragment> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return listFrag[position]
    }

    override fun getCount(): Int {
        return listFrag.size
    }

    fun setList(listItem: ArrayList<Fragment>) {
            this.listFrag = listItem
            notifyDataSetChanged()
    }
}