package com.tokopedia.flashsale.management.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.product.view.FlashSaleProductListFragment
import com.tokopedia.flashsale.management.view.fragment.FlashSaleInfoFragment

class CampaignDetailFragmentPagerAdapter(val fragmentManager: FragmentManager,
                                         private val titles: List<String>,
                                         private val campaignId: Long,
                                         private val campaignUrl: String,
                                         private val campaignType: String?,
                                         private val sellerStatus: SellerStatus)
    : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int) = when(position){
        0 -> FlashSaleInfoFragment.createInstance(campaignId, campaignUrl, sellerStatus, campaignType)
        1 -> FlashSaleProductListFragment.createInstance(campaignId.toInt(), campaignUrl)
        else -> Fragment()
    }

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}