package com.tokopedia.homenav.mainnav.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity
import com.tokopedia.analytics.performance.perf.PerformanceTrace
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation.SOURCE_ACCOUNT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.CONTROL
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.EXP_NAME
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.VARIANT
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.domain.MainNavSharedPref.getProfileCacheData
import com.tokopedia.homenav.mainnav.domain.MainNavSharedPref.setProfileCacheFromAccountModel
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.asTrackingPageSource
import com.tokopedia.homenav.mainnav.view.analytics.TrackingOthers
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavigationDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.view.activity.HomeNavPerformanceInterface
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import java.util.*
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {

    companion object {
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
        private const val REQUEST_LOGIN = 1234
        private const val REQUEST_REGISTER = 2345
        private const val OFFSET_TO_SHADOW = 100
        private const val REQUEST_REVIEW_PRODUCT = 999
        private const val COACHMARK_SAFE_DELAY = 200L
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
        private const val PERFORMANCE_TRACE_HOME_NAV = "home_nav"
    }

    private var mainNavDataFetched: Boolean = false
    private var sharedPrefs: SharedPreferences? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModel: MainNavViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: NpaLayoutManager
    lateinit var adapter: MainNavListAdapter

    private var navToolbar: NavToolbar? = null

    private var trackingQueue: TrackingQueue? = null

    private lateinit var userSession: UserSessionInterface
    private val args: MainNavFragmentArgs by navArgs()

    private var pageSource = ""
    private var trackingPageSource = pageSource.asTrackingPageSource()

    // for coachmark purpose
    private var isOngoingShowOnboarding = false

    private val performanceTrace = PerformanceTrace(PERFORMANCE_TRACE_HOME_NAV)

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val baseNavComponent =
            DaggerBaseNavComponent.builder()
                .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
                .build() as DaggerBaseNavComponent

        DaggerMainNavComponent.builder()
            .baseNavComponent(baseNavComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setInitialState()

        pageSource = args.StringMainNavArgsSourceKey
        viewModel.setPageSource(pageSource)
        context?.let {
            viewModel.setProfileCache(getProfileCacheData(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(getString(R.string.title_main_nav))
            it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_CLOSE)
            navToolbar = it
            viewLifecycleOwner.lifecycle.addObserver(it)
            it.setOnClickListener {
                TrackingOthers.onClickCloseButton(trackingPageSource)
            }
        }
        return inflater.inflate(R.layout.fragment_main_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        performanceTrace.init(
            v = view.rootView,
            scope = this.lifecycleScope,
            touchListenerActivity = activity as? TouchListenerActivity
        )
        recyclerView = view.findViewById(R.id.recycler_view)
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(MainNavSpacingDecoration(12f.toDpInt()))
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeVerticalScrollOffset()
                if (offset > OFFSET_TO_SHADOW) {
                    navToolbar?.showShadow(lineShadow = true)
                } else {
                    navToolbar?.hideShadow(lineShadow = true)
                }
            }
        })
        initAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_LOGIN, REQUEST_REGISTER -> viewModel.reloadMainNavAfterLogin()
                REQUEST_REVIEW_PRODUCT -> viewModel.refreshTransactionListData()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.mainNavLiveData.observe(
            viewLifecycleOwner,
            Observer {
                populateAdapterData(it)
            }
        )

        viewModel.networkProcessLiveData.observe(
            viewLifecycleOwner,
            Observer { isFinished ->
                if (!isFinished) {
                    getNavPerformanceCallback()?.startNetworkRequestPerformanceMonitoring()
                } else {
                    getNavPerformanceCallback()?.stopNetworkRequestPerformanceMonitoring()
                }
            }
        )

        viewModel.profileDataLiveData.observe(
            viewLifecycleOwner,
            Observer { accountHeaderModel ->
                context?.let { ctx ->
                    setProfileCacheFromAccountModel(ctx, accountHeaderModel)
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()

        getTrackingQueueObj()?.sendAll()
    }

    override fun onRefresh() {
    }

    override fun getTrackingQueueObj(): TrackingQueue? {
        if (trackingQueue == null) {
            activity?.let {
                trackingQueue = TrackingQueue(it)
            }
        }
        return trackingQueue
    }

    override fun putEEToTrackingQueue(data: HashMap<String, Any>) {
        if (getTrackingQueueObj() != null) {
            getTrackingQueueObj()?.putEETracking(data)
        }
    }

    override fun onProfileSectionClicked(eventLabel: String, applink: String) {
        TrackingProfileSection.onClickProfileSection(eventLabel, trackingPageSource)
        if (applink == ApplinkConst.ACCOUNT && pageSource == SOURCE_ACCOUNT) {
            activity?.onBackPressed()
        } else {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
    }

    override fun onTickerDescClicked(applink: String) {
        TrackingProfileSection.onClickProfileSection(TrackingProfileSection.CLICK_OPEN_SHOP, trackingPageSource)
        RouteManager.route(context, applink)
    }

    override fun onProfileLoginClicked() {
        TrackingProfileSection.onClickLoginButton(trackingPageSource)
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
        TrackingProfileSection.onClickRegisterButton(trackingPageSource)
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.REGISTER), REQUEST_REGISTER)
    }

    override fun onErrorProfileRefreshClicked(position: Int) {
        viewModel.refreshProfileData()
    }

    override fun onErrorShopInfoRefreshClicked(position: Int) {
        viewModel.refreshUserShopData()
    }

    override fun onErrorBuListClicked(position: Int) {
        viewModel.refreshBuListData()
    }

    override fun onErrorTransactionListClicked(position: Int) {
        viewModel.refreshTransactionListData()
    }

    override fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel) {
        view?.let {
            hitClickTrackingBasedOnId(homeNavMenuDataModel)
            if (homeNavMenuDataModel.sectionId == MainNavConst.Section.ORDER ||
                homeNavMenuDataModel.sectionId == MainNavConst.Section.BU_ICON ||
                homeNavMenuDataModel.sectionId == MainNavConst.Section.ACTIVITY
            ) {
                if (homeNavMenuDataModel.applink.isNotEmpty()) {
                    if (!handleClickFromPageSource(homeNavMenuDataModel.applink)) {
                        RouteManager.route(context, homeNavMenuDataModel.applink)
                    }
                } else {
                    NavigationRouter.MainNavRouter.navigateTo(
                        it,
                        NavigationRouter.PAGE_CATEGORY,
                        bundleOf("title" to homeNavMenuDataModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuDataModel)
                    )
                }
                TrackingOthers.onClickBusinessUnitItem(homeNavMenuDataModel.itemTitle, trackingPageSource)
            } else {
                RouteManager.route(requireContext(), homeNavMenuDataModel.applink)
            }
        }
    }

    private fun handleClickFromPageSource(applink: String): Boolean {
        if (validateTargetMenu(applink)) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

    private fun validateTargetMenu(applink: String): Boolean {
        return validateHomeUohPage(applink) || validateHomeWishlistPage(applink)
    }

    private fun validateHomeUohPage(applink: String) =
        applink == ApplinkConst.PURCHASE_ORDER && pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_UOH

    private fun validateHomeWishlistPage(applink: String) =
        applink == ApplinkConst.WISHLIST &&
            (
                pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_COLLECTION ||
                    pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
                )

    private fun hitClickTrackingBasedOnId(homeNavMenuDataModel: HomeNavMenuDataModel) {
        when (homeNavMenuDataModel.id) {
            ID_ALL_TRANSACTION -> TrackingTransactionSection.clickOnAllTransaction(trackingPageSource)
            ID_TICKET -> TrackingTransactionSection.clickOnTicket(trackingPageSource)
            ID_REVIEW -> TrackingTransactionSection.clickOnReview(trackingPageSource)
            ID_WISHLIST_MENU -> TrackingTransactionSection.clickOnWishlist(userSession.userId, trackingPageSource)
            ID_FAVORITE_SHOP -> TrackingTransactionSection.clickOnTokoFavorit(userSession.userId, trackingPageSource)
            ID_HOME -> TrackingOthers.onClickBackToHome(trackingPageSource)
            else -> TrackingOthers.clickOnUserMenu(homeNavMenuDataModel.trackerName, trackingPageSource)
        }
    }

    override fun onMenuImpression(homeNavMenuDataModel: HomeNavMenuDataModel) {
    }

    override fun getUserId(): String {
        return userSession.userId
    }

    override fun getReviewCounterAbIsUnify(): Boolean {
        return remoteConfig.getString(EXP_NAME, CONTROL) == VARIANT
    }

    override fun onErrorAffiliateInfoRefreshClicked(position: Int) {
        viewModel.refreshUserAffiliateData()
    }

    override fun onTitleClicked(homeNavTitleDataModel: HomeNavTitleDataModel) {
        when (homeNavTitleDataModel.identifier) {
            ClientMenuGenerator.IDENTIFIER_TITLE_ORDER_HISTORY -> TrackingTransactionSection.getClickViewAllTransaction(trackingPageSource)
            ClientMenuGenerator.IDENTIFIER_TITLE_WISHLIST -> TrackingTransactionSection.clickOnWishlistViewAll(trackingPageSource)
            ClientMenuGenerator.IDENTIFIER_TITLE_REVIEW -> TrackingTransactionSection.clickOnReviewViewAll(trackingPageSource)
        }
        if (!handleClickFromPageSource(homeNavTitleDataModel.applink)) {
            RouteManager.route(context, homeNavTitleDataModel.applink)
        }
    }

    @MePage(MePage.Widget.WISHLIST)
    override fun onErrorWishlistClicked() {
    }

    override fun onWishlistCardClicked(wishlistModel: NavWishlistModel, position: Int) {
        TrackingTransactionSection.clickOnWishlistItem(getUserId(), wishlistModel, position, trackingPageSource)
        RouteManager.route(context, ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL, wishlistModel.id)
    }

    override fun onWishlistCardImpressed(wishlistModel: NavWishlistModel, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnWishlist(
                userId = userSession.userId,
                position = position,
                wishlistModel = wishlistModel,
                pageSource = trackingPageSource
            )
        )
    }

    override fun onReviewCardClicked(
        element: NavReviewModel,
        position: Int,
        isClickStar: Boolean,
        ratingValue: String,
        uri: String
    ) {
        if(isClickStar) {
            TrackingTransactionSection.clickReviewStars(position, userSession.userId, element, ratingValue, trackingPageSource)
        } else {
            TrackingTransactionSection.clickReviewCard(position, userSession.userId, element, trackingPageSource)
        }
        val intent = RouteManager.getIntent(context, uri)
        startActivityForResult(intent, REQUEST_REVIEW_PRODUCT)
    }

    override fun onReviewCardImpressed(element: NavReviewModel, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnReviewProduct(
                userId = userSession.userId,
                element = element,
                position = position,
                pageSource = trackingPageSource
            )
        )
    }

    @MePage(MePage.Widget.REVIEW)
    override fun onErrorReviewClicked() {
    }

    override fun onOrderCardClicked(applink: String, trackingLabel: String?) {
        if(trackingLabel != null) {
            TrackingTransactionSection.clickOnOrderStatus(
                trackingLabel,
                trackingPageSource
            )
        } else {
            TrackingTransactionSection.getClickViewAllTransaction(trackingPageSource)
        }
        if (!handleClickFromPageSource(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun onOrderCardImpressed(trackingLabel: String, orderId: String, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnOrderStatus(
                userId = userSession.userId,
                orderLabel = trackingLabel,
                orderId = orderId,
                position = position,
                pageSource = trackingPageSource
            )
        )
    }

    override fun onViewAllWishlistClicked() {
        TrackingTransactionSection.clickOnWishlistViewAll(trackingPageSource)
        val applink = ApplinkConst.WISHLIST
        if (!handleClickFromPageSource(applink)) {
            RouteManager.route(context, applink)
        }
    }

    override fun onViewAllReviewClicked() {
        TrackingTransactionSection.clickOnReviewViewAll(trackingPageSource)
        RouteManager.route(context, ApplinkConst.REPUTATION)
    }

    private fun getNavPerformanceCallback(): PageLoadTimePerformanceInterface? {
        context?.let {
            return (it as? HomeNavPerformanceInterface)?.getNavPerformanceInterface()
        }
        return null
    }

    private fun initAdapter() {
        val mainNavFactory = MainNavTypeFactoryImpl(
            this,
            getUserSession(),
            object : TokopediaPlusListener {
                override fun isShown(
                    isShown: Boolean,
                    pageSource: String,
                    tokopediaPlusDataModel: TokopediaPlusDataModel
                ) {
                }

                override fun onClick(
                    pageSource: String,
                    tokopediaPlusDataModel: TokopediaPlusDataModel
                ) {
                    TrackingProfileSection.onClickTokopediaPlus(tokopediaPlusDataModel.isSubscriber, trackingPageSource)
                }

                override fun onRetry() {
                    viewModel.refreshTokopediaPlusData()
                }
            }
        )
        adapter = MainNavListAdapter(mainNavFactory)

        activity?.let {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }

        layoutManager = NpaLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun populateAdapterData(data: MainNavigationDataModel) {
        setupViewPerformanceMonitoring(data)
        adapter.submitList(data.dataList)

        if (data.dataList.size > 1 && !mainNavDataFetched) {
            viewModel.getMainNavData(true)
            mainNavDataFetched = true
        }
    }

    private fun setupViewPerformanceMonitoring(data: MainNavigationDataModel) {
        if (data.dataList.size > 1) {
            getNavPerformanceCallback()?.startRenderPerformanceMonitoring()
            recyclerView.addOneTimeGlobalLayoutListener {
                getNavPerformanceCallback()?.stopRenderPerformanceMonitoring()
                getNavPerformanceCallback()?.stopMonitoring()
            }
        }
    }

    private fun getUserSession(): UserSessionInterface {
        if (!::userSession.isInitialized) {
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderDataModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return requireContext().getSharedPreferences(AccountHeaderDataModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

    private fun goToPDP(productId: String, position: Int) {
        RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }
}

data class CoachmarkRecyclerViewConfig(
    val items: ArrayList<CoachMark2Item>,
    val configs: ArrayList<CoachmarkItemReyclerViewConfig>,
    val onFinish: () -> Unit
)

data class CoachmarkItemReyclerViewConfig(
    val scrollToPosition: Int,
    val targetPosition: Int?
)
