package com.tokopedia.topupbills.telco.common.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoPromoFragment
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoRecommendationFragment
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoProductFragment

class TelcoTabAdapter(fragment: Fragment, listener: Listener) : FragmentStateAdapter(fragment) {

    private val tabList = listener.getTabList()

    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (tabList.size == 2) {
            when (position) {
                0 -> DigitalTelcoPromoFragment.newInstance()
                1 -> DigitalTelcoRecommendationFragment.newInstance()
                else -> DigitalTelcoPromoFragment.newInstance()
            }
        } else {
            DigitalTelcoProductFragment.newInstance(tabList[position].bundle)
        }
    }

    override fun getItemId(position: Int): Long {
        return tabList[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        return tabList.any { it.id == itemId }
    }

    interface Listener {
        fun getTabList(): List<TelcoTabItem>
    }
}