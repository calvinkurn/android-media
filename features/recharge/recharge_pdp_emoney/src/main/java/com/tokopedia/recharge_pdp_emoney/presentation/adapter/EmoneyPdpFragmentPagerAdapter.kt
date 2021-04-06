package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpPromoListFragment
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpRecentTransactionFragment

class EmoneyPdpFragmentPagerAdapter(fragment: Fragment,
                                    private val recommendations: List<TopupBillsRecommendation>,
                                    private val promoList: List<TopupBillsPromo>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return arrayListOf(recommendations.isNotEmpty(), promoList.isNotEmpty()).count { it }
    }

    override fun createFragment(position: Int): Fragment {
        return when {
            isRecommendationFragmentPosition(position) -> EmoneyPdpRecentTransactionFragment()
            isPromoListFragmentPosition(position) -> EmoneyPdpPromoListFragment.newInstance(position == 0)
            else -> Fragment()
        }
    }

    private fun isRecommendationFragmentPosition(position: Int) = position == 0 && recommendations.isNotEmpty()
    private fun isPromoListFragmentPosition(position: Int) = position == 0 && recommendations.isEmpty() || position == 1
}