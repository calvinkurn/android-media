package com.tokopedia.deals.common.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.deals.ui.brand.DealsBrandActivity
import com.tokopedia.deals.ui.brand.DealsBrandFragment
import com.tokopedia.deals.ui.category.DealsCategoryActivity
import com.tokopedia.deals.ui.category.DealsCategoryFragment

class DealsFragmentPagerAdapter(
        activity: AppCompatActivity,
        private val ids: List<String?>,
        private val page: String,
        private val tabs: List<String>
) : FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        return when (page) {
            DealsCategoryActivity.TAG -> DealsCategoryFragment.getInstance(ids[position], tabs[position])
            DealsBrandActivity.TAG -> { DealsBrandFragment.getInstance(ids[position], tabs[position]) }
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int = ids.size
}
