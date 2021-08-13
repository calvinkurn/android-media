package com.tokopedia.topupbills.telco.common.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoPromoFragment
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoRecommendationFragment
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoProductFragment

class TelcoTabAdapter(fragment: Fragment, listener: Listener) : FragmentStateAdapter(fragment) {

    private val tabList = listener.getTabList()

    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (tabList[position].bundle != null) {
            DigitalTelcoProductFragment.newInstance(tabList[position].bundle!!)
        } else {
            val promoFragment = DigitalTelcoPromoFragment.newInstance()
            val recomFragment = DigitalTelcoRecommendationFragment.newInstance()

            when (position) {
                0 -> if (tabList.size == 1) promoFragment else recomFragment
                1 -> promoFragment
                else -> promoFragment
            }
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