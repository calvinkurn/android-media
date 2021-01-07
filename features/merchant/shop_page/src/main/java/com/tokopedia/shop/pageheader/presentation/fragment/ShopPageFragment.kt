package com.tokopedia.shop.pageheader.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.LinearLayout
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
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
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.util.setOnClickLinkSpannable
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.TrackShopTypeDef
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_ACTIVITY_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HEADER_CONTENT_DATA_MIDDLE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HEADER_CONTENT_DATA_RENDER
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_P1_MIDDLE
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtil.isUsingNewNavigation
import com.tokopedia.shop.common.view.bottomsheet.ShopShareBottomSheet
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.model.ShopShareModel
import com.tokopedia.shop.common.view.viewmodel.ShopPageFollowingStatusSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import com.tokopedia.shop.pageheader.di.component.DaggerShopPageComponent
import com.tokopedia.shop.pageheader.di.component.ShopPageComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageModule
import com.tokopedia.shop.pageheader.presentation.ShopPageViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageFragmentPagerAdapter
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentHeaderViewHolder
import com.tokopedia.shop.pageheader.presentation.listener.ShopPagePerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.product.view.fragment.HomeProductFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.review.shop.view.ReviewShopFragment
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop.setting.view.activity.ShopPageSettingActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.shop_page_fragment_content_layout.*
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
        const val NEWLY_BROADCAST_CHANNEL_SAVED = "EXTRA_NEWLY_BROADCAST_SAVED"
        const val EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION"
        const val TAB_POSITION_HOME = 0
        const val TAB_POSITION_FEED = 1
        const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
        const val SHOP_STICKY_LOGIN = "SHOP_STICKY_LOGIN"
        const val SAVED_INITIAL_FILTER = "saved_initial_filter"
        const val FORCE_NOT_SHOWING_HOME_TAB = "FORCE_NOT_SHOWING_HOME_TAB"
        private const val REQUEST_CODER_USER_LOGIN = 100
        private const val REQUEST_CODE_FOLLOW = 101
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        private const val PAGE_LIMIT = 2
        private const val SOURCE_SHOP = "shop"
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val PATH_HOME = "home"
        private const val PATH_REVIEW = "review"
        private const val PATH_PRODUCT = "product"
        private const val PATH_FEED = "feed"
        private const val QUERY_SHOP_REF = "shop_ref"
        private const val QUERY_SHOP_ATTRIBUTION = "tracker_attribution"
        private const val START_PAGE = 1

        private const val REQUEST_CODE_START_LIVE_STREAMING = 7621

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
    private var isForceNotShowingTab: Boolean = false
    private val iconTabHomeInactive: Int
        get() = R.drawable.ic_shop_tab_home_inactive.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_tab_home_old_inactive
    private val iconTabHomeActive: Int
        get() = R.drawable.ic_shop_tab_home_active.takeIf {
            isUsingNewNavigation()
        } ?: -1
    private val iconTabProductInactive: Int
        get() = R.drawable.ic_shop_tab_product_inactive.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_tab_products_old_inactive
    private val iconTabProductActive: Int
        get() = R.drawable.ic_shop_tab_product_active.takeIf {
            isUsingNewNavigation()
        } ?: -1
    private val iconTabFeedInactive: Int
        get() = R.drawable.ic_shop_tab_feed_inactive.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_tab_feed_old_inactive
    private val iconTabFeedActive: Int
        get() = R.drawable.ic_shop_tab_feed_active.takeIf {
            isUsingNewNavigation()
        } ?: -1
    private val iconTabReviewInactive: Int
        get() = R.drawable.ic_shop_tab_review_inactive.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_tab_review_old_inactive
    private val iconTabReviewActive: Int
        get() = R.drawable.ic_shop_tab_review_active.takeIf {
            isUsingNewNavigation()
        } ?: -1
    private val iconChatFloatingButton: Int
        get() = R.drawable.ic_chat_floating_button.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_chat_floating_button_old
    private val chatButton: FloatingActionButton
        get() = button_chat.takeIf {
            isUsingNewNavigation()
        }?: button_chat_old
    private val intentData: Intent = Intent()
    private val permissionChecker: PermissionCheckerHelper = PermissionCheckerHelper()
    private var isFirstLoading: Boolean = false
    private var shouldOverrideTabToHome: Boolean = false
    private var isRefresh: Boolean = false
    private var shouldOverrideTabToReview: Boolean = false
    private var shouldOverrideTabToProduct: Boolean = false
    private var shouldOverrideTabToFeed: Boolean = false
    private var listShopPageTabModel = listOf<ShopPageTabModel>()
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(
                shopId,
                shopPageHeaderDataModel?.isOfficial ?: false,
                shopPageHeaderDataModel?.isGoldMerchant ?: false
        )
    }
    private var shopPageHeaderDataModel: ShopPageHeaderDataModel? = null
    private var initialProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()
    private var shopShareBottomSheet: ShopShareBottomSheet? = null
    private var shopImageFilePath: String = ""
    private var shopProductFilterParameterSharedViewModel: ShopProductFilterParameterSharedViewModel? = null
    private var shopPageFollowingStatusSharedViewModel: ShopPageFollowingStatusSharedViewModel? = null
    var selectedPosition = -1
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
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopPageFollowingStatusSharedViewModel?.shopPageFollowingStatusLiveData?.removeObservers(this)
        shopViewModel.flush()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_INITIAL_FILTER,  initialProductFilterParameter)
    }

    private fun initViews(view: View) {
        context?.let{
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        errorTextView = view.findViewById(com.tokopedia.abstraction.R.id.message_retry)
        errorButton = view.findViewById(com.tokopedia.abstraction.R.id.button_retry)
        setupBottomSheetSellerMigration(view)
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
        swipeToRefresh.setOnRefreshListener {
            refreshData()
            updateStickyContent()
        }
        mainLayout.requestFocus()
        initStickyLogin(view)
        getChatButtonInitialMargin()
    }

    private fun setupBottomSheetSellerMigration(view: View) {
        val viewTarget: LinearLayout = view.findViewById(bottom_sheet_wrapper)
        (activity as? ShopPageActivity)?.bottomSheetSellerMigration = BottomSheetBehavior.from(viewTarget)
        hideBottomSheetSellerMigration()

        if (isSellerMigrationEnabled(context)) {
            BottomSheetUnify.bottomSheetBehaviorKnob(viewTarget, false)
            BottomSheetUnify.bottomSheetBehaviorHeader(viewTarget, false)

            val sellerMigrationLayout = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet_has_post, null)
            viewTarget.addView(sellerMigrationLayout)

            val ivTabFeedHasPost: ImageUnify = sellerMigrationLayout.findViewById(R.id.ivTabFeedHasPost)
            val tvTitleTabFeedHasPost: Typography = sellerMigrationLayout.findViewById(R.id.tvTitleTabFeedHasPost)
            tvTitleTabFeedHasPost.movementMethod = LinkMovementMethod.getInstance()
            ivTabFeedHasPost.setImageUrl(SellerMigrationConstants.SELLER_MIGRATION_SHOP_PAGE_TAB_FEED_LINK)
            tvTitleTabFeedHasPost.setOnClickLinkSpannable(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_tab_feed_bottom_sheet_content), ::trackContentFeedBottomSheet) {
                val shopAppLink = UriUtil.buildUri(ApplinkConst.SHOP, shopId).orEmpty()
                val appLinkShopPageFeed = UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_FEED, shopId).orEmpty()
                val intent = SellerMigrationActivity.createIntent(
                        context = requireContext(),
                        featureName = SellerMigrationFeatureName.FEATURE_POST_FEED,
                        screenName = FeedShopFragment::class.simpleName.orEmpty(),
                        appLinks = arrayListOf(ApplinkConstInternalSellerapp.SELLER_HOME, shopAppLink, appLinkShopPageFeed))
                startActivity(intent)
            }
        }
    }

    private fun trackContentFeedBottomSheet() {
        val userSession = UserSession(context)
        SellerMigrationTracking.trackClickShopAccount(userSession.userId.orEmpty())
    }

    private fun getChatButtonInitialMargin() {
        val buttonChatLayoutParams = (chatButton.layoutParams as ViewGroup.MarginLayoutParams)
        initialFloatingChatButtonMarginBottom = buttonChatLayoutParams.bottomMargin
    }

    private fun observeLiveData(owner: LifecycleOwner) {
        shopViewModel.shopPageP1Data.observe(owner, Observer { result ->
            stopMonitoringPltCustomMetric(SHOP_TRACE_P1_MIDDLE)
            startMonitoringPltCustomMetric(SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER)
            when (result) {
                is Success -> {
                    onSuccessGetShopPageTabData(result.data)
                }
                is Fail -> {
                    onErrorGetShopPageTabData(result.throwable)
                    val throwable = result.throwable
                    ShopUtil.logTimberWarning(
                            "SHOP_PAGE_P1_ERROR",
                            "shop_id='${shopId}';" +
                                    "error_message='${ErrorHandler.getErrorMessage(context, throwable)}';" +
                                    "error_trace='${Log.getStackTraceString(throwable)}'"
                    )
                }
            }
            stopMonitoringPltCustomMetric(SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER)
        })

        shopViewModel.shopPageHeaderContentData.observe(owner, Observer { result ->
            stopMonitoringPltCustomMetric(SHOP_TRACE_HEADER_CONTENT_DATA_MIDDLE)
            startMonitoringPltCustomMetric(SHOP_TRACE_HEADER_CONTENT_DATA_RENDER)
            when (result) {
                is Success -> {
                    onSuccessGetShopPageHeaderContentData(result.data)
                }
                is Fail -> {
                    onErrorGetShopPageHeaderContentData(result.throwable)
                }
            }
            stopMonitoringPltCustomMetric(SHOP_TRACE_HEADER_CONTENT_DATA_RENDER)
            stopMonitoringPerformance()
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
            if (shopImageFilePath.isNotEmpty()) {
                shopShareBottomSheet = ShopShareBottomSheet.createInstance().apply {
                    init(this@ShopPageFragment)
                }
                shopShareBottomSheet?.show(fragmentManager)
            }
        })

    }

    private fun onSuccessGetShopIdFromDomain(shopId: String) {
        this.shopId = shopId
        getShopPageP1Data()
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
        if (shopId.toIntOrZero() == 0 && shopDomain.orEmpty().isEmpty()) return
        startMonitoringPltCustomMetric(SHOP_TRACE_HEADER_CONTENT_DATA_MIDDLE)
        shopViewModel.getShopPageHeaderContentData(shopId, shopDomain ?: "", isRefresh)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopMonitoringPltPreparePage()
        stopMonitoringPltCustomMetric(SHOP_TRACE_ACTIVITY_PREPARE)
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopProductFilterParameterSharedViewModel::class.java)
        shopPageFollowingStatusSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopPageFollowingStatusSharedViewModel::class.java)
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
                isForceNotShowingTab = getBooleanExtra(FORCE_NOT_SHOWING_HOME_TAB, false)
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
                    if (lastPathSegment.orEmpty() == PATH_PRODUCT) {
                        shouldOverrideTabToProduct = true
                    }
                    if (lastPathSegment.orEmpty() == PATH_FEED) {
                        shouldOverrideTabToFeed = true
                    }
                    shopRef = getQueryParameter(QUERY_SHOP_REF) ?: ""
                    shopAttribution = getQueryParameter(QUERY_SHOP_ATTRIBUTION) ?: ""
                }
                handlePlayBroadcastExtra(this@run)
            }
            if (GlobalConfig.isSellerApp()) {
                shopId = shopViewModel.userShopId
            }
            getSavedInstanceStateData(savedInstanceState)
            observeLiveData(this)
            observeShopProductFilterParameterSharedViewModel()
            observeShopPageFollowingStatusSharedViewModel()
            getInitialData()
            view.findViewById<ViewStub>(R.id.view_stub_content_layout).inflate()
            if (!swipeToRefresh.isRefreshing) {
                setViewState(VIEW_LOADING)
            }
            initViews(view)
        }
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(viewLifecycleOwner, Observer {
            initialProductFilterParameter = it
        })
    }

    private fun observeShopPageFollowingStatusSharedViewModel() {
        shopPageFollowingStatusSharedViewModel?.shopPageFollowingStatusLiveData?.observe(viewLifecycleOwner, Observer {
            shopPageFragmentHeaderViewHolder.setFavoriteValue(it)
            shopPageFragmentHeaderViewHolder.updateFavoriteButton()
        })
    }

    private fun getSavedInstanceStateData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            initialProductFilterParameter = it.getParcelable(SAVED_INITIAL_FILTER)
        }
    }

    private fun stopMonitoringPltPreparePage() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
            }
        }
    }

    private fun startMonitoringPltNetworkRequest() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun startMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startCustomMetric(it, tag)
            }
        }
    }

    private fun stopMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopCustomMetric(it, tag)
            }
        }
    }

    private fun stopMonitoringPerformance() {
        (activity as? ShopPageActivity)?.stopShopHeaderPerformanceMonitoring()
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
        startMonitoringPltNetworkRequest()
        startMonitoringPltCustomMetric(SHOP_TRACE_P1_MIDDLE)
        isFirstLoading = true
        if (shopId.isEmpty()) {
            shopViewModel.getShopIdFromDomain(shopDomain.orEmpty())
        } else {
            getShopPageP1Data()
        }
    }

    private  fun getShopPageP1Data(){
        if (shopId.toIntOrZero() == 0 && shopDomain.orEmpty().isEmpty()) return
        shopViewModel.getShopPageTabData(
                shopId.toIntOrZero(),
                shopDomain.orEmpty(),
                START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                initialProductFilterParameter ?: ShopProductFilterParameter(),
                "",
                "",
                isRefresh
        )
    }

    private fun initToolbar() {
        if(isMyShop){
            initOldToolbar()
        }
        else{
            if (isUsingNewNavigation()) {
                initNewToolbar()
            } else {
                initOldToolbar()
            }
        }
    }

    private fun initNewToolbar() {
        new_navigation_toolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)
            show()
            val iconBuilder = IconBuilder()
            iconBuilder.addIcon(IconList.ID_SHARE) { clickShopShare() }
            if(isCartShownInNewNavToolbar())
                iconBuilder.addIcon(IconList.ID_CART) {}
            iconBuilder.addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            if(shopViewModel.isUserSessionActive)
                setBadgeCounter(IconList.ID_CART, getCartCounter())
            setToolbarPageName(SHOP_PAGE)
        }
    }

    private fun getCartCounter(): Int {
        return cartLocalCacheHandler.getInt(TOTAL_CART_CACHE_KEY, 0)
    }

    private fun isCartShownInNewNavToolbar(): Boolean {
        return !GlobalConfig.isSellerApp() && remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SHOP, true)
    }

    private fun initOldToolbar() {
        toolbar?.show()
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
        setShopName()
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                shopPageLoadingState.visibility = View.VISIBLE
                shopPageErrorState.visibility = View.GONE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
                chatButton.hide()
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
        if (uri.isNotEmpty()) {
            File(uri).apply {
                if (exists()) {
                    delete()
                }
            }
        }
    }

    private fun clickShopShare() {
        if (isMyShop) {
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
            shopHomeType = shopPageP1Data.shopHomeType.takeIf { !isForceNotShowingTab } ?: ShopHomeType.NONE
            topContentUrl = shopPageP1Data.topContentUrl
            shopName = shopPageP1Data.shopName
            shopDomain = shopPageP1Data.shopDomain
            avatar = shopPageP1Data.shopAvatar
        }
        new_navigation_toolbar?.run{
            setupSearchbar(
                    hints =  listOf(HintData(placeholder = getString(
                    R.string.shop_product_search_hint_2,
                    shopPageHeaderDataModel?.shopName.orEmpty()))),
                    searchbarClickCallback = {
                        redirectToShopSearchProduct()
                    }
            )
        }
        setShopName()
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
        shopPageHeaderDataModel?.let {
            shopPageFragmentHeaderViewHolder.hideFollowButton()
            shopPageFragmentHeaderViewHolder.bind(it, isMyShop, remoteConfig)
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
                chatButton.setImageResource(iconChatFloatingButton)
                chatButton.show()
                chatButton.setOnClickListener {
                    goToChatSeller()
                }
            } else {
                chatButton.hide()
            }
            updateFavouriteResult(shopPageHeaderContentData.favoriteData.alreadyFavorited == 1)
            shopPageFragmentHeaderViewHolder.bind(shopPageHeaderDataModel, isMyShop, remoteConfig)
            shopPageFragmentHeaderViewHolder.updateFavoriteData(shopPageHeaderContentData.favoriteData)
            shopPageFragmentHeaderViewHolder.setupFollowButton(isMyShop)
            if (!shopPageHeaderDataModel.isOfficial) {
                shopPageFragmentHeaderViewHolder.showShopReputationBadges(shopPageHeaderContentData.shopBadge)
            }
            shopPageFragmentHeaderViewHolder.updateShopTicker(
                    shopPageHeaderDataModel,
                    shopPageHeaderContentData.shopOperationalHourStatus,
                    isMyShop
            )
            setShopName()
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
        shopShareBottomSheet?.dismiss()
    }

    private fun setupTabs() {
        listShopPageTabModel = createListShopPageTabModel()
        viewPagerAdapter.setTabData(listShopPageTabModel)
        selectedPosition = getSelectedTabPosition()
        viewPagerAdapter.notifyDataSetChanged()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPagerAdapter.handleSelectedTab(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedPosition = tab.position
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
                if (isSellerMigrationEnabled(context)) {
                    if(isMyShop && viewPagerAdapter.isFragmentObjectExists(FeedShopFragment::class.java)){
                        val tabFeedPosition = viewPagerAdapter.getFragmentPosition(FeedShopFragment::class.java)
                        if (tab.position == tabFeedPosition) {
                            showBottomSheetSellerMigration()
                        } else {
                            hideBottomSheetSellerMigration()
                        }
                    }else{
                        hideBottomSheetSellerMigration()
                    }
                }
            }
        })
        tabLayout?.apply {
            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = viewPagerAdapter.getTabView(i, selectedPosition)
            }
        }
        viewPager.setCurrentItem(selectedPosition, false)
    }

    private fun getSelectedTabPosition(): Int {
        var selectedPosition = viewPager.currentItem
        if(tabLayout.tabCount == 0){
            if (shouldOverrideTabToHome) {
                selectedPosition = if (viewPagerAdapter.isFragmentObjectExists(HomeProductFragment::class.java)) {
                    viewPagerAdapter.getFragmentPosition(HomeProductFragment::class.java)
                } else {
                    viewPagerAdapter.getFragmentPosition(ShopPageHomeFragment::class.java)
                }
            }
            if (shouldOverrideTabToReview) {
                selectedPosition = if (viewPagerAdapter.isFragmentObjectExists(ReviewShopFragment::class.java)) {
                    viewPagerAdapter.getFragmentPosition(ReviewShopFragment::class.java)
                } else {
                    selectedPosition
                }
            }
            if (shouldOverrideTabToProduct) {
                selectedPosition = if (viewPagerAdapter.isFragmentObjectExists(ShopPageProductListFragment::class.java)) {
                    viewPagerAdapter.getFragmentPosition(ShopPageProductListFragment::class.java)
                } else {
                    selectedPosition
                }
            }
            if (shouldOverrideTabToFeed) {
                selectedPosition = if (viewPagerAdapter.isFragmentObjectExists(FeedShopFragment::class.java)) {
                    viewPagerAdapter.getFragmentPosition(FeedShopFragment::class.java)
                } else {
                    selectedPosition
                }
            }
        }
        return selectedPosition
    }

    private fun createListShopPageTabModel(): List<ShopPageTabModel> {
        return mutableListOf<ShopPageTabModel>().apply {
            if (isShowHomeTab()) {
                getHomeFragment()?.let { homeFragment ->
                    add(ShopPageTabModel(
                            getString(R.string.shop_info_title_tab_home),
                            iconTabHomeInactive,
                            iconTabHomeActive,
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
                    iconTabProductInactive,
                    iconTabProductActive,
                    shopPageProductFragment
            ))
            if (isShowFeed) {
                val feedFragment = FeedShopFragment.createInstance(
                        shopId,
                        createPostUrl
                )
                add(ShopPageTabModel(
                        getString(R.string.shop_info_title_tab_feed),
                        iconTabFeedInactive,
                        iconTabFeedActive,
                        feedFragment
                ))
            }
            val shopReviewFragment = ReviewShopFragment.createInstance(
                    shopId,
                    shopDomain
            )
            add(ShopPageTabModel(
                    getString(R.string.shop_info_title_tab_review),
                    iconTabReviewInactive,
                    iconTabReviewActive,
                    shopReviewFragment
            ))
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
                ).apply{
                    setInitialProductListData(shopViewModel.productListData)
                }
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
                if (!swipeToRefresh.isRefreshing)
                    setViewState(VIEW_LOADING)
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
        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
                goToCart()
            }
        } else if (requestCode == REQUEST_CODE_START_LIVE_STREAMING) {
            if (data != null) handleResultVideoFromLiveStreaming(resultCode, data)
        }
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
        if (!swipeToRefresh.isRefreshing)
            setViewState(VIEW_LOADING)
        swipeToRefresh.isRefreshing = true
    }

    fun collapseAppBar() {
        appBarLayout.post {
            appBarLayout.setExpanded(false)
        }
    }

    fun isNewlyBroadcastSaved(): Boolean? {
        val args = arguments
        return args?.containsKey(NEWLY_BROADCAST_CHANNEL_SAVED)?.let {
            args.getBoolean(NEWLY_BROADCAST_CHANNEL_SAVED)
        }
    }

    fun clearIsNewlyBroadcastSaved() {
        arguments?.remove(NEWLY_BROADCAST_CHANNEL_SAVED)
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

    override fun openShopInfo() {
        redirectToShopInfoPage()
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
                        when (shopShare) {
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
        val buttonChatLayoutParams = (chatButton.layoutParams as ViewGroup.MarginLayoutParams)
        buttonChatLayoutParams.setMargins(
                buttonChatLayoutParams.leftMargin,
                buttonChatLayoutParams.topMargin,
                buttonChatLayoutParams.rightMargin,
                initialFloatingChatButtonMarginBottom + stickyLoginViewHeight
        )
        chatButton.layoutParams = buttonChatLayoutParams
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

    private fun showBottomSheetSellerMigration(){
        (activity as? ShopPageActivity)?.bottomSheetSellerMigration?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideBottomSheetSellerMigration(){
        (activity as? ShopPageActivity)?.bottomSheetSellerMigration?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setShopName() {
        if(isMyShop) {
            shopPageHeaderDataModel?.shopName = shopViewModel.ownerShopName
            shopPageFragmentHeaderViewHolder.setShopName(shopViewModel.ownerShopName)
        }
    }

    /**
     * Play Widget "Start Live Streaming"
     */
    override fun onStartLiveStreamingClicked() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER)
        startActivityForResult(intent, REQUEST_CODE_START_LIVE_STREAMING)
    }

    private fun handleResultVideoFromLiveStreaming(resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            handlePlayBroadcastExtra(data)
            refreshData()
        }
    }

    private fun handlePlayBroadcastExtra(data: Intent) {
        val isChannelSaved: Boolean = if (data.hasExtra(NEWLY_BROADCAST_CHANNEL_SAVED)) {
            data.getBooleanExtra(NEWLY_BROADCAST_CHANNEL_SAVED, false)
        } else return

        if (arguments == null) arguments = Bundle()
        arguments?.putBoolean(NEWLY_BROADCAST_CHANNEL_SAVED, isChannelSaved)

        if (isChannelSaved) showWidgetTranscodingToaster()
        else showWidgetDeletedToaster()
    }

    private fun showWidgetTranscodingToaster() {
        Toaster.build(
                view = requireView(),
                text = getString(R.string.shop_page_play_widget_sgc_save_video),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_NORMAL
        ).show()
    }

    private fun showWidgetDeletedToaster() {
        Toaster.build(
                requireView(),
                getString(R.string.shop_page_play_widget_sgc_video_deleted),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL
        ).show()
    }

    fun isTabSelected(tabFragmentClass: Class<out Any>): Boolean {
        return if(::viewPagerAdapter.isInitialized){
            if (viewPagerAdapter.isFragmentObjectExists(tabFragmentClass)) {
                viewPagerAdapter.getFragmentPosition(tabFragmentClass) == selectedPosition
            } else {
                false
            }
        }else{
            false
        }

    }
}
