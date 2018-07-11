package com.tokopedia.shop.page.view.adapter

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment

class ShopPageViewPagerAdapter(val fragmentManager: FragmentManager, val titles: Array<String>,
                               val shopId: String?, val shopDomain: String?, val shopAttribution: String?)
    : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        const val STATES = "states"
    }

    override fun getItem(position: Int): Fragment {
        return ShopProductListLimitedFragment.createInstance(shopAttribution)
    }

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun saveState(): Parcelable {
        var bundle: Bundle? = super.saveState() as Bundle
        if (bundle == null){
            bundle = Bundle()
        }
        bundle.putParcelableArray(STATES, null)
        return bundle
    }
}