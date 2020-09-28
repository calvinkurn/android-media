package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GRUP
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TANPA_GRUP

/**
 * Created by Pika on 16/4/20.
 */
class GroupNonGroupPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    var list: ArrayList<Fragment> = arrayListOf()
    private var title: MutableList<String> = mutableListOf(GRUP, TANPA_GRUP)
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setData(list: ArrayList<Fragment>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    fun setTitleGroup(title: String) {
        this.title[0] = title
        notifyDataSetChanged()
    }

    fun setTitleProduct(title: String) {
        this.title[1] = title
        notifyDataSetChanged()
    }
}