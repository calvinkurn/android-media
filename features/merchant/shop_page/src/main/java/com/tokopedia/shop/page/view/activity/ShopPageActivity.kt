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
import javax.inject.Inject
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTracking
import com.tokopedia.shop.common.constant.ShopAppLink
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.info.view.fragment.ShopInfoFragmentNew
import com.tokopedia.shop.product.view.activity.ShopProductListActivity

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent>,
        ShopPageView, ShopPageHeaderViewHolder.ShopPageHeaderListener {

    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var shopInfo: ShopInfo? = null
    var isTabHidden: Boolean = false
    var isTabFullyVisible: Boolean = true

    @Inject
    lateinit var presenter: ShopPagePresenterNew
    @Inject
    lateinit var shopPageTracking: ShopPageTracking
    lateinit var shopPageViewHolder: ShopPageHeaderViewHolder

    lateinit var shopPageViewPagerAdapter: ShopPageViewPagerAdapter

    private val titles by lazy {
        arrayOf(getString(R.string.shop_info_title_tab_product),
                getString(R.string.shop_info_title_tab_info))
    }

    private var tabPosition = 0

    private val errorTextView by lazy {
        findViewById<TextView>(R.id.message_retry)
    }

    private val errorButton by lazy {
        findViewById<Button>(R.id.button_retry)
    }

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
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        @JvmStatic
        fun createIntent(context: Context, shopId: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_ID, shopId) }

        @JvmStatic
        fun createIntentWithDomain(context: Context, shopDomain: String) = Intent(context, ShopPageActivity::class.java)
                .apply { putExtra(SHOP_DOMAIN, shopDomain) }
    }

    object DeepLinkIntents {
        @DeepLink(ShopAppLink.SHOP)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, ShopPageActivity::class.java)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                    .putExtras(extras)
        }

        @DeepLink(ShopAppLink.SHOP_INFO)
        @JvmStatic
        fun getCallingIntentInfoSelected(context: Context, extras: Bundle): Intent {
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

        appBarLayout.addOnOffsetChangedListener { _, verticalOffset -> swipeToRefresh.isEnabled = (verticalOffset == 0) }

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = shopPageViewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                shopInfo?.run {
                    shopPageTracking.eventClickTabShopPage(titles[tab.getPosition()], shopId,
                            presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
                }
            }
        })
        viewPager.currentItem = tabPosition
        swipeToRefresh.setOnRefreshListener { refreshData() }

        mainLayout.requestFocus();

        getShopInfo()
    }

    private fun getShopInfo() {
        setViewState(VIEW_LOADING)
        if (!TextUtils.isEmpty(shopId)) {
            presenter.getShopInfo(shopId!!)
        } else {
            presenter.getShopInfoByDomain(shopDomain!!)
        }
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                shopPageLoadingState.visibility = View.VISIBLE
                shopPageErrorState.visibility = View.GONE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
            }
            VIEW_ERROR -> {
                shopPageLoadingState.visibility = View.GONE
                shopPageErrorState.visibility = View.VISIBLE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
            }
            else -> {
                shopPageLoadingState.visibility = View.GONE
                shopPageErrorState.visibility = View.GONE
                appBarLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }
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

    override fun getNewFragment(): Fragment? = null

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

    fun onShareShop() {
        shopInfo?.run {
            shopPageTracking.eventClickShareShop(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))

            (application as ShopModuleRouter).goToShareShop(this@ShopPageActivity,
                    shopId, info.shopUrl, getString(R.string.shop_label_share_formatted,
                    MethodChecker.fromHtml(info.shopName).toString(), info.shopLocation))
        }

    }

    override fun onBackPressed() {
        shopInfo?.run {
            shopPageTracking.eventBackPressed(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        if (isTaskRoot) {
            val applink = if (GlobalConfig.isSellerApp()) ApplinkConst.SellerApp.SELLER_APP_HOME else ApplinkConst.HOME
            val router = applicationContext as ApplinkRouter
            if (router.isSupportApplink(applink)) {
                val intent = router.getApplinkIntent(this, applink)
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo?) {
        setViewState(VIEW_CONTENT)
        shopInfo?.run {
            this@ShopPageActivity.shopInfo = this
            shopId = info.shopId
            shopDomain = info.shopDomain
            shopPageViewHolder.bind(this, presenter.isMyShop(shopId!!))
            searchInputView.setSearchHint(getString(R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(info.shopName).toString()))
            searchInputView.setListener(object : SearchInputView.Listener {
                override fun onSearchSubmitted(text: String?) {
                    if (TextUtils.isEmpty(text)) {
                        return
                    }
                    shopPageTracking.eventTypeKeywordSearchProduct(getString(R.string.shop_info_title_tab_product),
                            text, info.shopId, presenter.isMyShop(info.shopId), ShopPageTracking.getShopType(info))
                    startActivity(ShopProductListActivity.createIntent(this@ShopPageActivity, info.shopId,
                            text, "", shopAttribution))
                    //reset the search, since the result will go to another activity.
                    searchInputView.searchTextView.text = null
                }

                override fun onSearchTextChanged(text: String?) {}

            })
            searchInputView.setOnClickListener {
                shopPageTracking.eventClickSearchProduct(getString(R.string.shop_info_title_tab_product),
                        info.shopId, presenter.isMyShop(info.shopId), ShopPageTracking.getShopType(info))
            }

            (shopPageViewPagerAdapter.getRegisteredFragment(0) as ShopProductListLimitedNewFragment)
                    .displayProduct(this)

            (shopPageViewPagerAdapter.getRegisteredFragment(1) as ShopInfoFragmentNew)
                    .updateShopInfo(this)
        }
        swipeToRefresh.isRefreshing = false
    }

    override fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        errorTextView.text = ErrorHandler.getErrorMessage(this, e)
        errorButton.setOnClickListener { getShopInfo() }
        swipeToRefresh.isRefreshing = false
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODER_USER_LOGIN) {
            refreshData()
        }
    }

    private fun refreshData() {
        presenter.clearCache()
        getShopInfo()
        swipeToRefresh.isRefreshing = true
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
        shopId?.run { presenter.toggleFavouriteShop(this) }
    }

    override fun goToAddProduct() {
        shopInfo?.run {
            shopPageTracking.eventClickAddProduct(titles[viewPager.currentItem], shopId,
                    presenter.isMyShop(shopId!!), ShopPageTracking.getShopType(info))
        }
        (application as ShopModuleRouter).goToAddProduct(this)
    }

    override fun openShop() {
        if (application is ShopModuleRouter) {
            (application as ShopModuleRouter).goToEditShop(this)
        }
    }

    override fun requestOpenShop() {

    }

    override fun goToHowActivate() {
        ShopWebViewActivity.startIntent(this, ShopUrl.SHOP_HELP_CENTER)
    }

    override fun goToHelpCenter(url: String) {
        ShopWebViewActivity.startIntent(this, url)
    }

}