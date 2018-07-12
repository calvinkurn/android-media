package com.tokopedia.shop.page.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.page.di.component.DaggerShopPageComponent
import com.tokopedia.shop.page.di.module.ShopPageModule
import com.tokopedia.shop.page.view.adapter.ShopPageViewPagerAdapter
import com.tokopedia.shop.page.view.holder.ShopPageHeaderViewHolder
import com.tokopedia.shop.page.view.listener.ShopPageView
import com.tokopedia.shop.page.view.presenter.ShopPagePresenter
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView
import kotlinx.android.synthetic.main.activity_shop_page.*
import javax.inject.Inject

class ShopPageActivity: BaseSimpleActivity(), ShopPagePromoWebView.Listener, HasComponent<ShopComponent>, ShopPageView {

    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var shopInfo: ShopInfo? = null

    @Inject lateinit var presenter: ShopPagePresenter
    lateinit var shopPageViewHolder: ShopPageHeaderViewHolder

    lateinit var shopPageViewPagerAdapter: ShopPageViewPagerAdapter;

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "EXTRA_SHOP_DOMAIN"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"

        fun createIntent(context: Context, shopId: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_ID, shopId) }

        fun createIntentWithDomain(context: Context, shopDomain: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_DOMAIN, shopDomain) }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        shopId = intent.getStringExtra(SHOP_ID)
        shopDomain = intent.getStringExtra(SHOP_DOMAIN)
        shopAttribution = intent.getStringExtra(SHOP_ATTRIBUTION)
        super.onCreate(savedInstanceState)
        shopPageViewHolder = ShopPageHeaderViewHolder(shopPageHeader)
        initAdapter()
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = shopPageViewPagerAdapter

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE)
        tabLayout.setupWithViewPager(viewPager)

        getShopInfo()

    }

    private fun getShopInfo() {
        if (!TextUtils.isEmpty(shopId)){
            presenter.getShopInfo(shopId)
        } else {
            presenter.getShopInfoByDomain(shopDomain)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_page
    }

    private fun initInjector(){
        DaggerShopPageComponent.builder().shopPageModule(ShopPageModule())
                .shopComponent(component).build().inject(this)
        presenter.attachView(this)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    fun initAdapter(){
        val titles = arrayOf(getString(R.string.shop_info_title_tab_product),
                getString(R.string.shop_info_title_tab_info))
        shopPageViewPagerAdapter = ShopPageViewPagerAdapter(supportFragmentManager, titles, shopId,
                shopDomain, shopAttribution)
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo?) {
        shopInfo?.run {
            this@ShopPageActivity.shopInfo = this
            shopId = info.shopId
            shopDomain = info.shopDomain
            shopPageViewHolder.bind(this, presenter.isMyShop(shopId))
        }
    }

    override fun onErrorGetShopInfo(e: Throwable?) {

    }

    override fun onSuccessGetReputation(reputationSpeed: ReputationSpeed?) {

    }

    override fun onErrorGetReputation(e: Throwable?) {

    }

    override fun onSuccessToggleFavourite(successValue: Boolean) {

    }

    override fun onErrorToggleFavourite(e: Throwable?) {

    }

    override fun getComponent() = ShopComponentInstance.getComponent(application)

    override fun webViewTouched(touched: Boolean) {}
}