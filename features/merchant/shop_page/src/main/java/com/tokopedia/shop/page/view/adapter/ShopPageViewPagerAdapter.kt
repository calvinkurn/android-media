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
import com.tokopedia.shop.product.view.fragment.HomeProductFragment
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment

class ShopPageViewPagerAdapter(val fragmentManager: FragmentManager,
                               var titles: Array<String>,
                               var shopId: String?,
                               val shopAttribution: String?,
                               val router: ShopModuleRouter,
                               private val shopPageActivity: ShopPageActivity) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        return if (shopPageActivity.isShowFeed) {
            if (shopPageActivity.isOfficialStore) {
                renderTabOsFeed(position)
            } else {
                renderTabFeed(position)
            }
        } else {
            if (shopPageActivity.isOfficialStore) {
                renderTabOsDefault(position)
            } else {
                renderTabDefault(position)
            }
        }
    }

    private fun renderTabOsDefault(position: Int): Fragment {
        return when (position) {
            0 -> {
                val f = HomeProductFragment.createInstance()
                shopPageActivity.getShopInfoData()?.run {
                    f.setShopInfo(this)
                }
                return f
            }
            1 -> {
                val f = ShopProductListLimitedFragment.createInstance(shopAttribution)
                shopPageActivity.getShopInfoData()?.run {
                    f.setShopInfo(this)
                }
                return f
            }
            2 -> {
                val f = ShopInfoFragment.createInstance()
                shopPageActivity.getShopInfoData()?.run {
                    f.shopInfo = this
                }
                return f
            }
            else -> Fragment()
        }
    }

    private fun renderTabOsFeed(position: Int): Fragment {
        return when (position) {
            0 -> {
                val f = HomeProductFragment.createInstance()
                shopPageActivity.getShopInfoData()?.run {
                    f.setShopInfo(this)
                }
                return f
            }
            1 -> {
                val f = ShopProductListLimitedFragment.createInstance(shopAttribution)
                shopPageActivity.getShopInfoData()?.run {
                    f.setShopInfo(this)
                }
                return f
            }
            2 -> {
                FeedShopFragment.createInstance(shopId ?: "", shopPageActivity.createPostUrl)
            }
            3 -> {
                val f = ShopInfoFragment.createInstance()
                shopPageActivity.getShopInfoData()?.run {
                    f.shopInfo = this
                }
                return f
            }
            else -> Fragment()
        }
    }

    private fun renderTabFeed(position: Int): Fragment {
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
    }

    private fun renderTabDefault(position: Int): Fragment {
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