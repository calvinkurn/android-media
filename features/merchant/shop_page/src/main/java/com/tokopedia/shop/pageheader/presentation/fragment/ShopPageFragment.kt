package com.tokopedia.shop.pageheader.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.TrackShopTypeDef
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import com.tokopedia.shop.pageheader.di.component.DaggerShopPageComponent
import com.tokopedia.shop.pageheader.di.component.ShopPageComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageModule
import com.tokopedia.shop.pageheader.presentation.ShopPageViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageFragmentPagerAdapter
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentHeaderViewHolder
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.fragment.HomeProductFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop.setting.view.activity.ShopPageSettingActivity
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
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
        HasComponent<ShopPageComponent>,
        ShopPageFragmentHeaderViewHolder.ShopPageFragmentViewHolderListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
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
        const val SHOP_STICKY_LOGIN = "SHOP_STICKY_LOGIN"
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
        private const val PATH_HOME = "home"
        private const val PATH_REVIEW = "review"
        private const val QUERY_SHOP_REF = "shop_ref"
        private const val QUERY_SHOP_ATTRIBUTION = "tracker_attribution"

        @JvmStatic
        fun createInstance() = ShopPageFragment()
    }

    private var initialFloatingChatButtonMarginBottom: Int = 0
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var shopViewModel: ShopPageViewModel

    private lateinit var remoteConfig: RemoteConfig
    private lateinit var cartLocalCacheHandler: LocalCacheHandler
    var shopPageTracking: ShopPageTrackingBuyer? = null
    var shopId: String? = null
    var shopRef: String = ""
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var isFirstCreateShop: Boolean = false
    var isShowFeed: Boolean = false
    var isOfficialStore: Boolean = false
    var isGoldMerchant: Boolean = false
    var createPostUrl: String = ""
    var shopName: String = ""
    private var tabPosition = TAB_POSITION_HOME
    lateinit var stickyLoginView: StickyLoginView
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private lateinit var shopPageFragmentHeaderViewHolder: ShopPageFragmentHeaderViewHolder
    private lateinit var viewPagerAdapter: ShopPageFragmentPagerAdapter
    private lateinit var errorTextView: TextView
    private lateinit var errorButton: View
    private val iconTabHome = R.drawable.ic_shop_tab_home_inactive
    private val iconTabProduct = R.drawable.ic_shop_tab_products_inactive
    private val iconTabFeed = R.drawable.ic_shop_tab_feed_inactive
    private val iconTabReview = R.drawable.ic_shop_tab_review_inactive
    private val intentData: Intent = Intent()
    private var isFirstLoading: Boolean = false
    private var shouldOverrideTabToHome: Boolean = false
    private var shouldOverrideTabToReview: Boolean = false
    private var listShopPageTabModel = listOf<ShopPageTabModel>()
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }


    val isMyShop: Boolean
        get() = if (::shopViewModel.isInitialized) {
            shopId?.let { shopViewModel.isMyShop(it) } ?: false
        } else false

    override fun getComponent() = activity?.run {
        DaggerShopPageComponent.builder().shopPageModule(ShopPageModule())
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
        shopViewModel.flush()
        super.onDestroy()
    }

    private fun initViews(view: View) {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
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
                viewPagerAdapter.handleSelectedTab(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab, true)
                if (isFirstLoading) {
                    isFirstLoading = false
                } else {
                    (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                        shopPageTracking?.clickTab(shopViewModel.isMyShop(it.shopCore.shopID),
                                listShopPageTabModel[tab.position].tabTitle,
                                CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                                        it.goldOS.isGold == 1))
                    }
                }
            }
        })
        swipeToRefresh.setOnRefreshListener {
            refreshData()
            updateStickyContent()
        }
        mainLayout.requestFocus()
        initStickyLogin(view)
        getChatButtonInitialMargin()
    }

    private fun getChatButtonInitialMargin() {
        val buttonChatLayoutParams = (button_chat.layoutParams as ViewGroup.MarginLayoutParams)
        initialFloatingChatButtonMarginBottom = buttonChatLayoutParams.bottomMargin
    }

    private fun openShopProductSortPage() {
        val intent = ShopProductSortActivity.createIntent(activity, "")
        startActivityForResult(intent, REQUEST_CODE_SORT)
    }

    private fun observeLiveData(owner: LifecycleOwner) {
        shopViewModel.shopFavouriteResp.observe(this, Observer {
            updateFavouriteResult(it.alreadyFavorited == 1)
            shopPageFragmentHeaderViewHolder.updateFavoriteData(it ?: ShopInfo.FavoriteData())
        })
        shopViewModel.shopInfoResp.observe(owner, Observer { result ->
            startShopPageHeaderMonitoringPltRenderPage()
            when (result) {
                is Success -> {
                    onSuccessGetShopInfo(result.data)
                }
                is Fail -> {
                    onErrorGetShopInfo(result.throwable)
                }
            }
            stopPerformanceMonitoring()
            stopShopPageHeaderMonitoringPltRenderPage()
        })

        shopViewModel.whiteListResp.observe(this, Observer { response ->
            when (response) {
                is Success -> onSuccessGetFeedWhitelist(response.data.first, response.data.second)
            }
        })

        shopViewModel.shopBadgeResp.observe(this, Observer { reputation ->
            if (!isOfficialStore) {
                reputation?.let {
                    shopPageFragmentHeaderViewHolder.showShopReputationBadges(it.second)
                }
            }
        })

        shopViewModel.shopTickerData.observe(this, Observer { response ->
            when(response){
                is Success -> shopPageFragmentHeaderViewHolder.updateShopTicker(
                        response.data.first,
                        response.data.second,
                        isMyShop
                )
            }
        })
    }

    private fun stopShopPageHeaderMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let {shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    private fun startShopPageHeaderMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let {shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            remoteConfig = FirebaseRemoteConfigImpl(it)
            cartLocalCacheHandler = LocalCacheHandler(it, CART_LOCAL_CACHE_NAME)
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            activity?.intent?.run {
                shopId = getStringExtra(SHOP_ID)
                shopRef = getStringExtra(SHOP_REF).orEmpty()
                shopDomain = getStringExtra(SHOP_DOMAIN)
                shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
                tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                isFirstCreateShop = getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_FIRST_CREATE_SHOP, false)
                data?.run {
                    if (shopId.isNullOrEmpty()) {
                        if (pathSegments.size > 1) {
                            shopId = pathSegments[1]
                        } else if (!getQueryParameter(SHOP_ID).isNullOrEmpty()) {
                            shopId = getQueryParameter(SHOP_ID)
                        }
                    }
                    if (shopDomain.isNullOrEmpty()) {
                        shopDomain = getQueryParameter(SHOP_DOMAIN)
                    }
                    if (lastPathSegment.orEmpty() == PATH_HOME) {
                        shouldOverrideTabToHome = true
                    }
                    if (lastPathSegment.orEmpty() == PATH_REVIEW) {
                        shouldOverrideTabToReview = true
                    }
                    shopRef = getQueryParameter(QUERY_SHOP_REF) ?: ""
                    shopAttribution = getQueryParameter(QUERY_SHOP_ATTRIBUTION) ?: ""
                }
            }
            shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageViewModel::class.java)
            initViews(view)
            observeLiveData(this)
            startPltNetworkPerformanceMonitoring()
            getShopInfo()
        }
    }

    private fun startPltNetworkPerformanceMonitoring() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let {shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
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
            updateStickyContent()
        })
        updateStickyContent()
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        isFirstLoading = true
        if (!swipeToRefresh.isRefreshing)
            setViewState(VIEW_LOADING)
        shopViewModel.getShop(shopId, shopDomain, isRefresh)
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
        if (isMyShop) {
            displayToolbarSeller()
        } else {
            displayToolbarBuyer()
        }
    }

    private fun displayToolbarSeller() {
        text_your_shop.show()
        searchBarLayout.hide()
    }

    private fun displayToolbarBuyer() {
        text_your_shop.hide()
        searchBarLayout.show()
        initSearchInputView()
    }

    private fun initSearchInputView() {
        searchBarText.setOnClickListener {
            clickSearch()
        }
        searchBarSort.setOnClickListener {
            clickSort()
        }
    }

    private fun clickSort() {
        shopPageTracking?.clickSort(isMyShop, customDimensionShopPage)
        openShopProductSortPage()
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
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let { shopInfo ->
                saveShopInfoModelToCacheManager(shopInfo)?.let { cacheManagerId ->
                    startActivity(ShopSearchProductActivity.createIntent(
                            context,
                            "",
                            cacheManagerId,
                            shopAttribution,
                            shopRef
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
            viewPagerAdapter = ShopPageFragmentPagerAdapter(this, childFragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isMyShop) {
            inflater.inflate(R.menu.menu_shop_page_fragment_seller, menu)
        } else {
            inflater.inflate(R.menu.menu_shop_page_fragment_buyer, menu)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
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
            R.id.menu_action_search -> clickSearch()
            R.id.menu_action_settings -> clickSettingButton()
            R.id.menu_action_cart -> redirectToCartPage()
            R.id.menu_action_shop_info -> redirectToShopInfoPage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clickSearch() {
        shopPageTracking?.clickSearch(isMyShop, customDimensionShopPage)
        redirectToShopSearchProduct()
    }

    private fun clickSettingButton() {
        shopPageTracking?.clickSettingButton(customDimensionShopPage)
        redirectToShopSettingsPage()
    }

    private fun redirectToShopInfoPage() {
        context?.let { context ->
            shopId?.let { shopId ->
                shopPageTracking?.clickShopProfile(customDimensionShopPage)
                RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_INFO, shopId)
            }
        }
    }

    private fun redirectToShopSettingsPage() {
        context?.let { context ->
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConstInternalSellerapp.MENU_SETTING)
            } else {
                shopId?.let { shopId ->
                    startActivity(ShopPageSettingActivity.createIntent(context, shopId))
                }
            }
        }
    }

    private fun redirectToCartPage() {
        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking?.clickCartButton(shopViewModel.isMyShop(it.shopCore.shopID),
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

    fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        with(shopInfo) {
            isOfficialStore = (goldOS.isOfficial == 1 && !TextUtils.isEmpty(shopInfo.topContent.topUrl))
            isGoldMerchant = (goldOS.isGoldBadge == 1)
            shopName = shopInfo.shopCore.name
            customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
            shopPageFragmentHeaderViewHolder.bind(this, shopViewModel.isMyShop(shopCore.shopID), remoteConfig)
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
                val shopType = when {
                    isOfficialStore -> TrackShopTypeDef.OFFICIAL_STORE
                    isGoldMerchant -> TrackShopTypeDef.GOLD_MERCHANT
                    else -> TrackShopTypeDef.REGULAR_MERCHANT
                }
                shopPageTracking?.sendScreenShopPage(shopCore.shopID, shopType)
            }
        }
        swipeToRefresh.isRefreshing = false
        view?.let { onToasterNoUploadProduct(it, getString(R.string.shop_page_product_no_upload_product), isFirstCreateShop) }

    }

    fun onBackPressed() {
        shopPageTracking?.clickBackArrow(isMyShop, customDimensionShopPage)
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
    }

    private fun setupTabs() {
        listShopPageTabModel = createListShopPageTabModel()
        viewPagerAdapter.setTabData(listShopPageTabModel)
        viewPagerAdapter.notifyDataSetChanged()
        var selectedPosition = getSelectedTabPosition()
        if(shouldOverrideTabToHome){
            selectedPosition = if(viewPagerAdapter.isFragmentObjectExists(HomeProductFragment::class.java)){
                viewPagerAdapter.getFragmentPosition(HomeProductFragment::class.java)
            }else{
                viewPagerAdapter.getFragmentPosition(ShopPageHomeFragment::class.java)
            }
        }
        if(shouldOverrideTabToReview){
            selectedPosition = if(viewPagerAdapter.isFragmentObjectExists((activity?.application as ShopModuleRouter).reviewFragmentClass)){
                viewPagerAdapter.getFragmentPosition((activity?.application as ShopModuleRouter).reviewFragmentClass)
            } else {
                selectedPosition
            }
        }
        tabLayout?.apply {
            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = viewPagerAdapter.getTabView(i, selectedPosition)
            }
        }
        setViewState(VIEW_CONTENT)
        viewPager.setCurrentItem(selectedPosition, false)
    }

    private fun createListShopPageTabModel(): List<ShopPageTabModel> {
        return mutableListOf<ShopPageTabModel>().apply {
            if (isShowHomeTab()){
                getHomeFragment()?.let{homeFragment ->
                    add(ShopPageTabModel(
                            getString(R.string.shop_info_title_tab_home),
                            iconTabHome,
                            homeFragment
                    ))
                }
            }
            val shopPageProductFragment = ShopPageProductListFragment.createInstance(shopAttribution, shopRef).apply {
                getShopInfoData()?.let {
                    setShopInfo(it)
                }
            }
            add(ShopPageTabModel(
                    getString(R.string.new_shop_info_title_tab_product),
                    iconTabProduct,
                    shopPageProductFragment
            ))
            if (isShowFeed){
                val feedFragment = FeedShopFragment.createInstance(shopId ?: "", createPostUrl)
                add(ShopPageTabModel(
                        getString(R.string.shop_info_title_tab_feed),
                        iconTabFeed,
                        feedFragment
                ))
            }
            if(activity?.application is ShopModuleRouter){
                val shopReviewFragment = (activity?.application as ShopModuleRouter).getReviewFragment(activity, shopId, shopDomain)
                add(ShopPageTabModel(
                        getString(R.string.shop_info_title_tab_review),
                        iconTabReview,
                        shopReviewFragment
                ))
            }
        }
    }

    private fun getSelectedTabPosition(): Int {
        return if (isShowHomeTab()) {
            if (isShowNewHomeTab()) {
                viewPagerAdapter.getFragmentPosition(ShopPageHomeFragment::class.java)
            } else {
                viewPagerAdapter.getFragmentPosition(ShopPageProductListFragment::class.java)
            }
        } else {
            viewPagerAdapter.getFragmentPosition(ShopPageProductListFragment::class.java)
        }
    }

    private fun isShowHomeTab(): Boolean {
        return getShopInfoData()?.shopHomeType?.let {
            it != ShopHomeType.NONE
        } ?: false
    }

    private fun isShowNewHomeTab(): Boolean {
        return getShopInfoData()?.shopHomeType?.let {
            it == ShopHomeType.NATIVE
        } ?: false
    }

    private fun getHomeFragment(): Fragment? {
        return if (isShowHomeTab()) {
            if (isShowNewHomeTab()) {
                ShopPageHomeFragment.createInstance(
                        shopId ?: "",
                        isOfficialStore,
                        isGoldMerchant,
                        shopName,
                        shopAttribution ?: "",
                        shopRef
                )
            } else {
                HomeProductFragment.createInstance().apply {
                    getShopInfoData()?.let {
                        setShopInfo(it)
                    }
                }
            }
        } else {
            null
        }
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        context?.run {
            setViewState(VIEW_ERROR)
            errorTextView.text = ErrorHandler.getErrorMessage(this, e)
            errorButton.setOnClickListener { getShopInfo(true) }
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun onSuccessToggleFavourite(successValue: Boolean) {
        if (successValue) {
            shopPageFragmentHeaderViewHolder.toggleFavourite()
            updateFavouriteResult(shopPageFragmentHeaderViewHolder.isShopFavourited())
        }
        shopPageFragmentHeaderViewHolder.updateFavoriteButton()
    }

    private fun updateFavouriteResult(isFavorite: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, intentData.apply {
                putExtra(SHOP_STATUS_FAVOURITE, isFavorite)
            })
        }
    }

    private fun updateStickyResult() {
        activity?.run {
            setResult(Activity.RESULT_OK, intentData.apply {
                putExtra(SHOP_STICKY_LOGIN, true)
            })
        }
    }

    private fun onErrorToggleFavourite(e: Throwable) {
        shopPageFragmentHeaderViewHolder.updateFavoriteButton()
        context?.let {
            if (e is UserNotLoginException) {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
                return
            }
        }

        activity?.run {
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
                val sortValue = it.getStringExtra(ShopProductSortActivity.SORT_VALUE)
                val sortName = it.getStringExtra(ShopProductSortActivity.SORT_NAME)
                shopPageTracking?.sortProduct(sortName, isMyShop, customDimensionShopPage)
                redirectToShopSearchProductResultPage(sortValue)
            }
        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
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
            shopPageTracking?.clickSortBy(isMyShop,
                    sortName, CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant))
            startActivity(ShopProductListActivity.createIntent(activity, shopId,
                    "", selectedEtalaseId, "", sortName, shopRef))
        }
    }

    private fun stopPerformanceMonitoring() {
        (activity as? ShopPageActivity)?.stopShopHeaderPerformanceMonitoring()
    }

    fun refreshData() {
        val f: Fragment? = viewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
        if (f != null && f is ShopPageProductListFragment) {
            f.clearCache()
        }
        val feedfragment: Fragment? = viewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_FEED + 1 else TAB_POSITION_FEED)
        if (feedfragment != null && feedfragment is FeedShopFragment) {
            feedfragment.clearCache()
        }

        if (isShowHomeTab() && isShowNewHomeTab()) {
            val shopPageHomeFragment: Fragment? = viewPagerAdapter.getRegisteredFragment(TAB_POSITION_HOME)
            if (shopPageHomeFragment != null && shopPageHomeFragment is ShopPageHomeFragment) {
                shopPageHomeFragment.clearCache()
            }
        }

        getShopInfo(true)
        swipeToRefresh.isRefreshing = true
    }

    override fun onFollowerTextClicked(shopFavourited: Boolean) {
        context?.run {
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                shopPageTracking?.clickFollowUnfollow(shopFavourited, customDimensionShopPage)
                startActivityForResult(ShopFavouriteListActivity.createIntent(this, it.shopCore.shopID),
                        REQUEST_CODE_FOLLOW)
            }
        }
    }

    private fun goToChatSeller() {
        context?.let { context ->
            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
                shopPageTracking?.clickMessageSeller(CustomDimensionShopPage.create(it.shopCore.shopID,
                        it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
                if (shopViewModel.isUserSessionActive) {
                    shopPageTracking?.eventShopSendChat()
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
            shopPageTracking?.clickFollowUnfollowShop(isFavourite,
                    CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1))

            shopPageTracking?.sendMoEngageFavoriteEvent(it.shopCore.name,
                    it.shopCore.shopID,
                    it.shopCore.domain,
                    it.location,
                    it.goldOS.isOfficial == 1,
                    isFavourite)

            shopViewModel.toggleFavorite(it.shopCore.shopID, this::onSuccessToggleFavourite,
                    this::onErrorToggleFavourite)
        }
    }

    override fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence) {
        context?.let {
            RouteManager.route(it, linkUrl.toString())
        }
    }

    override fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }

    private fun getShopInfoPosition(): Int = viewPagerAdapter.count - 1

    fun getShopInfoData() = (shopViewModel.shopInfoResp.value as? Success)?.data

    private fun updateStickyContent() {
        shopViewModel.getStickyLoginContent(
                onSuccess = {
                    this.tickerDetail = it
                    updateStickyState()
                    updateStickyResult()
                },
                onError = {
                    stickyLoginView.hide()
                }
        )
    }

    private fun updateFloatingChatButtonMargin() {
        val stickyLoginViewHeight = stickyLoginView.height.takeIf { stickyLoginView.isShowing() }
                ?: 0
        val buttonChatLayoutParams = (button_chat.layoutParams as ViewGroup.MarginLayoutParams)
        buttonChatLayoutParams.setMargins(
                buttonChatLayoutParams.leftMargin,
                buttonChatLayoutParams.topMargin,
                buttonChatLayoutParams.rightMargin,
                initialFloatingChatButtonMarginBottom + stickyLoginViewHeight
        )
        button_chat.layoutParams = buttonChatLayoutParams
    }

    private fun updateStickyState() {
        if (this.tickerDetail == null) {
            stickyLoginView.hide()
            updateViewPagerPadding()
            updateFloatingChatButtonMargin()
            return
        }

        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_SHOP, true)
        if (!isCanShowing) {
            stickyLoginView.hide()
            updateViewPagerPadding()
            updateFloatingChatButtonMargin()
            return
        }

        val userSession = UserSession(context)
        if (userSession.isLoggedIn) {
            stickyLoginView.hide()
            updateViewPagerPadding()
            updateFloatingChatButtonMargin()
            return
        }

        this.tickerDetail?.let { stickyLoginView.setContent(it) }
        stickyLoginView.show(StickyLoginConstant.Page.SHOP)
        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.SHOP)
        updateViewPagerPadding()
        updateFloatingChatButtonMargin()

    }

    private fun updateViewPagerPadding() {
        if (stickyLoginView.isShowing()) {
            viewPager.setPadding(0, 0, 0, stickyLoginView.height)
        } else {
            viewPager.setPadding(0, 0, 0, 0)
        }
    }

    private fun onToasterNoUploadProduct(view: View, message: String, isFirstCreateShop: Boolean) {
        if (isFirstCreateShop) {
            Toaster.make(view, message, actionText = getString(R.string.shop_page_product_action_no_upload_product), type = Toaster.TYPE_NORMAL)
            this.isFirstCreateShop = false
        }
    }
}
