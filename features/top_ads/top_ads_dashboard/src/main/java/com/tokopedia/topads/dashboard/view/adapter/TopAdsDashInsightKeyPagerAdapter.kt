package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Pika on 21/7/20.
 */
class TopAdsDashInsightKeyPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private var listFrag: ArrayList<Fragment> = arrayListOf()
    private val title: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return listFrag[position]
    }
    override fun getCount(): Int {
        return listFrag.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    fun setList(listItem: ArrayList<Fragment>) {
            this.listFrag = listItem
            notifyDataSetChanged()
    }

    fun setTitle(title1: String, title2: String, title3: String) {
        title.add(title1)
        title.add(title2)
        title.add(title3)
        notifyDataSetChanged()
    }
}