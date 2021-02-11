package com.tokopedia.common.topupbills.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class TopupBillsProductTabAdapter(
        fragment: Fragment,
        private val tabList: MutableList<TopupBillsTabItem>)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabList[position].fragment
    }
}