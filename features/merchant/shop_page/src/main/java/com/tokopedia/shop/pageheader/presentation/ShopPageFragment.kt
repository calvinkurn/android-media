package com.tokopedia.shop.pageheader.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
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
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.newproduct.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.oldpage.di.component.DaggerOldShopPageComponent
import com.tokopedia.shop.oldpage.di.component.OldShopPageComponent
import com.tokopedia.shop.oldpage.di.module.OldShopPageModule
import com.tokopedia.shop.oldpage.view.ShopPageViewModel
import com.tokopedia.shop.oldpage.view.activity.ShopWebViewActivity
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageFragmentPagerAdapter
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentHeaderViewHolder
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.fragment.HomeProductFragment
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.shop_page_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShopPageFragment :
        BaseDaggerFragment(),
        HasComponent<OldShopPageComponent>,
        ShopPageFragmentHeaderViewHolder.ShopPageFragmentViewHolderListener {
    override fun changeShopCover(isOfficial: Boolean, isPowerMerchant: Boolean) {
        if (!isOfficial && !isPowerMerchant) {
            view?.run {
                Toaster.make(
                        this,
                        context.getString(R.string.text_regular_merchant_change_cover_message),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        context.getString(R.string.oke)
                )
            }
        } else {
            redirectToSettingProfileShop()
        }
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
        private const val REQUEST_CODE_SORT = 300
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        private const val PAGE_LIMIT = 2

        private const val SOURCE_SHOP = "shop"

        private const val STICKY_SHOW_DELAY: Long = 3 * 60 * 1000

        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"

        @JvmStatic
        fun initInstance() = ShopPageFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var shopViewModel: ShopPageViewModel

    private lateinit var remoteConfig: RemoteConfig
    private lateinit var cartLocalCacheHandler: LocalCacheHandler
    private var performanceMonitoring: PerformanceMonitoring? = null
    lateinit var shopPageTracking: ShopPageTrackingBuyer
    var titles = listOf<String>()
    var shopId: String? = null
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var isShowFeed: Boolean = false
    var isOfficialStore: Boolean = false
    var isGoldMerchant: Boolean = false
    var createPostUrl: String = ""
    private var tabPosition = 0
    lateinit var stickyLoginView: StickyLoginView
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private lateinit var shopPageFragmentHeaderViewHolder: ShopPageFragmentHeaderViewHolder
    private lateinit var viewPagerAdapter: ShopPageFragmentPagerAdapter
    private lateinit var errorTextView: TextView
    private lateinit var errorButton: Button
    private val iconTabHome = R.drawable.ic_shop_tab_home_inactive
    private val iconTabProduct = R.drawable.ic_shop_tab_products_inactive
    private val iconTabFeed = R.drawable.ic_shop_tab_feed_inactive
    private val iconTabReview = R.drawable.ic_shop_tab_review_inactive


    val isMyShop: Boolean
        get() = if (::shopViewModel.isInitialized) {
            shopId?.let { shopViewModel.isMyShop(it) } ?: false
        } else false

    override fun getComponent() = activity?.run {
        DaggerOldShopPageComponent.builder().oldShopPageModule(OldShopPageModule())
                .shopComponent(ShopComponentInstance.getComponent(application)).build()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.shop_page_main, container, false)

    override fun onDestroy() {
        shopViewModel.shopInfoResp.removeObservers(this)
        shopViewModel.whiteListResp.removeObservers(this)
        shopViewModel.shopBadgeResp.removeObservers(this)
        shopViewModel.shopModerateResp.removeObservers(this)
        shopViewModel.shopFavouriteResp.removeObservers(this)
        shopViewModel.clear()
        super.onDestroy()
    }

    private fun initViews(view: View) {
        errorTextView = view.findViewById(R.id.message_retry)
        errorButton = view.findViewById(R.id.button_retry)
        shopPageFragmentHeaderViewHolder = ShopPageFragmentHeaderViewHolder(view, this, shopPageTracking, view.context)
        initToolbar()
        initAdapter()
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            swipeToRefresh.isEnabled = (verticalOffset == 0)
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = PAGE_LIMIT
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab,true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab,false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab,true)
                (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                    shopPageTracking.clickTab(shopViewModel.isMyShop(it.shopCore.shopID),
                            titles[tab.position],
                            CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                                    it.goldOS.isGold == 1))
                }
            }
        })
        swipeToRefresh.setOnRefreshListener {
            refreshData()
            updateStickyContent()
        }
        mainLayout.requestFocus()
        initStickyLogin(view)
        initSearchInputView()
    }

    private fun openShopProductSortPage() {
        val intent = ShopProductSortActivity.createIntent(activity, "")
        startActivityForResult(intent, REQUEST_CODE_SORT)
    }

    private fun observeLiveData(owner: LifecycleOwner) {
        shopViewModel.shopFavouriteResp.observe(this, Observer {
            shopPageFragmentHeaderViewHolder.updateFavoriteData(it ?: ShopInfo.FavoriteData())
        })
        shopViewModel.shopInfoResp.observe(owner, Observer { result ->
            when (result) {
                is Success -> {
                    onSuccessGetShopInfo(result.data)
                }
                is Fail -> {
                    onErrorGetShopInfo(result.throwable)
                }
            }
        })

        shopViewModel.whiteListResp.observe(this, Observer { response ->
            when (response) {
                is Success -> onSuccessGetFeedWhitelist(response.data.first, response.data.second)
            }
        })

        shopViewModel.shopBadgeResp.observe(this, Observer { reputation ->
            reputation?.let {
                shopPageFragmentHeaderViewHolder.showShopReputationBadges(it.second)
            }
        })

        shopViewModel.shopModerateResp.observe(this, Observer { shopModerate ->
            when (shopModerate) {
                is Success -> onSuccessGetModerateInfo(shopModerate.data)
                is Fail -> onErrorModerateListener(shopModerate.throwable)
            }
        })
    }

    fun updateUIByShopName(shopName: String) {
        searchBarText.text = getString(
                R.string.shop_product_search_hint_2,
                MethodChecker.fromHtml(shopName).toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            GraphqlClient.init(it)
            remoteConfig = FirebaseRemoteConfigImpl(it)
            cartLocalCacheHandler = LocalCacheHandler(it, CART_LOCAL_CACHE_NAME)
            performanceMonitoring = PerformanceMonitoring.start(SHOP_TRACE)
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            activity?.intent?.run {
                shopId = getStringExtra(SHOP_ID)
                shopDomain = getStringExtra(SHOP_DOMAIN)
                shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
                tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                data?.run {
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
            }
            shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageViewModel::class.java)
            initViews(view)
            observeLiveData(this)
            getShopInfo()
        }
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_text)
        stickyLoginView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.SHOP)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.SHOP)
            stickyLoginView.dismiss(StickyLoginConstant.Page.SHOP)
        })
        updateStickyContent()
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        setViewState(VIEW_LOADING)
        shopViewModel.getShop(shopId, shopDomain, isRefresh)
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(toolbar)
                supportActionBar?.title = getString(R.string.text_your_shop)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    private fun initSearchInputView() {
        if (isMyShop) {
            searchBarLayout.hide()
        } else {
            searchBarLayout.show()
            searchBarText.setOnClickListener {
                redirectToShopSearchProduct()
            }
            searchBarSort.setOnClickListener {
                openShopProductSortPage()
            }
        }
    }

    private fun saveShopInfoModelToCacheManager(shopInfo: ShopInfo): String? {
        return context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(ShopInfo.TAG, shopInfo, TimeUnit.DAYS.toMillis(7))
            cacheManager.id
        } ?: ""
    }

    private fun redirectToShopSearchProduct() {
        context?.let { context ->
            shopPageTracking.clickSearchBox(SCREEN_SHOP_PAGE)
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let { shopInfo ->
                saveShopInfoModelToCacheManager(shopInfo)?.let { cacheManagerId ->
                    startActivity(ShopSearchProductActivity.createIntent(
                            context,
                            "",
                            cacheManagerId,
                            shopAttribution
                    ))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateStickyState()
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                shopPageLoadingState.visibility = View.VISIBLE
                shopPageErrorState.visibility = View.GONE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
                button_chat.hide()
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

    private fun initAdapter() {
        activity?.run {
            viewPagerAdapter = ShopPageFragmentPagerAdapter(this, supportFragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (isMyShop) {
            inflater?.inflate(R.menu.menu_shop_page_fragment_seller, menu)
        } else {
            inflater?.inflate(R.menu.menu_shop_page_fragment_buyer, menu)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        context?.let {
            val userSession = UserSession(it)
            if (GlobalConfig.isSellerApp() || !remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SHOP, true)) {
                menu?.removeItem(R.id.action_cart)
            } else if (userSession.isLoggedIn) {
                showCartBadge(menu)
            } else {
            }
        }
    }

    private fun showCartBadge(menu: Menu?) {
        context?.let {
            val drawable = ContextCompat.getDrawable(it, R.drawable.ic_cart_menu)
            if (drawable is LayerDrawable) {
                val countDrawable = CountDrawable(it)
                val cartCount = cartLocalCacheHandler.getInt(TOTAL_CART_CACHE_KEY, 0)
                countDrawable.setCount(cartCount.toString())
                drawable.mutate()
                drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
                menu?.findItem(R.id.action_cart)?.icon = drawable
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_search -> redirectToShopSearchProduct()
            R.id.menu_action_settings -> redirectToShopSettingsPage()
            R.id.menu_action_cart -> redirectToCartPage()
            R.id.menu_action_shop_info -> redirectToShopInfoPage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun redirectToShopInfoPage() {
        view?.run {
            Toaster.make(
                    this,
                    "Open Shop Info",
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context.getString(R.string.oke)
            )
        }
    }

    private fun redirectToShopSettingsPage() {
        view?.run {
            Toaster.make(
                    this,
                    "Open Shop Settings",
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context.getString(R.string.oke)
            )
        }
    }

    private fun redirectToCartPage() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickCartButton(shopViewModel.isMyShop(it.shopCore.shopID),
                    CustomDimensionShopPage.create(it.shopCore.shopID,
                            it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1))
            goToCart()
        }
    }

    private fun goToCart() {
        context?.let {
            val userSession = UserSession(it)
            if (userSession.isLoggedIn) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        REQUEST_CODE_USER_LOGIN_CART)
            }
        }
    }

    fun stopPerformanceMonitor() {
        performanceMonitoring?.stopTrace()
    }

    fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        with(shopInfo) {
            isOfficialStore = (goldOS.isOfficial == 1 && !TextUtils.isEmpty(shopInfo.topContent.topUrl))
            isGoldMerchant = (goldOS.isGoldBadge == 1)
            shopPageFragmentHeaderViewHolder.bind(this, shopViewModel.isMyShop(shopCore.shopID), remoteConfig)
            updateUIByShopName(shopCore.name)
            setupTabs()
            if (!isMyShop) {
                button_chat.show()
                button_chat.setOnClickListener {
                    goToChatSeller()
                }
            } else {
                button_chat.hide()
            }
            activity?.run {
                shopPageTracking.sendScreenShopPage(this,
                        CustomDimensionShopPage.create(shopCore.shopID, goldOS.isOfficial == 1,
                                goldOS.isGold == 1))
            }
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
                listOf(getString(R.string.shop_info_title_tab_home),
                        getString(R.string.shop_info_title_tab_product),
                        getString(R.string.shop_info_title_tab_feed),
                        getString(R.string.shop_info_title_tab_info))
            }
            isShowFeed -> {
                listOf(getString(R.string.shop_info_title_tab_product),
                        getString(R.string.shop_info_title_tab_feed),
                        getString(R.string.shop_info_title_tab_info))
            }
            isOfficialStore -> {
                listOf(getString(R.string.shop_info_title_tab_home),
                        getString(R.string.shop_info_title_tab_product),
                        getString(R.string.shop_info_title_tab_info))
            }
            else -> {
                listOf(getString(R.string.shop_info_title_tab_product),
                        getString(R.string.shop_info_title_tab_info))
            }
        }
        viewPagerAdapter.setTabData(generateTabData())
        viewPagerAdapter.notifyDataSetChanged()
        tabLayout?.apply {
            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = viewPagerAdapter.getTabView(i)
            }
        }
        if (isOfficialStore && tabPosition == 0) {
            tabPosition = 1
        } else if (isOfficialStore && tabPosition == TAB_POSITION_OS_HOME) {
            tabPosition = 0
        }
        setViewState(VIEW_CONTENT)
        viewPager.currentItem = if (tabPosition == TAB_POSITION_INFO) getShopInfoPosition() else tabPosition
    }

    private fun generateTabData(): Pair<List<Int>, List<Fragment>> {
        return Pair(getListTitleIcon(), getListFragment())
    }

    private fun getListFragment(): List<Fragment> {
        return when {
            isShowFeed and isOfficialStore -> {
                val homeFragment = HomeProductFragment.createInstance()
                val shopPageProductFragment = ShopPageProductListFragment.createInstance(shopAttribution)
                val feedFragment = FeedShopFragment.createInstance(shopId ?: "", createPostUrl)
                val shopReviewFragment = (activity?.application as ShopModuleRouter).getReviewFragment(activity, shopId, shopDomain)
                getShopInfoData()?.run {
                    homeFragment.setShopInfo(this)
                    shopPageProductFragment.setShopInfo(this)
                }
                listOf(homeFragment, shopPageProductFragment, feedFragment, shopReviewFragment)
            }
            isShowFeed -> {
                val shopPageProductFragment = ShopPageProductListFragment.createInstance(shopAttribution)
                val feedFragment = FeedShopFragment.createInstance(shopId ?: "", createPostUrl)
                val shopReviewFragment = (activity?.application as ShopModuleRouter).getReviewFragment(activity, shopId, shopDomain)
                getShopInfoData()?.run {
                    shopPageProductFragment.setShopInfo(this)
                }
                listOf(shopPageProductFragment, feedFragment, shopReviewFragment)
            }
            isOfficialStore -> {
                val homeFragment = HomeProductFragment.createInstance()
                val shopPageProductFragment = ShopPageProductListFragment.createInstance(shopAttribution)
                val shopReviewFragment = (activity?.application as ShopModuleRouter).getReviewFragment(activity, shopId, shopDomain)
                getShopInfoData()?.run {
                    homeFragment.setShopInfo(this)
                    shopPageProductFragment.setShopInfo(this)
                }
                listOf(homeFragment, shopPageProductFragment, shopReviewFragment)
            }
            else -> {
                val shopPageProductFragment = ShopPageProductListFragment.createInstance(shopAttribution)
                val shopReviewFragment = (activity?.application as ShopModuleRouter).getReviewFragment(activity, shopId, shopDomain)
                getShopInfoData()?.run {
                    shopPageProductFragment.setShopInfo(this)
                }
                listOf(shopPageProductFragment, shopReviewFragment)
            }
        }
    }

    private fun getListTitleIcon(): List<Int> {
        return when {
            isShowFeed and isOfficialStore -> {
                listOf(iconTabHome, iconTabProduct, iconTabFeed, iconTabReview)
            }
            isShowFeed -> {
                listOf(iconTabProduct, iconTabFeed, iconTabReview)
            }
            isOfficialStore -> {
                listOf(iconTabHome, iconTabProduct, iconTabReview)
            }
            else -> {
                listOf(iconTabProduct, iconTabReview)
            }
        }
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        context?.run {
            setViewState(VIEW_ERROR)
            errorTextView.text = ErrorHandler.getErrorMessage(this, e)
            errorButton.setOnClickListener { getShopInfo() }
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun onSuccessGetModerateInfo(shopModerateRequestData: ShopModerateRequestData) {
        val statusModerate = shopModerateRequestData.shopModerateRequestStatus.result.status
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageFragmentHeaderViewHolder.updateViewModerateStatus(statusModerate, it, shopViewModel.isMyShop(it.shopCore.shopID))
        }
    }

    private fun onSuccessToggleFavourite(successValue: Boolean) {
        if (successValue) {
            shopPageFragmentHeaderViewHolder.toggleFavourite()
            updateFavouriteResult()
        }
        shopPageFragmentHeaderViewHolder.updateFavoriteButton()
    }

    private fun updateFavouriteResult() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(SHOP_STATUS_FAVOURITE, shopPageFragmentHeaderViewHolder.isShopFavourited())
            })
        }
    }

    private fun onErrorToggleFavourite(e: Throwable) {
        activity?.run {
            shopPageFragmentHeaderViewHolder.updateFavoriteButton()
            if (e is UserNotLoginException) {
                val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
                return
            }
            NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e))
        }
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
        } else if (requestCode == REQUEST_CODE_SORT) {
            data?.let {
                val sortName = it.getStringExtra(ShopProductSortActivity.SORT_NAME)
                redirectToShopSearchProductResultPage(sortName)
            }
        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                goToCart()
            }
        }
    }

    private fun redirectToShopSearchProductResultPage(sortName: String) {
        if (getShopInfoData() == null)
            return
        var selectedEtalaseId = ""
        for (pos in 0 until viewPagerAdapter.count) {
            val fragment = viewPagerAdapter.getRegisteredFragment(pos)
            if (fragment is ShopPageProductListFragment) {
                selectedEtalaseId = fragment.getSelectedEtalaseId()
            }
        }
        if (selectedEtalaseId.isNotEmpty()) {
            shopPageTracking.clickSortBy(isMyShop,
                    sortName, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant))
            startActivity(ShopProductListActivity.createIntent(activity, shopId,
                    "", selectedEtalaseId, "", sortName))
        }
    }

    private fun refreshData() {
        val f: Fragment? = viewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
        if (f != null && f is ShopPageProductListFragment) {
            f.clearCache()
        }
        val feedfragment: Fragment? = viewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_FEED + 1 else TAB_POSITION_FEED)
        if (feedfragment != null && feedfragment is FeedShopFragment) {
            feedfragment.clearCache()
        }
        getShopInfo(true)
        swipeToRefresh.isRefreshing = true
    }

    override fun onFollowerTextClicked() {
        context?.run {
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                shopPageTracking.clickFollowerList(shopViewModel.isMyShop(it.shopCore.shopID),
                        CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                                it.goldOS.isGold == 1))
                startActivityForResult(ShopFavouriteListActivity.createIntent(this, it.shopCore.shopID),
                        REQUEST_CODE_FOLLOW)
            }
        }
    }

    fun goToChatSeller() {
        context?.let { context ->
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                shopPageTracking.clickMessageSeller(CustomDimensionShopPage.create(it.shopCore.shopID,
                        it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
                if (shopViewModel.isUserSessionActive) {
                    shopPageTracking.eventShopSendChat()
                    val intent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT_ASKSELLER,
                            it.shopCore.shopID, "", SOURCE_SHOP, it.shopCore.name, it.shopAssets.avatar)
                    startActivity(intent)
                } else {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
                }
            }
        }
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

    override fun openShop() {
        context?.run {
            shopPageTracking.sendOpenShop();
            RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
        }
    }


    private fun onErrorModerateListener(e: Throwable?) {
        activity?.run {
            val errorMessage = if (e == null) {
                getString(R.string.moderate_shop_error)
            } else {
                ErrorHandler.getErrorMessage(this, e)
            }
            ToasterError.make(window.decorView.rootView, errorMessage, BaseToaster.LENGTH_INDEFINITE)
                    .setAction(R.string.title_ok) {}
                    .show()
        }
    }

    private fun onSuccessModerateListener() {
        activity?.run {
            ToasterNormal.make(window.decorView.rootView, getString(R.string.moderate_shop_success), BaseToaster.LENGTH_LONG)
                    .setAction(R.string.title_ok) {}
                    .show()
        }
    }

    override fun requestOpenShop(shopId: Int, moderateNotes: String) {
        if (moderateNotes.isNotEmpty()) {
            shopViewModel.moderateShopRequest(shopId, moderateNotes, this::onSuccessModerateListener, this::onErrorModerateListener)
        }
    }

    override fun goToHowActivate() {
        context?.run {
            ShopWebViewActivity.startIntent(this, ShopUrl.SHOP_HELP_CENTER)
        }
    }

    override fun goToHelpCenter(url: String) {
        context?.run {
            ShopWebViewActivity.startIntent(this, url)
        }
    }

    private fun getShopInfoPosition(): Int = viewPagerAdapter.count - 1

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

        val userSession = UserSession(context)
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

    private fun redirectToSettingProfileShop() {
        view?.run {
            Toaster.make(
                    this,
                    "To Shop Profile Setting",
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
            )
        }
    }
}
