package com.tokopedia.common.topupbills.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class TopupBillsProductTabAdapter(val tabList: MutableList<TopupBillsTabItem>, fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return tabList[position].fragment
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].title
    }
}