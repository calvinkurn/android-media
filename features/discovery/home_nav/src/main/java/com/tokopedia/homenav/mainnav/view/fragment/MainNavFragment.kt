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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation.SOURCE_ACCOUNT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.utils.toDpInt
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
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.analytics.TrackingBuSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingUserMenuSection
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

    protected var trackingQueue: TrackingQueue? = null

    private lateinit var userSession: UserSessionInterface
    private val args: MainNavFragmentArgs by navArgs()

    private var pageSource = ""

    // for coachmark purpose
    private var isOngoingShowOnboarding = false

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
        }
        return inflater.inflate(R.layout.fragment_main_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        viewModel.allProcessFinished.observe(
            viewLifecycleOwner,
            Observer {
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

    override fun onProfileSectionClicked() {
        if (pageSource == SOURCE_ACCOUNT) {
            activity?.onBackPressed()
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConst.ACCOUNT)
            startActivity(intent)
        }
    }

    override fun onProfileLoginClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
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
            if (homeNavMenuDataModel.sectionId == MainNavConst.Section.ORDER || homeNavMenuDataModel.sectionId == MainNavConst.Section.BU_ICON) {
                if (homeNavMenuDataModel.applink.isNotEmpty()) {
                    if (!handleClickFromPageSource(homeNavMenuDataModel)) {
                        RouteManager.route(context, homeNavMenuDataModel.applink)
                    }
                } else {
                    NavigationRouter.MainNavRouter.navigateTo(
                        it,
                        NavigationRouter.PAGE_CATEGORY,
                        bundleOf("title" to homeNavMenuDataModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuDataModel)
                    )
                }
                TrackingBuSection.onClickBusinessUnitItem(homeNavMenuDataModel.itemTitle, userSession.userId)
            } else {
                RouteManager.route(requireContext(), homeNavMenuDataModel.applink)
            }
        }
    }

    private fun handleClickFromPageSource(homeNavMenuDataModel: HomeNavMenuDataModel): Boolean {
        if (validateTargetMenu(homeNavMenuDataModel)) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

    private fun validateTargetMenu(homeNavMenuDataModel: HomeNavMenuDataModel): Boolean {
        return validateHomeUohPage(homeNavMenuDataModel) || validateHomeWishlistPage(homeNavMenuDataModel)
    }

    private fun validateHomeUohPage(homeNavMenuDataModel: HomeNavMenuDataModel) =
        homeNavMenuDataModel.id == ID_ALL_TRANSACTION && pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_UOH

    private fun validateHomeWishlistPage(homeNavMenuDataModel: HomeNavMenuDataModel) =
        homeNavMenuDataModel.id == ID_WISHLIST_MENU &&
            (
                pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_COLLECTION ||
                    pageSource == ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
                )

    private fun hitClickTrackingBasedOnId(homeNavMenuDataModel: HomeNavMenuDataModel) {
        when (homeNavMenuDataModel.id) {
            ID_ALL_TRANSACTION -> TrackingTransactionSection.clickOnAllTransaction(userSession.userId)
            ID_TICKET -> TrackingTransactionSection.clickOnTicket(userSession.userId)
            ID_REVIEW -> TrackingTransactionSection.clickOnReview(userSession.userId)
            ID_WISHLIST_MENU -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuDataModel.trackerName, userSession.userId)
            ID_FAVORITE_SHOP -> TrackingTransactionSection.clickOnTokoFavorit(userSession.userId)
            ID_HOME -> TrackingBuSection.onClickBackToHome(userSession.userId)
            else -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuDataModel.trackerName, userSession.userId)
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
            ClientMenuGenerator.IDENTIFIER_TITLE_ORDER_HISTORY -> sendTrackingAllTransaction()
            ClientMenuGenerator.IDENTIFIER_TITLE_WISHLIST -> TrackingTransactionSection.clickOnWishlistViewAll()
            ClientMenuGenerator.IDENTIFIER_TITLE_FAVORITE_SHOP -> TrackingTransactionSection.clickOnFavoriteShopViewAll()
        }
        RouteManager.route(context, homeNavTitleDataModel.applink)
    }

    override fun onErrorWishlistClicked() {
        viewModel.refreshWishlistData()
    }

    override fun onWishlistCollectionClicked(wishlistModel: NavWishlistModel, position: Int) {
        TrackingTransactionSection.clickOnWishlistItem(getUserId(), wishlistModel, position)
        goToPDP(wishlistModel.id, position)
    }

    override fun onErrorFavoriteShopClicked() {
        viewModel.refreshFavoriteShopData()
    }

    override fun onFavoriteShopItemClicked(favoriteShopModel: NavFavoriteShopModel, position: Int) {
        TrackingTransactionSection.clickOnFavoriteShopItem(
            userId = getUserId(),
            position = position,
            favoriteShopModel = favoriteShopModel
        )
        val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, favoriteShopModel.id)
        startActivity(intent)
    }

    override fun showReviewProduct(uriReviewProduct: String) {
        val intent = RouteManager.getIntent(context, uriReviewProduct)
        startActivityForResult(intent, REQUEST_REVIEW_PRODUCT)
    }

    override fun onErrorReviewClicked() {
        viewModel.refreshReviewData()
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
                    TrackingProfileSection.onClickTokopediaPlus(tokopediaPlusDataModel.isSubscriber)
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

    private fun sendTrackingAllTransaction() {
        TrackingTransactionSection.clickOnAllTransaction(userSession.userId)
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
