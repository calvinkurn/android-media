package com.tokopedia.shop.page.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Menu
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.shop.page.view.presenter.ShopPagePresenterNew
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedNewFragment
import kotlinx.android.synthetic.main.activity_shop_page.*
import kotlinx.android.synthetic.main.partial_shop_page_searchview.*
import javax.inject.Inject
import android.support.design.widget.AppBarLayout
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTracking
import com.tokopedia.shop.common.constant.ShopAppLink
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.info.view.fragment.ShopInfoFragmentNew

class ShopPageActivity: BaseSimpleActivity(), HasComponent<ShopComponent>,
        ShopPageView, ShopPageHeaderViewHolder.ShopPageHeaderListener {

    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var shopInfo: ShopInfo? = null

    @Inject lateinit var presenter: ShopPagePresenterNew
    @Inject lateinit var shopPageTracking: ShopPageTracking
    lateinit var shopPageViewHolder: ShopPageHeaderViewHolder

    lateinit var shopPageViewPagerAdapter: ShopPageViewPagerAdapter

    private val titles by lazy { arrayOf(getString(R.string.shop_info_title_tab_product),
            getString(R.string.shop_info_title_tab_info))}

    private var tabPosition = 0

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "EXTRA_SHOP_DOMAIN"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"
        const val APP_LINK_EXTRA_SHOP_ATTRIBUTION = "tracker_attribution"
        const val EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION"
        const val TAB_POSITION_HOME = 0
        const val TAB_POSITION_INFO = 1
        const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
        private const val REQUEST_CODER_USER_LOGIN = 100

        @JvmStatic
        fun createIntent(context: Context, shopId: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_ID, shopId) }

        @JvmStatic
        fun createIntentWithDomain(context: Context, shopDomain: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_DOMAIN, shopDomain) }
    }

    object DeepLinkIntents{
        @DeepLink(ShopAppLink.SHOP)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent{
            return Intent(context, ShopPageActivity::class.java)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                    .putExtras(extras)
        }

        @DeepLink(ShopAppLink.SHOP_INFO)
        @JvmStatic
        fun getCallingIntentInfoSelected(context: Context, extras: Bundle): Intent{
            return Intent(context, ShopPageActivity::class.java)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
                    .putExtras(extras)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        intent.run {
            shopId = getStringExtra(SHOP_ID)
            shopDomain = getStringExtra(SHOP_DOMAIN)
            shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
            tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
        }
        super.onCreate(savedInstanceState)
        shopPageViewHolder = ShopPageHeaderViewHolder(shopPageHeader, this)
        initAdapter()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = shopPageViewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        onProductListDetailFullyHide();
        viewPager.setCurrentItem(tabPosition)
        getShopInfo()

    }

    private fun onProductListDetailStartShow() {
        //hide tabs
        val params = tabLayout.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    }

    private fun onProductListDetailFullyHide() {
        //show tabs
        val params = tabLayout.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    }

    private fun getShopInfo() {
        if (!TextUtils.isEmpty(shopId)) {
            presenter.getShopInfo(shopId!!)
        } else {
            presenter.getShopInfoByDomain(shopDomain!!)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_page
    }

    private fun initInjector() {
        DaggerShopPageComponent.builder().shopPageModule(ShopPageModule())
                .shopComponent(component).build().inject(this)
        presenter.attachView(this)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    fun initAdapter() {
        shopPageViewPagerAdapter = ShopPageViewPagerAdapter(supportFragmentManager, titles,
                shopId, shopAttribution)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_shop_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            onShareShop()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onShareShop(){
        shopInfo?.run {
            shopPageTracking.eventClickShareShop(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))

            (application as ShopModuleRouter).goToShareShop(this@ShopPageActivity,
                    shopId, info.shopUrl, getString(R.string.shop_label_share_formatted,
                    MethodChecker.fromHtml(info.shopName).toString(), info.shopLocation))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        shopInfo?.run{
            shopPageTracking.eventBackPressed(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo?) {
        shopInfo?.run {
            this@ShopPageActivity.shopInfo = this
            shopId = info.shopId
            shopDomain = info.shopDomain
            shopPageViewHolder.bind(this, presenter.isMyShop(shopId!!))
            searchViewText.text = getString(R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(info.shopName).toString())

            (shopPageViewPagerAdapter.getRegisteredFragment(0) as ShopProductListLimitedNewFragment)
                    .displayProduct(this)

            (shopPageViewPagerAdapter.getRegisteredFragment(1) as ShopInfoFragmentNew)
                    .updateShopInfo(this)
        }
    }

    override fun onErrorGetShopInfo(e: Throwable?) {

    }

    override fun onSuccessGetReputation(reputationSpeed: ReputationSpeed?) {

    }

    override fun onErrorGetReputation(e: Throwable?) {

    }

    override fun onSuccessToggleFavourite(successValue: Boolean) {
        if (successValue) {
            shopPageViewHolder.toggleFavourite()
            updateFavouriteResult()
        }
        shopPageViewHolder.updateFavoriteButton()
    }

    private fun updateFavouriteResult() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(SHOP_STATUS_FAVOURITE, shopPageViewHolder.isShopFavourited())
        })
    }

    override fun onErrorToggleFavourite(e: Throwable) {
        shopPageViewHolder.updateFavoriteButton()
        if (e is UserNotLoginException) {
            val intent = (application as ShopModuleRouter).getLoginIntent(this)
            startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
            return
        }
        NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e))
    }

    override fun getComponent() = ShopComponentInstance.getComponent(application)

    override fun onFollowerTextClicked() {
        shopInfo?.run {
            shopPageTracking.eventClickListFavourite(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        startActivity(ShopFavouriteListActivity.createIntent(this, shopId))
    }

    override fun goToChatSeller() {
        shopInfo?.run {
            shopPageTracking.eventClickMessageShop(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))

            (application as ShopModuleRouter).goToChatSeller(this@ShopPageActivity, shopId,
                    MethodChecker.fromHtml(info.shopName).toString(), info.shopAvatar)
        }
    }

    override fun goToManageShop() {
        shopInfo?.run {
            shopPageTracking.eventClickShopSetting(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        (application as ShopModuleRouter).goToManageShop(this)
    }

    override fun toggleFavorite(isFavourite: Boolean) {
        shopInfo?.run {
            shopPageTracking.eventClickFavouriteShop(titles[viewPager.currentItem], shopId, isFavourite,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        shopId?.run {presenter.toggleFavouriteShop(this)}
    }

    override fun goToAddProduct() {
        shopInfo?.run {
            shopPageTracking.eventClickAddProduct(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        (application as ShopModuleRouter).goToAddProduct(this)
    }

    override fun openShop() {

    }

    override fun requestOpenShop() {

    }

    override fun goToHowActivate() {

    }
}