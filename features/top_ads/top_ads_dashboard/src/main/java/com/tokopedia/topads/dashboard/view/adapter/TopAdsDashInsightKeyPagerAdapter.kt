package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Pika on 21/7/20.
 */
class TopAdsDashInsightKeyPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private var listFrag: ArrayList<Fragment> = arrayListOf()

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