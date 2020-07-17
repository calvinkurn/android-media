package com.tokopedia.topads.dashboard.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NEG_KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK

/**
 * Created by Pika on 06/06/20.
 */
class TopAdsDashGroupDetailPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    private val itemTabList: MutableList<Fragment> = mutableListOf()
    private val title: MutableList<String> = mutableListOf(PRODUK, KATA_KUNCI, NEG_KATA_KUNCI)

    override fun getItem(position: Int): Fragment {
        return itemTabList[position]
    }

    override fun getCount(): Int {
        return itemTabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    fun setList(item: List<Fragment>) {
        item.let {
            itemTabList.clear()
            itemTabList.addAll(item)
            notifyDataSetChanged()
        }
    }

    fun setTitleProduct(title: String) {
        this.title[0] = title
        notifyDataSetChanged()
    }

    fun setTitleKeyword(title: String) {
        this.title[1] = title
        notifyDataSetChanged()
    }

    fun setTitleNegKeyword(title: String) {
        this.title[2] = title
        notifyDataSetChanged()
    }
}