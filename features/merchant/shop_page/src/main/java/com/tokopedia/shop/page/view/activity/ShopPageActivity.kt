package com.tokopedia.shop.page.view.activity

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SCREEN_SHOP_PAGE
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.shop.page.di.component.DaggerShopPageComponent
import com.tokopedia.shop.page.di.module.ShopPageModule
import com.tokopedia.shop.page.view.ShopPageViewModel
import com.tokopedia.shop.page.view.adapter.ShopPageViewPagerAdapter
import com.tokopedia.shop.page.view.holder.ShopPageHeaderViewHolder
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_shop_page.*
import kotlinx.android.synthetic.main.partial_shop_page_header.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent>,
    ShopPageHeaderViewHolder.ShopPageHeaderListener, ShopProductListFragment.OnShopProductListFragmentListener {

    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var isShowFeed: Boolean = false
    var isOfficialStore: Boolean = false
    var createPostUrl: String = ""
    private var performanceMonitoring: PerformanceMonitoring? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var shopViewModel: ShopPageViewModel

    lateinit var shopPageTracking: ShopPageTrackingBuyer
    lateinit var shopPageViewHolder: ShopPageHeaderViewHolder

    lateinit var shopPageViewPagerAdapter: ShopPageViewPagerAdapter
    lateinit var stickyLoginView: StickyLoginView
    private lateinit var titles: Array<String>

    private var tabPosition = 0
    lateinit var remoteConfig: RemoteConfig
    private lateinit var cartLocalCacheHandler: LocalCacheHandler

    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null

    private val errorTextView by lazy {
        findViewById<TextView>(R.id.message_retry)
    }

    private val errorButton by lazy {
        findViewById<Button>(R.id.button_retry)
    }

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "domain"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"
        const val APP_LINK_EXTRA_SHOP_ATTRIBUTION = "tracker_attribution"
        const val EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION"
        const val TAB_POSITION_OS_HOME = -1
        const val TAB_POSITION_HOME = 0
        const val TAB_POSITION_FEED = 1
        const val TAB_POSITION_INFO = 2
        const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
        const val SHOP_TRACE = "mp_shop"
        const val SHOP_NAME_PLACEHOLDER = "{{shop_name}}"
        const val SHOP_LOCATION_PLACEHOLDER = "{{shop_location}}"
        private const val REQUEST_CODER_USER_LOGIN = 100
        private const val REQUEST_CODE_FOLLOW = 101
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        private const val PAGE_LIMIT = 2

        private const val SOURCE_SHOP = "shop"

        private const val STICKY_SHOW_DELAY: Long = 3 * 60 * 1000

        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"

        @JvmStatic
        fun createIntent(context: Context, shopId: String) = Intent(context, ShopPageActivity::class.java)
            .apply { putExtra(SHOP_ID, shopId) }
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.SHOP)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, ShopPageActivity::class.java)
                .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                .putExtras(extras)
        }

        @DeepLink(ApplinkConst.SHOP_INFO)
        @JvmStatic
        fun getCallingIntentInfoSelected(context: Context, extras: Bundle): Intent {
            return Intent(context, ShopPageActivity::class.java)
                .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
                .putExtras(extras)
        }

        @DeepLink(ApplinkConst.SHOP_NOTE)
        @JvmStatic
        fun getCallingIntentNoteSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ShopPageActivity::class.java)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
        }

        @DeepLink(ApplinkConst.SHOP_HOME)
        @JvmStatic
        fun getCallingIntentHomeSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ShopPageActivity::class.java)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_OS_HOME)
        }
    }


    override fun updateUIByShopName(shopName: String) {
        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint_2,
            MethodChecker.fromHtml(shopName).toString()))
    }

    override fun updateUIByEtalaseName(etalaseName: String?) {
        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint_3,
            MethodChecker.fromHtml(etalaseName).toString()))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(this)
        initInjector()
        remoteConfig = FirebaseRemoteConfigImpl(this)
        cartLocalCacheHandler = LocalCacheHandler(this, CART_LOCAL_CACHE_NAME)
        performanceMonitoring = PerformanceMonitoring.start(SHOP_TRACE)
        shopPageTracking = ShopPageTrackingBuyer(
            TrackingQueue(this))

        titles = arrayOf(getString(R.string.shop_info_title_tab_product),
            getString(R.string.shop_info_title_tab_info))

        intent.run {
            shopId = getStringExtra(SHOP_ID)
            shopDomain = getStringExtra(SHOP_DOMAIN)
            shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
            tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
        }
        intent?.data?.run {
            if (shopId.isNullOrEmpty()) {
                shopId = getQueryParameter(SHOP_ID)
            }
            if (shopDomain.isNullOrEmpty()) {
                shopDomain = getQueryParameter(SHOP_DOMAIN)
            }
            if (shopAttribution.isNullOrEmpty()) {
                shopAttribution = getQueryParameter(SHOP_ATTRIBUTION)
            }
        }
        super.onCreate(savedInstanceState)
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageViewModel::class.java)
        shopViewModel.shopInfoResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })

        shopViewModel.shopFavouriteResp.observe(this, Observer {
            shopPageViewHolder.updateFavoriteData(it ?: ShopInfo.FavoriteData())
        })

        shopPageViewHolder = ShopPageHeaderViewHolder(shopPageHeader, this, shopPageTracking, this)
        initAdapter()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            swipeToRefresh.isEnabled = (verticalOffset == 0)
        })


        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = shopPageViewPagerAdapter
        viewPager.offscreenPageLimit = PAGE_LIMIT

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                    shopPageTracking.clickTab(shopViewModel.isMyShop(it.shopCore.shopID),
                        titles[tab.position],
                        CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1))

                    val shopInfoFragment: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(tab.position)
                    if (shopInfoFragment != null && shopInfoFragment is ShopInfoFragment) {
                        shopInfoFragment.updateShopInfo(it)
                    }
                }
            }
        })

        swipeToRefresh.setOnRefreshListener {
            refreshData()
            updateStickyContent()
        }

        mainLayout.requestFocus()

        shopViewModel.whiteListResp.observe(this, Observer { response ->
            when (response) {
                is Success -> onSuccessGetFeedWhitelist(response.data.first, response.data.second)
            }
        })

        shopViewModel.shopBadgeResp.observe(this, Observer { reputation ->
            reputation?.let {
                shopPageViewHolder.displayGeneral(it.second, it.first)
            }
        })

        shopViewModel.shopModerateResp.observe(this, Observer { shopModerate ->
            when (shopModerate) {
                is Success -> onSuccessGetModerateInfo(shopModerate.data)
                is Fail -> onErrorModerateListener(shopModerate.throwable)
            }
        })

        getShopInfo()

        stickyLoginView = findViewById(R.id.sticky_login_text)
        stickyLoginView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.SHOP)
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.SHOP)
            stickyLoginView.dismiss(StickyLoginConstant.Page.SHOP)
        })
        updateStickyContent()
        initSearchInputView()
    }

    private fun initSearchInputView() {
        searchInputView.searchTextView.movementMethod = null
        searchInputView.searchTextView.keyListener = null
        searchInputView.setOnClickListener {
            shopPageTracking.clickSearchBox(SCREEN_SHOP_PAGE)
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                saveShopInfoModelToCacheManager(it)?.let {
                    redirectToShopSearchProduct(it)
                }
            }
        }
    }

    private fun saveShopInfoModelToCacheManager(shopInfo: ShopInfo): String? {
        val cacheManager = SaveInstanceCacheManager(this, true)
        cacheManager.put(ShopInfo.TAG, shopInfo, TimeUnit.DAYS.toMillis(7))
        return cacheManager.id
    }

    private fun redirectToShopSearchProduct(cacheManagerId: String) {
        startActivity(ShopSearchProductActivity.createIntent(
            this,
            "",
            cacheManagerId,
            shopAttribution
        ))
    }

    override fun onResume() {
        super.onResume()
        updateStickyState()
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        setViewState(VIEW_LOADING)
        shopViewModel.getShop(shopId, shopDomain, isRefresh)
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
    }

    override fun getNewFragment(): Fragment? = null

    private fun initAdapter() {
        shopPageViewPagerAdapter = ShopPageViewPagerAdapter(supportFragmentManager,
            titles,
            shopId,
            shopAttribution,
            (application as ShopModuleRouter),
            this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_shop_page, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val userSession = UserSession(this)
        if (GlobalConfig.isSellerApp() || !remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SHOP, true)) {
            menu?.removeItem(R.id.action_cart)
        } else if (userSession.isLoggedIn) {
            showCartBadge(menu)
        }
        return true
    }

    private fun showCartBadge(menu: Menu?) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_cart_menu)
        if (drawable is LayerDrawable) {
            val countDrawable = CountDrawable(this)
            val cartCount = cartLocalCacheHandler.getInt(TOTAL_CART_CACHE_KEY, 0)
            countDrawable.setCount(cartCount.toString())
            drawable.mutate()
            drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
            menu?.findItem(R.id.action_cart)?.icon = drawable
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            onShareShop()
        } else if (item.itemId == R.id.action_cart) {
            onClickCart()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onShareShop() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickShareButton(shopViewModel.isMyShop(it.shopCore.shopID),
                CustomDimensionShopPage.create(it.shopCore.shopID,
                    it.goldOS.isOfficial == 1,
                    it.goldOS.isGold == 1))
            var shopShareMsg: String = remoteConfig.getString(RemoteConfigKey.SHOP_SHARE_MSG)
            if (!TextUtils.isEmpty(shopShareMsg)) {
                shopShareMsg = FindAndReplaceHelper.findAndReplacePlaceHolders(shopShareMsg,
                    SHOP_NAME_PLACEHOLDER, MethodChecker.fromHtml(it.shopCore.name).toString(),
                    SHOP_LOCATION_PLACEHOLDER, it.location)
            } else {
                shopShareMsg = getString(R.string.shop_label_share_formatted,
                    MethodChecker.fromHtml(it.shopCore.name).toString(), it.location)
            }
            (application as ShopModuleRouter).goToShareShop(this@ShopPageActivity,
                shopId, it.shopCore.url, shopShareMsg)
        }

    }

    private fun onClickCart() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickCartButton(shopViewModel.isMyShop(it.shopCore.shopID),
                CustomDimensionShopPage.create(it.shopCore.shopID,
                    it.goldOS.isOfficial == 1,
                    it.goldOS.isGold == 1))
            goToCart()
        }
    }

    private fun goToCart() {
        val userSession = UserSession(this)
        if (userSession.isLoggedIn) {
            startActivity(RouteManager.getIntent(this, ApplinkConst.CART))
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN),
                REQUEST_CODE_USER_LOGIN_CART)
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = RouteManager.getIntentNoFallback(this, ApplinkConst.HOME)
            if (intent != null) {
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    fun stopPerformanceMonitor() {
        performanceMonitoring?.stopTrace()
    }

    fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        with(shopInfo) {
            isOfficialStore = (goldOS.isOfficial == 1 && !TextUtils.isEmpty(shopInfo.topContent.topUrl))
            shopPageViewPagerAdapter.shopId = shopCore.shopID
            shopPageViewHolder.bind(this, shopViewModel.isMyShop(shopCore.shopID), remoteConfig)
            updateUIByShopName(shopCore.name)
            setupTabs()
            shopPageTracking.sendScreenShopPage(this@ShopPageActivity,
                    CustomDimensionShopPage.create(shopCore.shopID, goldOS.isOfficial == 1,
                            goldOS.isGold == 1))
            if (shopInfo.statusInfo.shopStatus != ShopStatusDef.OPEN) {
                shopViewModel.getModerateShopInfo()
            }
        }

        swipeToRefresh.isRefreshing = false
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking.sendAllTrackingQueue()
    }

    private fun setupTabs() {
        titles = when {
            isShowFeed and isOfficialStore -> {
                arrayOf(getString(R.string.shop_info_title_tab_home),
                    getString(R.string.shop_info_title_tab_product),
                    getString(R.string.shop_info_title_tab_feed),
                    getString(R.string.shop_info_title_tab_info))
            }
            isShowFeed -> {
                arrayOf(getString(R.string.shop_info_title_tab_product),
                    getString(R.string.shop_info_title_tab_feed),
                    getString(R.string.shop_info_title_tab_info))
            }
            isOfficialStore -> {
                arrayOf(getString(R.string.shop_info_title_tab_home),
                    getString(R.string.shop_info_title_tab_product),
                    getString(R.string.shop_info_title_tab_info))
            }
            else -> {
                arrayOf(getString(R.string.shop_info_title_tab_product),
                    getString(R.string.shop_info_title_tab_info))
            }
        }
        shopPageViewPagerAdapter.titles = titles
        shopPageViewPagerAdapter.notifyDataSetChanged()

        if (isOfficialStore && tabPosition == 0) {
            tabPosition = 1
        } else if (isOfficialStore && tabPosition == TAB_POSITION_OS_HOME) {
            tabPosition = 0
        }
        setViewState(VIEW_CONTENT)
        viewPager.currentItem = if (tabPosition == TAB_POSITION_INFO) getShopInfoPosition() else tabPosition
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        errorTextView.text = ErrorHandler.getErrorMessage(this, e)
        errorButton.setOnClickListener { getShopInfo() }
        swipeToRefresh.isRefreshing = false
    }

    private fun onSuccessGetModerateInfo(shopModerateRequestData: ShopModerateRequestData) {
        val statusModerate = shopModerateRequestData.shopModerateRequestStatus.result.status
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageViewHolder.updateViewModerateStatus(statusModerate, it, shopViewModel.isMyShop(it.shopCore.shopID))
        }
    }

    private fun onSuccessToggleFavourite(successValue: Boolean) {
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

    private fun onErrorToggleFavourite(e: Throwable) {
        shopPageViewHolder.updateFavoriteButton()
        if (e is UserNotLoginException) {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
            return
        }
        NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e))
    }

    private fun onSuccessGetFeedWhitelist(isWhitelist: Boolean, createPostUrl: String) {
        this.isShowFeed = isWhitelist
        this.createPostUrl = createPostUrl
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODER_USER_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
            }
        } else if (requestCode == REQUEST_CODE_FOLLOW) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
            }
        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                goToCart()
            }
        }
    }

    private fun refreshData() {
        val f: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
        if (f != null && f is ShopProductListLimitedFragment) {
            f.clearCache()
        }
        val feedfragment: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_FEED + 1 else TAB_POSITION_FEED)
        if (feedfragment != null && feedfragment is FeedShopFragment) {
            feedfragment.setRefresh()
        }

        getShopInfo(true)
        swipeToRefresh.isRefreshing = true
    }

    override fun getComponent() = ShopComponentInstance.getComponent(application)

    override fun onFollowerTextClicked() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickFollowerList(shopViewModel.isMyShop(it.shopCore.shopID),
                CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                    it.goldOS.isGold == 1))
            startActivityForResult(ShopFavouriteListActivity.createIntent(this, it.shopCore.shopID),
                REQUEST_CODE_FOLLOW)
        }

    }

    override fun goToChatSeller() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickMessageSeller(CustomDimensionShopPage.create(it.shopCore.shopID,
                it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))

            if (shopViewModel.isUserSessionActive) {
                shopPageTracking.eventShopSendChat()
                val intent = RouteManager.getIntent(this, ApplinkConst.TOPCHAT_ASKSELLER,
                    it.shopCore.shopID, "", SOURCE_SHOP, it.shopCore.name, it.shopAssets.avatar)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
            }
        }

    }

    override fun goToManageShop() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickManageShop(CustomDimensionShopPage.create(it.shopCore.shopID,
                it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
        }

        val intent = RouteManager.getIntent(this,
            if (GlobalConfig.isCustomerApp()) ApplinkConstInternalMarketplace.STORE_SETTING
            else ApplinkConsInternalHome.MANAGE_SHOP_SELLERAPP_TEMP) ?: return
        startActivity(intent)
    }

    override fun toggleFavorite(isFavourite: Boolean) {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickFollowUnfollowShop(isFavourite,
                CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                    it.goldOS.isGold == 1))

            sendMoEngageFavoriteEvent(it.shopCore.name,
                it.shopCore.shopID,
                it.shopCore.domain,
                it.location,
                it.goldOS.isOfficial == 1,
                isFavourite)

            shopViewModel.toggleFavorite(it.shopCore.shopID, this::onSuccessToggleFavourite,
                this::onErrorToggleFavourite)
        }
    }

    private fun sendMoEngageFavoriteEvent(shopName: String, shopID: String, shopDomain: String, shopLocation: String,
                                          isShopOfficaial: Boolean, isFollowed: Boolean) {
        TrackApp.getInstance().moEngage.sendTrackEvent(mapOf(
            "shop_name" to shopName,
            "shop_id" to shopID,
            "shop_location" to shopLocation,
            "url_slug" to shopDomain,
            "is_official_store" to isShopOfficaial),
            if (isFollowed)
                "Seller_Added_To_Favorite"
            else
                "Seller_Removed_From_Favorite")
    }

    override fun goToAddProduct() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickAddProduct(CustomDimensionShopPage.create(it.shopCore.shopID,
                it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
        }
        RouteManager.route(this, ApplinkConst.PRODUCT_ADD)
    }

    override fun openShop() {
        shopPageTracking.sendOpenShop();
        RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }


    private fun onErrorModerateListener(e: Throwable?) {
        val errorMessage = if (e == null) {
            getString(R.string.moderate_shop_error)
        } else {
            ErrorHandler.getErrorMessage(this, e)
        }

        ToasterError.make(window.decorView.rootView, errorMessage, BaseToaster.LENGTH_INDEFINITE)
            .setAction(R.string.title_ok) {}
            .show()
    }

    private fun onSuccessModerateListener() {
        buttonActionAbnormal.visibility = View.GONE
        ToasterNormal.make(window.decorView.rootView, getString(R.string.moderate_shop_success), BaseToaster.LENGTH_LONG)
            .setAction(R.string.title_ok) {}
            .show()
    }

    override fun requestOpenShop(shopId: Int, moderateNotes: String) {
        if (moderateNotes.isNotEmpty()) {
            shopViewModel.moderateShopRequest(shopId, moderateNotes, this::onSuccessModerateListener, this::onErrorModerateListener)
        }
    }

    override fun goToHowActivate() {
        ShopWebViewActivity.startIntent(this, ShopUrl.SHOP_HELP_CENTER)
    }

    override fun goToHelpCenter(url: String) {
        ShopWebViewActivity.startIntent(this, url)
    }

    private fun getShopInfoPosition(): Int = shopPageViewPagerAdapter.count - 1

    fun getShopInfoData() = (shopViewModel.shopInfoResp.value as? Success)?.data

    private fun updateStickyContent() {
        shopViewModel.getStickyLoginContent(
            onSuccess = {
                this.tickerDetail = it
                updateStickyState()
            },
            onError = {
                stickyLoginView.hide()
            }
        )
    }


    private fun updateStickyState() {
        if (this.tickerDetail == null) {
            stickyLoginView.hide()
            return
        }

        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_SHOP, true)
        if (!isCanShowing) {
            stickyLoginView.hide()
            return
        }

        val userSession = UserSession(this)
        if (userSession.isLoggedIn) {
            stickyLoginView.hide()
            return
        }

        this.tickerDetail?.let { stickyLoginView.setContent(it) }
        stickyLoginView.show(StickyLoginConstant.Page.SHOP)
        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.SHOP)

        if (stickyLoginView.isShowing()) {
            viewPager.setPadding(0, 0, 0, stickyLoginView.height)
        } else {
            viewPager.setPadding(0, 0, 0, 0)
        }
    }
}