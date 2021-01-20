package com.tokopedia.topads.common.view.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

private const val POS_FIRST = 0
private const val POS_SECOND = 1
private const val POS_THIRD = 2

class TopAdsEditPagerAdapter(private val titles: Array<String>, fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    var list: ArrayList<Fragment> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            POS_FIRST -> return titles[0]
            POS_SECOND -> return titles[1]
            POS_THIRD -> return titles[2]
        }
        return titles[2]
    }

    fun setData(list: ArrayList<Fragment>) {
        this.list = list
        notifyDataSetChanged()
    }
}