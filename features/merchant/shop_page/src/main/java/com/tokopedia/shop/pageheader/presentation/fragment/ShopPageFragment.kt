package com.tokopedia.shop.pageheader.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.*
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
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.feedcomponent.util.util.ClipboardHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.TrackShopTypeDef
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.view.bottomsheet.ShopShareBottomSheet
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopShareModel
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import com.tokopedia.shop.pageheader.di.component.DaggerShopPageComponent
import com.tokopedia.shop.pageheader.di.component.ShopPageComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageModule
import com.tokopedia.shop.pageheader.presentation.ShopPageViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageFragmentPagerAdapter
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentHeaderViewHolder
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPagePerformanceMonitoringListener
import com.tokopedia.shop.product.view.fragment.HomeProductFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop.setting.view.activity.ShopPageSettingActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.shop_page_main.*
import java.io.File
import javax.inject.Inject

class ShopPageFragment :
        BaseDaggerFragment(),
        HasComponent<ShopPageComponent>,
        ShopPageFragmentHeaderViewHolder.ShopPageFragmentViewHolderListener,
        ShopShareBottomsheetListener {

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
        private const val START_PAGE = 1
        private const val DEFAULT_SORT_ID = 0

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
    var shopPageTrackingSGCPlay: ShopPageTrackingSGCPlayWidget? = null
    private var shopId = ""
    var shopRef: String = ""
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var isFirstCreateShop: Boolean = false
    var isShowFeed: Boolean = false
    var createPostUrl: String = ""
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
    private val permissionChecker: PermissionCheckerHelper = PermissionCheckerHelper()
    private var isFirstLoading: Boolean = false
    private var shouldOverrideTabToHome: Boolean = false
    private var isRefresh: Boolean = false
    private var shouldOverrideTabToReview: Boolean = false
    private var listShopPageTabModel = listOf<ShopPageTabModel>()
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(
                shopId,
                shopPageHeaderDataModel?.isOfficial ?: false,
                shopPageHeaderDataModel?.isGoldMerchant ?: false
        )
    }
    private var shopPageHeaderDataModel: ShopPageHeaderDataModel? = null
    private var initialProductListSortId: String = ""
    private var shopShareBottomSheet: ShopShareBottomSheet? = null
    private var shopImageFilePath: String = ""

    val isMyShop: Boolean
        get() = if (::shopViewModel.isInitialized) {
            shopViewModel.isMyShop(shopId)
        } else false

    override fun getComponent() = activity?.run {
        DaggerShopPageComponent.builder().shopPageModule(ShopPageModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this)).build()
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
        shopViewModel.shopPageP1Data.removeObservers(this)
        shopViewModel.shopPageHeaderContentData.removeObservers(this)
        shopViewModel.shopImagePath.removeObservers(this)
        shopViewModel.flush()
        super.onDestroy()
    }

    private fun initViews(view: View) {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        errorTextView = view.findViewById(com.tokopedia.abstraction.R.id.message_retry)
        errorButton = view.findViewById(com.tokopedia.abstraction.R.id.button_retry)
        shopPageFragmentHeaderViewHolder = ShopPageFragmentHeaderViewHolder(view, this, shopPageTracking, shopPageTrackingSGCPlay, view.context)
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
                    shopPageTracking?.clickTab(
                            shopViewModel.isMyShop(shopId),
                            listShopPageTabModel[tab.position].tabTitle,
                            CustomDimensionShopPage.create(
                                    shopId,
                                    shopPageHeaderDataModel?.isOfficial ?: false,
                                    shopPageHeaderDataModel?.isGoldMerchant ?: false
                            )
                    )
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

    private fun observeLiveData(owner: LifecycleOwner) {
        shopViewModel.shopPageP1Data.observe(owner, Observer { result ->
            startShopPageHeaderMonitoringPltRenderPage()
            when (result) {
                is Success -> {
                    onSuccessGetShopPageTabData(result.data)
                }
                is Fail -> {
                    onErrorGetShopPageTabData(result.throwable)
                }
            }
            stopPerformanceMonitoring()
            stopShopPageHeaderMonitoringPltRenderPage()
        })

        shopViewModel.shopPageHeaderContentData.observe(owner, Observer { result ->
            when (result) {
                is Success -> {
                    onSuccessGetShopPageHeaderContentData(result.data)
                }
                is Fail -> {
                    onErrorGetShopPageHeaderContentData(result.throwable)
                }
            }
        })

        shopViewModel.shopIdFromDomainData.observe(owner, Observer { result ->
            when (result) {
                is Success -> {
                    onSuccessGetShopIdFromDomain(result.data)
                }
                is Fail -> {
                    onErrorGetShopPageHeaderContentData(result.throwable)
                }
            }
        })

        shopViewModel.shopImagePath.observe(owner, Observer {
            shopImageFilePath = it
            if(shopImageFilePath.isNotEmpty()) {
                shopShareBottomSheet = ShopShareBottomSheet(context, fragmentManager, this).apply {
                    show()
                }
            }
        })

    }

    private fun onSuccessGetShopIdFromDomain(shopId: String) {
        this.shopId = shopId
        shopViewModel.getShopPageTabData(
                shopId.toIntOrZero(),
                shopDomain.orEmpty(),
                START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                initialProductListSortId.toIntOrZero(),
                "",
                "",
                isRefresh
        )
    }

    private fun onErrorGetShopPageHeaderContentData(error: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, error)
        view?.let { view ->
            Toaster.make(
                    view,
                    errorMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    getString(R.string.shop_page_retry),
                    View.OnClickListener {
                        getShopPageHeaderContentData()
                    })
        }
    }

    private fun getShopPageHeaderContentData() {
        shopViewModel.getShopPageHeaderContentData(shopId, shopDomain ?: "", isRefresh)
    }

    private fun stopShopPageHeaderMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    private fun startShopPageHeaderMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopPreparePltShopPage()
        context?.let {
            remoteConfig = FirebaseRemoteConfigImpl(it)
            cartLocalCacheHandler = LocalCacheHandler(it, CART_LOCAL_CACHE_NAME)
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            shopPageTrackingSGCPlay = ShopPageTrackingSGCPlayWidget(TrackingQueue(it))
            activity?.intent?.run {
                shopId = getStringExtra(SHOP_ID).orEmpty()
                shopRef = getStringExtra(SHOP_REF).orEmpty()
                shopDomain = getStringExtra(SHOP_DOMAIN)
                shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
                tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                isFirstCreateShop = getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_FIRST_CREATE_SHOP, false)
                data?.run {
                    if (shopId.isEmpty()) {
                        if (pathSegments.size > 1) {
                            shopId = pathSegments[1]
                        } else if (!getQueryParameter(SHOP_ID).isNullOrEmpty()) {
                            shopId = getQueryParameter(SHOP_ID)!!
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
            getInitialData()
        }
    }

    private fun startPltNetworkPerformanceMonitoring() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHeaderLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_text)
        stickyLoginView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateViewPagerPadding()
            updateFloatingChatButtonMargin()
        }
        stickyLoginView.setOnClickListener {
            if (stickyLoginView.isLoginReminder()) {
                stickyLoginView.trackerLoginReminder.clickOnLogin(StickyLoginConstant.Page.SHOP)
            } else {
                stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.SHOP)
            }
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            if (stickyLoginView.isLoginReminder()) {
                stickyLoginView.trackerLoginReminder.clickOnDismiss(StickyLoginConstant.Page.SHOP)
            } else {
                stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.SHOP)
            }
            stickyLoginView.dismiss(StickyLoginConstant.Page.SHOP)
            updateStickyContent()
        })
        updateStickyContent()
    }

    private fun getInitialData() {
        isFirstLoading = true
        if (!swipeToRefresh.isRefreshing)
            setViewState(VIEW_LOADING)
        startMonitoringNetworkPltShopPage()
        if(shopId.isEmpty()){
            shopViewModel.getShopIdFromDomain(shopDomain.orEmpty())
        }else{
            shopViewModel.getShopPageTabData(
                    shopId.toIntOrZero(),
                    shopDomain.orEmpty(),
                    START_PAGE,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    initialProductListSortId.toIntOrZero(),
                    "",
                    "",
                    isRefresh
            )
        }
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
    }

    private fun redirectToShopSearchProduct() {
        context?.let { context ->
            shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
                startActivity(ShopSearchProductActivity.createIntent(
                        context,
                        shopId,
                        shopPageHeaderDataModel.shopName,
                        shopPageHeaderDataModel.isOfficial,
                        shopPageHeaderDataModel.isGoldMerchant,
                        "",
                        shopAttribution,
                        shopRef
                ))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        removeTemporaryShopImage(shopImageFilePath)
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
                menu.removeItem(R.id.action_cart)
            } else if (userSession.isLoggedIn) {
                showCartBadge(menu)
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
        shopPageHeaderDataModel?.let {
            when (item.itemId) {
                R.id.menu_action_share, R.id.menu_action_share_seller_view -> clickShopShare()
                R.id.menu_action_search -> clickSearch()
                R.id.menu_action_settings -> clickSettingButton()
                R.id.menu_action_cart -> redirectToCartPage()
                R.id.menu_action_shop_info -> redirectToShopInfoPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeTemporaryShopImage(uri: String) {
        if(uri.isNotEmpty()) {
            File(uri).apply {
                if(exists()) {
                    delete()
                }
            }
        }
    }

    private fun clickShopShare() {
        if(isMyShop) {
            shopPageTracking?.clickShareButtonSellerView(customDimensionShopPage)
        } else {
            shopPageTracking?.clickShareButton(customDimensionShopPage)
        }
        removeTemporaryShopImage(shopImageFilePath)
        getWriteReadStoragePermission()
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
            shopPageTracking?.clickShopProfile(customDimensionShopPage)
            RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_INFO, shopId)
        }
    }

    private fun redirectToShopSettingsPage() {
        context?.let { context ->
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConstInternalSellerapp.MENU_SETTING)
            } else {
                startActivity(ShopPageSettingActivity.createIntent(context, shopId))
            }
        }
    }

    private fun redirectToCartPage() {
        shopPageTracking?.clickCartButton(
                shopViewModel.isMyShop(shopId),
                CustomDimensionShopPage.create(
                        shopId,
                        shopPageHeaderDataModel?.isOfficial ?: false,
                        shopPageHeaderDataModel?.isGoldMerchant ?: false
                )
        )
        goToCart()
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

    private fun getWriteReadStoragePermission() = activity?.let {
        permissionChecker.checkPermissions(it, arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                PermissionCheckerHelper.Companion.PERMISSION_READ_EXTERNAL_STORAGE
        ), object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                permissionChecker.onPermissionDenied(it, permissionText)
            }

            override fun onNeverAskAgain(permissionText: String) {
                permissionChecker.onNeverAskAgain(it, permissionText)
            }

            override fun onPermissionGranted() {
                saveShopImage()
            }
        })
    }

    private fun onSuccessGetShopPageTabData(shopPageP1Data: ShopPageP1HeaderData) {
        isShowFeed = shopPageP1Data.isWhitelist
        createPostUrl = shopPageP1Data.url
        shopPageHeaderDataModel = ShopPageHeaderDataModel().apply {
            shopId = this@ShopPageFragment.shopId
            isOfficial = shopPageP1Data.isOfficial
            isGoldMerchant = shopPageP1Data.isGoldMerchant
            shopHomeType = shopPageP1Data.shopHomeType
            topContentUrl = shopPageP1Data.topContentUrl
            shopName = shopPageP1Data.shopName
            shopDomain = shopPageP1Data.shopDomain
            avatar = shopPageP1Data.shopAvatar
        }
        customDimensionShopPage.updateCustomDimensionData(
                shopId,
                shopPageHeaderDataModel?.isOfficial ?: false,
                shopPageHeaderDataModel?.isGoldMerchant ?: false
        )
        val shopType = when {
            shopPageHeaderDataModel?.isOfficial ?: false -> TrackShopTypeDef.OFFICIAL_STORE
            shopPageHeaderDataModel?.isGoldMerchant ?: false -> TrackShopTypeDef.GOLD_MERCHANT
            else -> TrackShopTypeDef.REGULAR_MERCHANT
        }
        shopPageTracking?.sendScreenShopPage(shopId, shopType)
        getShopPageHeaderContentData()
        setupTabs()
        setViewState(VIEW_CONTENT)
        swipeToRefresh.isRefreshing = false
        shopPageHeaderDataModel?.let{
            shopPageFragmentHeaderViewHolder.bind(it, isMyShop, remoteConfig)
        }
    }

    protected fun stopPreparePltShopPage(){
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
            }
        }
    }

    protected fun startMonitoringNetworkPltShopPage(){
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun onSuccessGetShopPageHeaderContentData(shopPageHeaderContentData: ShopPageHeaderContentData) {
        shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            shopPageHeaderDataModel.location = shopPageHeaderContentData.shopInfo.location
            shopPageHeaderDataModel.isFreeOngkir = shopPageHeaderContentData.shopInfo.freeOngkir.isActive
            shopPageHeaderDataModel.statusTitle = shopPageHeaderContentData.shopInfo.statusInfo.statusTitle
            shopPageHeaderDataModel.statusMessage = shopPageHeaderContentData.shopInfo.statusInfo.statusMessage
            shopPageHeaderDataModel.shopStatus = shopPageHeaderContentData.shopInfo.statusInfo.shopStatus
            shopPageHeaderDataModel.broadcaster = shopPageHeaderContentData.broadcasterConfig
            shopPageHeaderDataModel.shopSnippetUrl = shopPageHeaderContentData.shopInfo.shopSnippetUrl
            shopPageHeaderDataModel.shopCoreUrl = shopPageHeaderContentData.shopInfo.shopCore.url
            if (!isMyShop) {
                button_chat.show()
                button_chat.setOnClickListener {
                    goToChatSeller()
                }
            } else {
                button_chat.hide()
            }
            updateFavouriteResult(shopPageHeaderContentData.favoriteData.alreadyFavorited == 1)
            shopPageFragmentHeaderViewHolder.bind(shopPageHeaderDataModel, isMyShop, remoteConfig)
            shopPageFragmentHeaderViewHolder.updateFavoriteData(shopPageHeaderContentData.favoriteData)
            if (!shopPageHeaderDataModel.isOfficial) {
                shopPageFragmentHeaderViewHolder.showShopReputationBadges(shopPageHeaderContentData.shopBadge)
            }
            shopPageFragmentHeaderViewHolder.updateShopTicker(
                    shopPageHeaderDataModel,
                    shopPageHeaderContentData.shopOperationalHourStatus,
                    isMyShop
            )
            view?.let { onToasterNoUploadProduct(it, getString(R.string.shop_page_product_no_upload_product), isFirstCreateShop) }
        }
    }

    fun onBackPressed() {
        shopPageTracking?.clickBackArrow(isMyShop, customDimensionShopPage)
        removeTemporaryShopImage(shopImageFilePath)
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
        if (shouldOverrideTabToHome) {
            selectedPosition = if (viewPagerAdapter.isFragmentObjectExists(HomeProductFragment::class.java)) {
                viewPagerAdapter.getFragmentPosition(HomeProductFragment::class.java)
            } else {
                viewPagerAdapter.getFragmentPosition(ShopPageHomeFragment::class.java)
            }
        }
        if(shouldOverrideTabToReview){
            selectedPosition = if(viewPagerAdapter.isFragmentObjectExists(ReviewShopFragment::class.java)){
                viewPagerAdapter.getFragmentPosition(ReviewShopFragment::class.java)
            } else {
                selectedPosition
            }
        }
        tabLayout?.apply {
            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = viewPagerAdapter.getTabView(i, selectedPosition)
            }
        }
        viewPager.setCurrentItem(selectedPosition, false)
    }

    private fun createListShopPageTabModel(): List<ShopPageTabModel> {
        return mutableListOf<ShopPageTabModel>().apply {
            if (isShowHomeTab()) {
                getHomeFragment()?.let { homeFragment ->
                    add(ShopPageTabModel(
                            getString(R.string.shop_info_title_tab_home),
                            iconTabHome,
                            homeFragment
                    ))
                }
            }
            val shopPageProductFragment = ShopPageProductListFragment.createInstance(
                    shopId,
                    shopPageHeaderDataModel?.shopName.orEmpty(),
                    shopPageHeaderDataModel?.isOfficial ?: false,
                    shopPageHeaderDataModel?.isGoldMerchant ?: false,
                    shopPageHeaderDataModel?.shopHomeType.orEmpty(),
                    shopAttribution,
                    shopRef
            )
            shopPageProductFragment.setInitialProductListData(
                    shopViewModel.productListData
            )
            add(ShopPageTabModel(
                    getString(R.string.new_shop_info_title_tab_product),
                    iconTabProduct,
                    shopPageProductFragment
            ))
            if (isShowFeed) {
                val feedFragment = FeedShopFragment.createInstance(
                        shopId,
                        createPostUrl
                )
                add(ShopPageTabModel(
                        getString(R.string.shop_info_title_tab_feed),
                        iconTabFeed,
                        feedFragment
                ))
            }
            val shopReviewFragment = ReviewShopFragment.createInstance(
                    shopId,
                    shopDomain
            )
            add(ShopPageTabModel(
                    getString(R.string.shop_info_title_tab_review),
                    iconTabReview,
                    shopReviewFragment
            ))
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
        return (shopPageHeaderDataModel?.shopHomeType.orEmpty() != ShopHomeType.NONE)
    }

    private fun isShowNewHomeTab(): Boolean {
        return (shopPageHeaderDataModel?.shopHomeType.orEmpty() == ShopHomeType.NATIVE)
    }

    private fun getHomeFragment(): Fragment? {
        return if (isShowHomeTab()) {
            if (isShowNewHomeTab()) {
                ShopPageHomeFragment.createInstance(
                        shopId,
                        shopPageHeaderDataModel?.isOfficial ?: false,
                        shopPageHeaderDataModel?.isGoldMerchant ?: false,
                        shopPageHeaderDataModel?.shopName.orEmpty(),
                        shopAttribution ?: "",
                        shopRef
                )
            } else {
                HomeProductFragment.createInstance(
                        shopId,
                        shopPageHeaderDataModel?.topContentUrl.orEmpty()
                )
            }
        } else {
            null
        }
    }

    private fun onErrorGetShopPageTabData(e: Throwable?) {
        context?.run {
            setViewState(VIEW_ERROR)
            errorTextView.text = ErrorHandler.getErrorMessage(this, e)
            errorButton.setOnClickListener {
                isRefresh = true
                getInitialData()
            }
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
        }
        else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
                goToCart()
            }
        }
    }

    private fun stopPerformanceMonitoring() {
        (activity as? ShopPageActivity)?.stopShopHeaderPerformanceMonitoring()
    }

    fun refreshData() {
        val shopProductListFragment: Fragment? = viewPagerAdapter.getRegisteredFragment(if (shopPageHeaderDataModel?.isOfficial == true) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
        if (shopProductListFragment is ShopPageProductListFragment) {
            shopProductListFragment.clearCache()
        }
        val feedfragment: Fragment? = viewPagerAdapter.getRegisteredFragment(if (shopPageHeaderDataModel?.isOfficial == true) TAB_POSITION_FEED + 1 else TAB_POSITION_FEED)
        if (feedfragment is FeedShopFragment) {
            feedfragment.clearCache()
        }

        val shopPageHomeFragment: Fragment? = viewPagerAdapter.getRegisteredFragment(TAB_POSITION_HOME)
        if (shopPageHomeFragment is ShopPageHomeFragment) {
            shopPageHomeFragment.clearCache()
        }
        isRefresh = true
        getInitialData()
        swipeToRefresh.isRefreshing = true
    }

    override fun onFollowerTextClicked(shopFavourited: Boolean) {
        context?.run {
            shopPageTracking?.clickFollowUnfollow(shopFavourited, customDimensionShopPage)
            startActivityForResult(
                    ShopFavouriteListActivity.createIntent(this,
                            shopId
                    ),
                    REQUEST_CODE_FOLLOW
            )
        }
    }

    private fun goToChatSeller() {
        context?.let { context ->
            shopPageTracking?.clickMessageSeller(CustomDimensionShopPage.create(
                    shopId,
                    shopPageHeaderDataModel?.isOfficial ?: false,
                    shopPageHeaderDataModel?.isGoldMerchant ?: false
            ))
            if (shopViewModel.isUserSessionActive) {
                shopPageTracking?.eventShopSendChat()
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ASKSELLER,
                        shopId,
                        "",
                        SOURCE_SHOP,
                        shopPageHeaderDataModel?.shopName.orEmpty(),
                        shopPageHeaderDataModel?.avatar.orEmpty()
                )
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
            }
        }
    }

    override fun toggleFavorite(isFavourite: Boolean) {
        shopPageTracking?.clickFollowUnfollowShop(
                isFavourite,
                CustomDimensionShopPage.create(
                        shopId,
                        shopPageHeaderDataModel?.isOfficial ?: false,
                        shopPageHeaderDataModel?.isGoldMerchant ?: false
                )
        )

        shopPageTracking?.sendMoEngageFavoriteEvent(
                shopPageHeaderDataModel?.shopName.orEmpty(),
                shopId,
                shopPageHeaderDataModel?.domain.orEmpty(),
                shopPageHeaderDataModel?.location.orEmpty(),
                shopPageHeaderDataModel?.isOfficial ?: false,
                isFavourite
        )

        shopViewModel.toggleFavorite(
                shopId,
                this::onSuccessToggleFavourite,
                this::onErrorToggleFavourite
        )
    }

    override fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence) {
        context?.let {
            RouteManager.route(it, linkUrl.toString())
        }
    }

    override fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }

    override fun onCloseBottomSheet() {
        shopPageTracking?.clickCancelShareBottomsheet(customDimensionShopPage, isMyShop)
    }

    override fun onItemBottomsheetShareClicked(shopShare: ShopShareModel) {
        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.SHOP_TYPE
            uri = shopPageHeaderDataModel?.shopCoreUrl
            id = shopPageHeaderDataModel?.shopId
        })
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {

                    val shopImageFileUri = MethodChecker.getUri(context, File(shopImageFilePath))
                    shopShare.appIntent?.clipData = ClipData.newRawUri("", shopImageFileUri)
                    shopShare.appIntent?.removeExtra(Intent.EXTRA_STREAM)
                    shopShare.appIntent?.removeExtra(Intent.EXTRA_TEXT)
                    when(shopShare) {
                        is ShopShareModel.CopyLink -> {
                            linkerShareData?.url?.let { ClipboardHandler().copyToClipboard((activity as Activity), it) }
                            Toast.makeText(context, getString(R.string.shop_page_share_action_copy_success), Toast.LENGTH_SHORT).show()
                        }
                        is ShopShareModel.Instagram, is ShopShareModel.Facebook -> {
                            startActivity(shopShare.appIntent?.apply {
                                putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                            })
                        }
                        is ShopShareModel.Whatsapp -> {
                            startActivity(shopShare.appIntent?.apply {
                                putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                                type = ShopShareBottomSheet.MimeType.TEXT.type
                                putExtra(Intent.EXTRA_TEXT, getString(
                                        R.string.shop_page_share_text_with_link,
                                        shopPageHeaderDataModel?.shopName,
                                        linkerShareData?.shareContents
                                ))
                            })
                        }
                        is ShopShareModel.Others -> {
                            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                                type = ShopShareBottomSheet.MimeType.IMAGE.type
                                putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                                type = ShopShareBottomSheet.MimeType.TEXT.type
                                putExtra(Intent.EXTRA_TEXT, getString(
                                        R.string.shop_page_share_text_with_link,
                                        shopPageHeaderDataModel?.shopName,
                                        linkerShareData?.shareContents
                                ))
                            }, getString(R.string.shop_page_share_to_social_media_text)))
                        }
                        else -> {
                            startActivity(shopShare.appIntent?.apply {
                                putExtra(Intent.EXTRA_TEXT, getString(
                                        R.string.shop_page_share_text_with_link,
                                        shopPageHeaderDataModel?.shopName,
                                        linkerShareData?.shareContents
                                ))
                            })
                        }
                    }
                    shopPageTracking?.clickShareSocialMedia(customDimensionShopPage, isMyShop, shopShare.socialMediaName)
                    shopShareBottomSheet?.dismiss()

                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )
    }

    private fun saveShopImage() {
        shopPageHeaderDataModel?.shopSnippetUrl?.let {
            shopViewModel.saveShopImageToPhoneStorage(context, it)
        }
    }

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

        val userSession = UserSession(context)
        if (userSession.isLoggedIn) {
            stickyLoginView.hide()
            updateViewPagerPadding()
            updateFloatingChatButtonMargin()
            return
        }

        var isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_SHOP, true)
        if (stickyLoginView.isLoginReminder() && isCanShowing) {
            stickyLoginView.showLoginReminder(StickyLoginConstant.Page.SHOP)
            if (stickyLoginView.isShowing()) {
                stickyLoginView.trackerLoginReminder.viewOnPage(StickyLoginConstant.Page.SHOP)
            }
        } else {
            isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_SHOP, true)
            if (!isCanShowing) {
                stickyLoginView.hide()
                updateViewPagerPadding()
                updateFloatingChatButtonMargin()
                return
            }

            this.tickerDetail?.let { stickyLoginView.setContent(it) }
            stickyLoginView.show(StickyLoginConstant.Page.SHOP)
            stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.SHOP)
        }
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

    fun updateSortId(sortId: String) {
        this.initialProductListSortId = sortId
    }
}
