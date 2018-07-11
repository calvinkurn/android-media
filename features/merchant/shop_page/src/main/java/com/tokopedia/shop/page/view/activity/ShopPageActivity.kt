package com.tokopedia.shop.page.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.page.view.adapter.ShopPageViewPagerAdapter
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView
import kotlinx.android.synthetic.main.activity_shop_page.*

class ShopPageActivity: BaseTabActivity(), ShopPagePromoWebView.Listener, HasComponent<ShopComponent> {

    override fun getComponent() = ShopComponentInstance.getComponent(application)

    override fun webViewTouched(touched: Boolean) {}

    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null

    companion object {
        const val PAGE_LIMIT = 2
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "EXTRA_SHOP_DOMAIN"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"

        fun createIntent(context: Context, shopId: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_ID, shopId) }

        fun createIntentWithDomain(context: Context, shopDomain: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_DOMAIN, shopDomain) }

    }

    override fun getViewPagerAdapter(): PagerAdapter {
        val titles = arrayOf(getString(R.string.shop_info_title_tab_product),
                getString(R.string.shop_info_title_tab_info))
        return ShopPageViewPagerAdapter(supportFragmentManager, titles, shopId, shopDomain, shopAttribution)
    }

    override fun getPageLimit() = PAGE_LIMIT

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_page
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE)
        tabLayout.setupWithViewPager(viewPager)
        setSupportActionBar(toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(SHOP_ID)
        shopDomain = intent.getStringExtra(SHOP_DOMAIN)
        shopAttribution = intent.getStringExtra(SHOP_ATTRIBUTION)
        super.onCreate(savedInstanceState)
    }
}