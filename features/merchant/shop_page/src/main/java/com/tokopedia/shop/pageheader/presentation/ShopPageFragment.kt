package com.tokopedia.shop.pageheader.presentation

import android.os.Bundle
import android.view.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.error
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.di.DaggerShopPageComponent
import com.tokopedia.shop.pageheader.di.ShopPageComponent
import com.tokopedia.shop.pageheader.di.ShopPageModule
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPagePagerAdapter
import com.tokopedia.shop.pageheader.presentation.viewmodel.ShopPageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.shop_page_main.*
import javax.inject.Inject

class ShopPageFragment :
        BaseDaggerFragment(),
        HasComponent<ShopPageComponent> {

    companion object {
        @JvmStatic
        fun initInstance(bundle: Bundle?) = ShopPageFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModel: ShopPageViewModel
    private lateinit var remoteConfig: RemoteConfig
    private val tabAdapter by lazy {
        ShopPagePagerAdapter(context, childFragmentManager, viewModel.shopInfoResult)
    }



    override fun getComponent() = activity?.run {
        DaggerShopPageComponent
                .builder()
                .shopPageModule(ShopPageModule())
                .shopComponent(ShopComponentInstance.getComponent(application))
                .build()
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
        viewModel.shopInfoResult.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    private fun initViews() {
        shop_page_main_status_bar.visibility = View.GONE
        shop_page_main_toolbar.inflateMenu(R.menu.menu_shop_page_main)
        shop_page_main_viewpager.adapter = tabAdapter
        shop_page_main_tab_layout.apply {
            setupWithViewPager(shop_page_main_viewpager)
            tabRippleColor = null

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, false)
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, true)
                }
            })

            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = tabAdapter.getTabView(i)
            }
        }
    }

    private fun bindDataToHeaderView(shopInfo: ShopInfo) {
        shop_page_main_profile_name.text = shopInfo.shopCore.name

        ImageHandler.loadRoundedImage(
                shop_page_main_profile_image,
                shopInfo.shopAssets.avatar,
                32.dpToPx(resources.displayMetrics).toFloat(),
                -1,
                -1
        )

        ImageHandler.loadImageFitCenter(
                context,
                shop_page_main_profile_background,
                shopInfo.shopAssets.cover
        )
    }

    private fun networkFailureHandler(throwable: Throwable) {
        view?.run { Toaster.error(ErrorHandler.getErrorMessage(context, throwable)) }
    }

    private fun startObserveLiveDatas(owner: LifecycleOwner) {
        viewModel.shopInfoResult.observe(owner, Observer { result ->
            when (result) {
                is Success -> {
                    bindDataToHeaderView(result.data)
                }
                is Fail -> {
                    networkFailureHandler(result.throwable)
                }
            }
        })
    }

    fun updateUIByShopName(shopName: String) {
        shopPageFragmentSearchTextView.text = getString(
                R.string.shop_product_search_hint_2,
                MethodChecker.fromHtml(shopName).toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        GraphqlClient.init(this)
        remoteConfig = FirebaseRemoteConfigImpl(context)
        startObserveLiveDatas(this)
        viewModel.fetchData()
        initViews()
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        cartLocalCacheHandler = LocalCacheHandler(this, CART_LOCAL_CACHE_NAME)
//        performanceMonitoring = PerformanceMonitoring.start(SHOP_TRACE)
//        shopPageTracking = ShopPageTrackingBuyer(
//                TrackingQueue(this))
//
//        titles = arrayOf(getString(R.string.shop_info_title_tab_product),
//                getString(R.string.shop_info_title_tab_info))
//
//        intent.run {
//            shopId = getStringExtra(SHOP_ID)
//            shopDomain = getStringExtra(SHOP_DOMAIN)
//            shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
//            tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
//        }
//        intent?.data?.run {
//            if (shopId.isNullOrEmpty()) {
//                shopId = getQueryParameter(SHOP_ID)
//            }
//            if (shopDomain.isNullOrEmpty()) {
//                shopDomain = getQueryParameter(SHOP_DOMAIN)
//            }
//            if (shopAttribution.isNullOrEmpty()) {
//                shopAttribution = getQueryParameter(SHOP_ATTRIBUTION)
//            }
//        }
//        super.onCreate(savedInstanceState)
//        if(isNewShopPageEnabled())
//            return
//        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageViewModel::class.java)
//        shopViewModel.shopInfoResp.observe(this, Observer {
//            when (it) {
//                is Success -> onSuccessGetShopInfo(it.data)
//                is Fail -> onErrorGetShopInfo(it.throwable)
//            }
//        })
//
//        shopViewModel.shopFavouriteResp.observe(this, Observer {
//            shopPageViewHolder.updateFavoriteData(it ?: ShopInfo.FavoriteData())
//        })
//
//        shopPageViewHolder = ShopPageHeaderViewHolder(shopPageHeader, this, shopPageTracking, this)
//        initAdapter()
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//
//        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
//            swipeToRefresh.isEnabled = (verticalOffset == 0)
//        })
//
//
//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        viewPager.adapter = shopPageViewPagerAdapter
//        viewPager.offscreenPageLimit = PAGE_LIMIT
//
//        tabLayout.setupWithViewPager(viewPager)
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//                    shopPageTracking.clickTab(shopViewModel.isMyShop(it.shopCore.shopID),
//                            titles[tab.position],
//                            CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
//                                    it.goldOS.isGold == 1))
//
//                    val shopInfoFragment: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(tab.position)
//                    if (shopInfoFragment != null && shopInfoFragment is ShopInfoFragment) {
//                        shopInfoFragment.updateShopInfo(it)
//                    }
//                }
//            }
//        })
//
//        swipeToRefresh.setOnRefreshListener {
//            refreshData()
//            updateStickyContent()
//        }
//
//        mainLayout.requestFocus()
//
//        shopViewModel.whiteListResp.observe(this, Observer { response ->
//            when (response) {
//                is Success -> onSuccessGetFeedWhitelist(response.data.first, response.data.second)
//            }
//        })
//
//        shopViewModel.shopBadgeResp.observe(this, Observer { reputation ->
//            reputation?.let {
//                shopPageViewHolder.displayGeneral(it.second, it.first)
//            }
//        })
//
//        shopViewModel.shopModerateResp.observe(this, Observer { shopModerate ->
//            when (shopModerate) {
//                is Success -> onSuccessGetModerateInfo(shopModerate.data)
//                is Fail -> onErrorModerateListener(shopModerate.throwable)
//            }
//        })
//
//        getShopInfo()
//
//        stickyLoginView = findViewById(R.id.sticky_login_text)
//        stickyLoginView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            updateStickyState()
//        }
//        stickyLoginView.setOnClickListener {
//            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.SHOP)
//            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
//        }
//        stickyLoginView.setOnDismissListener(View.OnClickListener {
//            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.SHOP)
//            stickyLoginView.dismiss(StickyLoginConstant.Page.SHOP)
//        })
//        updateStickyContent()
//        initSearchInputView()
//    }
//
//    private fun initSearchInputView() {
//        searchInputView.searchTextView.movementMethod = null
//        searchInputView.searchTextView.keyListener = null
//        searchInputView.setOnClickListener {
//            shopPageTracking.clickSearchBox(SCREEN_SHOP_PAGE)
//            (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//                saveShopInfoModelToCacheManager(it)?.let {
//                    redirectToShopSearchProduct(it)
//                }
//            }
//        }
//    }
//
//    private fun saveShopInfoModelToCacheManager(shopInfo: ShopInfo): String? {
//        val cacheManager = SaveInstanceCacheManager(this, true)
//        cacheManager.put(ShopInfo.TAG, shopInfo, TimeUnit.DAYS.toMillis(7))
//        return cacheManager.id
//    }
//
//    private fun redirectToShopSearchProduct(cacheManagerId: String) {
//        startActivity(ShopSearchProductActivity.createIntent(
//                this,
//                "",
//                cacheManagerId,
//                shopAttribution
//        ))
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateStickyState()
//    }
//
//    private fun getShopInfo(isRefresh: Boolean = false) {
//        setViewState(VIEW_LOADING)
//        shopViewModel.getShop(shopId, shopDomain, isRefresh)
//    }
//
//    private fun setViewState(viewState: Int) {
//        when (viewState) {
//            VIEW_LOADING -> {
//                shopPageLoadingState.visibility = View.VISIBLE
//                shopPageErrorState.visibility = View.GONE
//                appBarLayout.visibility = View.INVISIBLE
//                viewPager.visibility = View.INVISIBLE
//            }
//            VIEW_ERROR -> {
//                shopPageLoadingState.visibility = View.GONE
//                shopPageErrorState.visibility = View.VISIBLE
//                appBarLayout.visibility = View.INVISIBLE
//                viewPager.visibility = View.INVISIBLE
//            }
//            else -> {
//                shopPageLoadingState.visibility = View.GONE
//                shopPageErrorState.visibility = View.GONE
//                appBarLayout.visibility = View.VISIBLE
//                viewPager.visibility = View.VISIBLE
//            }
//        }
//    }
//
//    override fun getLayoutRes(): Int {
//        return if (isNewShopPageEnabled()) {
//            super.getLayoutRes()
//        } else {
//            R.layout.activity_shop_page
//        }
//    }
//
//    private fun isNewShopPageEnabled() = remoteConfig.getBoolean(ENABLE_NEW_SHOP_PAGE, true)
//
//    override fun getNewFragment(): Fragment? {
//        return if (isNewShopPageEnabled()) {
//            val shopPageActivityIntentData = intent.extras
//            ShopPageFragment.initInstance(shopPageActivityIntentData)
//        } else {
//            null
//        }
//    }
//
//    private fun initAdapter() {
//        shopPageViewPagerAdapter = ShopPageViewPagerAdapter(supportFragmentManager,
//                titles,
//                shopId,
//                shopAttribution,
//                (application as ShopModuleRouter),
//                this)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_shop_page, menu)
//        return true
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        val userSession = UserSession(this)
//        if (GlobalConfig.isSellerApp() || !remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SHOP, true)) {
//            menu?.removeItem(R.id.action_cart)
//        } else if (userSession.isLoggedIn) {
//            showCartBadge(menu)
//        }
//        return true
//    }
//
//    private fun showCartBadge(menu: Menu?) {
//        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_cart_menu)
//        if (drawable is LayerDrawable) {
//            val countDrawable = CountDrawable(this)
//            val cartCount = cartLocalCacheHandler.getInt(TOTAL_CART_CACHE_KEY, 0)
//            countDrawable.setCount(cartCount.toString())
//            drawable.mutate()
//            drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
//            menu?.findItem(R.id.action_cart)?.icon = drawable
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.action_share) {
//            onShareShop()
//        } else if (item.itemId == R.id.action_cart) {
//            onClickCart()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun onShareShop() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickShareButton(shopViewModel.isMyShop(it.shopCore.shopID),
//                    CustomDimensionShopPage.create(it.shopCore.shopID,
//                            it.goldOS.isOfficial == 1,
//                            it.goldOS.isGold == 1))
//            var shopShareMsg: String = remoteConfig.getString(RemoteConfigKey.SHOP_SHARE_MSG)
//            if (!TextUtils.isEmpty(shopShareMsg)) {
//                shopShareMsg = FindAndReplaceHelper.findAndReplacePlaceHolders(shopShareMsg,
//                        SHOP_NAME_PLACEHOLDER, MethodChecker.fromHtml(it.shopCore.name).toString(),
//                        SHOP_LOCATION_PLACEHOLDER, it.location)
//            } else {
//                shopShareMsg = getString(R.string.shop_label_share_formatted,
//                        MethodChecker.fromHtml(it.shopCore.name).toString(), it.location)
//            }
//            (application as ShopModuleRouter).goToShareShop(this@ShopPageActivity,
//                    shopId, it.shopCore.url, shopShareMsg)
//        }
//
//    }
//
//    private fun onClickCart() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickCartButton(shopViewModel.isMyShop(it.shopCore.shopID),
//                    CustomDimensionShopPage.create(it.shopCore.shopID,
//                            it.goldOS.isOfficial == 1,
//                            it.goldOS.isGold == 1))
//            goToCart()
//        }
//    }
//
//    private fun goToCart() {
//        val userSession = UserSession(this)
//        if (userSession.isLoggedIn) {
//            startActivity(RouteManager.getIntent(this, ApplinkConst.CART))
//        } else {
//            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN),
//                    REQUEST_CODE_USER_LOGIN_CART)
//        }
//    }
//
//    override fun onBackPressed() {
//        if (isTaskRoot) {
//            val intent = RouteManager.getIntentNoFallback(this, ApplinkConst.HOME)
//            if (intent != null) {
//                startActivity(intent)
//                finish()
//            } else {
//                super.onBackPressed()
//            }
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    fun stopPerformanceMonitor() {
//        performanceMonitoring?.stopTrace()
//    }
//
//    fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
//        with(shopInfo) {
//            isOfficialStore = (goldOS.isOfficial == 1 && !TextUtils.isEmpty(shopInfo.topContent.topUrl))
//            shopPageViewPagerAdapter.shopId = shopCore.shopID
//            shopPageViewHolder.bind(this, shopViewModel.isMyShop(shopCore.shopID), remoteConfig)
//            if(buyer) updateUIByShopName(shopCore.name)
//            setupTabs()
//            shopPageTracking.sendScreenShopPage(this@ShopPageActivity,
//                    CustomDimensionShopPage.create(shopCore.shopID, goldOS.isOfficial == 1,
//                            goldOS.isGold == 1))
//            if (shopInfo.statusInfo.shopStatus != ShopStatusDef.OPEN) {
//                shopViewModel.getModerateShopInfo()
//            }
//        }
//
//        swipeToRefresh.isRefreshing = false
//    }
//
//    override fun onPause() {
//        super.onPause()
//        shopPageTracking.sendAllTrackingQueue()
//    }
//
//    private fun setupTabs() {
//        titles = when {
//            isShowFeed and isOfficialStore -> {
//                arrayOf(getString(R.string.shop_info_title_tab_home),
//                        getString(R.string.shop_info_title_tab_product),
//                        getString(R.string.shop_info_title_tab_feed),
//                        getString(R.string.shop_info_title_tab_info))
//            }
//            isShowFeed -> {
//                arrayOf(getString(R.string.shop_info_title_tab_product),
//                        getString(R.string.shop_info_title_tab_feed),
//                        getString(R.string.shop_info_title_tab_info))
//            }
//            isOfficialStore -> {
//                arrayOf(getString(R.string.shop_info_title_tab_home),
//                        getString(R.string.shop_info_title_tab_product),
//                        getString(R.string.shop_info_title_tab_info))
//            }
//            else -> {
//                arrayOf(getString(R.string.shop_info_title_tab_product),
//                        getString(R.string.shop_info_title_tab_info))
//            }
//        }
//        shopPageViewPagerAdapter.titles = titles
//        shopPageViewPagerAdapter.notifyDataSetChanged()
//
//        if (isOfficialStore && tabPosition == 0) {
//            tabPosition = 1
//        } else if (isOfficialStore && tabPosition == TAB_POSITION_OS_HOME) {
//            tabPosition = 0
//        }
//        setViewState(VIEW_CONTENT)
//        viewPager.currentItem = if (tabPosition == TAB_POSITION_INFO) getShopInfoPosition() else tabPosition
//    }
//
//    private fun onErrorGetShopInfo(e: Throwable?) {
//        setViewState(VIEW_ERROR)
//        errorTextView.text = ErrorHandler.getErrorMessage(this, e)
//        errorButton.setOnClickListener { getShopInfo() }
//        swipeToRefresh.isRefreshing = false
//    }
//
//    private fun onSuccessGetModerateInfo(shopModerateRequestData: ShopModerateRequestData) {
//        val statusModerate = shopModerateRequestData.shopModerateRequestStatus.result.status
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageViewHolder.updateViewModerateStatus(statusModerate, it, shopViewModel.isMyShop(it.shopCore.shopID))
//        }
//    }
//
//    private fun onSuccessToggleFavourite(successValue: Boolean) {
//        if (successValue) {
//            shopPageViewHolder.toggleFavourite()
//            updateFavouriteResult()
//        }
//        shopPageViewHolder.updateFavoriteButton()
//    }
//
//    private fun updateFavouriteResult() {
//        setResult(Activity.RESULT_OK, Intent().apply {
//            putExtra(SHOP_STATUS_FAVOURITE, shopPageViewHolder.isShopFavourited())
//        })
//    }
//
//    private fun onErrorToggleFavourite(e: Throwable) {
//        shopPageViewHolder.updateFavoriteButton()
//        if (e is UserNotLoginException) {
//            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
//            startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
//            return
//        }
//        NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e))
//    }
//
//    private fun onSuccessGetFeedWhitelist(isWhitelist: Boolean, createPostUrl: String) {
//        this.isShowFeed = isWhitelist
//        this.createPostUrl = createPostUrl
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODER_USER_LOGIN) {
//            if (resultCode == Activity.RESULT_OK) {
//                refreshData()
//            }
//        } else if (requestCode == REQUEST_CODE_FOLLOW) {
//            if (resultCode == Activity.RESULT_OK) {
//                refreshData()
//            }
//        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
//            if (resultCode == Activity.RESULT_OK) {
//                goToCart()
//            }
//        }
//    }
//
//    private fun refreshData() {
//        val f: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
//        if (f != null && f is ShopProductListLimitedFragment) {
//            f.clearCache()
//        }
//        val feedfragment: Fragment? = shopPageViewPagerAdapter.getRegisteredFragment(if (isOfficialStore) TAB_POSITION_FEED + 1 else TAB_POSITION_FEED)
//        if (feedfragment != null && feedfragment is FeedShopFragment) {
//            feedfragment.clearCache()
//        }
//
//        getShopInfo(true)
//        swipeToRefresh.isRefreshing = true
//    }
//
//    override fun onFollowerTextClicked() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickFollowerList(shopViewModel.isMyShop(it.shopCore.shopID),
//                    CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
//                            it.goldOS.isGold == 1))
//            startActivityForResult(ShopFavouriteListActivity.createIntent(this, it.shopCore.shopID),
//                    REQUEST_CODE_FOLLOW)
//        }
//
//    }
//
//    override fun goToChatSeller() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickMessageSeller(CustomDimensionShopPage.create(it.shopCore.shopID,
//                    it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
//
//            if (shopViewModel.isUserSessionActive) {
//                shopPageTracking.eventShopSendChat()
//                val intent = RouteManager.getIntent(this, ApplinkConst.TOPCHAT_ASKSELLER,
//                        it.shopCore.shopID, "", SOURCE_SHOP, it.shopCore.name, it.shopAssets.avatar)
//                startActivity(intent)
//            } else {
//                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
//            }
//        }
//
//    }
//
//    override fun goToManageShop() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickManageShop(CustomDimensionShopPage.create(it.shopCore.shopID,
//                    it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
//        }
//
//        val intent = RouteManager.getIntent(this,
//                if (GlobalConfig.isCustomerApp()) ApplinkConstInternalMarketplace.STORE_SETTING
//                else ApplinkConsInternalHome.MANAGE_SHOP_SELLERAPP_TEMP) ?: return
//        startActivity(intent)
//    }
//
//    override fun toggleFavorite(isFavourite: Boolean) {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickFollowUnfollowShop(isFavourite,
//                    CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
//                            it.goldOS.isGold == 1))
//
//            sendMoEngageFavoriteEvent(it.shopCore.name,
//                    it.shopCore.shopID,
//                    it.shopCore.domain,
//                    it.location,
//                    it.goldOS.isOfficial == 1,
//                    isFavourite)
//
//            shopViewModel.toggleFavorite(it.shopCore.shopID, this::onSuccessToggleFavourite,
//                    this::onErrorToggleFavourite)
//        }
//    }
//
//    private fun sendMoEngageFavoriteEvent(shopName: String, shopID: String, shopDomain: String, shopLocation: String,
//                                          isShopOfficaial: Boolean, isFollowed: Boolean) {
//        TrackApp.getInstance().moEngage.sendTrackEvent(mapOf(
//                "shop_name" to shopName,
//                "shop_id" to shopID,
//                "shop_location" to shopLocation,
//                "url_slug" to shopDomain,
//                "is_official_store" to isShopOfficaial),
//                if (isFollowed)
//                    "Seller_Added_To_Favorite"
//                else
//                    "Seller_Removed_From_Favorite")
//    }
//
//    override fun goToAddProduct() {
//        (shopViewModel.shopInfoResp.value as? Success)?.data?.let {
//            shopPageTracking.clickAddProduct(CustomDimensionShopPage.create(it.shopCore.shopID,
//                    it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
//        }
//        RouteManager.route(this, ApplinkConst.PRODUCT_ADD)
//    }
//
//    override fun openShop() {
//        shopPageTracking.sendOpenShop();
//        RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
//    }
//
//
//    private fun onErrorModerateListener(e: Throwable?) {
//        val errorMessage = if (e == null) {
//            getString(R.string.moderate_shop_error)
//        } else {
//            ErrorHandler.getErrorMessage(this, e)
//        }
//
//        ToasterError.make(window.decorView.rootView, errorMessage, BaseToaster.LENGTH_INDEFINITE)
//                .setAction(R.string.title_ok) {}
//                .show()
//    }
//
//    private fun onSuccessModerateListener() {
//        buttonActionAbnormal.visibility = View.GONE
//        ToasterNormal.make(window.decorView.rootView, getString(R.string.moderate_shop_success), BaseToaster.LENGTH_LONG)
//                .setAction(R.string.title_ok) {}
//                .show()
//    }
//
//    override fun requestOpenShop(shopId: Int, moderateNotes: String) {
//        if (moderateNotes.isNotEmpty()) {
//            shopViewModel.moderateShopRequest(shopId, moderateNotes, this::onSuccessModerateListener, this::onErrorModerateListener)
//        }
//    }
//
//    override fun goToHowActivate() {
//        ShopWebViewActivity.startIntent(this, ShopUrl.SHOP_HELP_CENTER)
//    }
//
//    override fun goToHelpCenter(url: String) {
//        ShopWebViewActivity.startIntent(this, url)
//    }
//
//    private fun getShopInfoPosition(): Int = shopPageViewPagerAdapter.count - 1
//
//    fun getShopInfoData() = (shopViewModel.shopInfoResp.value as? Success)?.data
//
//    private fun updateStickyContent() {
//        shopViewModel.getStickyLoginContent(
//                onSuccess = {
//                    this.tickerDetail = it
//                    updateStickyState()
//                },
//                onError = {
//                    stickyLoginView.hide()
//                }
//        )
//    }
//
//
//    private fun updateStickyState() {
//        if (this.tickerDetail == null) {
//            stickyLoginView.hide()
//            return
//        }
//
//        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_SHOP, true)
//        if (!isCanShowing) {
//            stickyLoginView.hide()
//            return
//        }
//
//        val userSession = UserSession(this)
//        if (userSession.isLoggedIn) {
//            stickyLoginView.hide()
//            return
//        }
//
//        this.tickerDetail?.let { stickyLoginView.setContent(it) }
//        stickyLoginView.show(StickyLoginConstant.Page.SHOP)
//        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.SHOP)
//
//        if (stickyLoginView.isShowing()) {
//            viewPager.setPadding(0, 0, 0, stickyLoginView.height)
//        } else {
//            viewPager.setPadding(0, 0, 0, 0)
//        }
//    }
}
