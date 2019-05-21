package com.tokopedia.shop.page.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.shop.page.view.activity.ShopPageActivity
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment

class ShopPageViewPagerAdapter(val fragmentManager: FragmentManager,
                               var titles: Array<String>,
                               var shopId: String?,
                               val shopAttribution: String?,
                               val router: ShopModuleRouter,
                               val shopPageActivity: ShopPageActivity) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        if (shopPageActivity.isShowFeed) {
            return when (position) {
                ShopPageActivity.TAB_POSITION_HOME -> {
                    val f = ShopProductListLimitedFragment.createInstance(shopAttribution)
                    shopPageActivity.getShopInfoData()?.run {
                        f.setShopInfo(this)
                    }
                    return f
                }
                ShopPageActivity.TAB_POSITION_FEED -> {
                    FeedShopFragment.createInstance(shopId ?: "", shopPageActivity.createPostUrl)
                }
                ShopPageActivity.TAB_POSITION_INFO -> {
                    val f = ShopInfoFragment.createInstance()
                    shopPageActivity.getShopInfoData()?.run {
                        f.shopInfo = this
                    }
                    return f
                }
                else -> Fragment()
            }
        } else {
            return when (position) {
                0 -> {
                    val f = ShopProductListLimitedFragment.createInstance(shopAttribution)
                    shopPageActivity.getShopInfoData()?.run {
                        f.setShopInfo(this)
                    }
                    return f
                }
                1 -> {
                    val f = ShopInfoFragment.createInstance()
                    shopPageActivity.getShopInfoData()?.run {
                        f.shopInfo = this
                    }
                    return f
                }
                else -> Fragment()
            }
        }
    }

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE;
    }

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