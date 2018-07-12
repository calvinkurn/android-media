package com.tokopedia.shop.page.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedNewFragment
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView

class ShopPageViewPagerAdapter(val fragmentManager: FragmentManager,
                               val titles: Array<String>,
                               val webViewListener: ShopPagePromoWebView.Listener,
                               val shopId: String?,
                               val shopAttribution: String?) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                val shopProductListLimitedNewFragment = ShopProductListLimitedNewFragment.createInstance(shopAttribution)
                shopProductListLimitedNewFragment.setPromoWebViewListener(webViewListener)
                return shopProductListLimitedNewFragment
            }
            else -> {
                //TODO go to product info fragment
                return Fragment()
            }
        }
    }

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}