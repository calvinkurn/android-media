package com.tokopedia.homenav.mainnav.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
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
import com.tokopedia.homenav.mainnav.view.analytics.TrackingOthers
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavigationDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.interactor.listener.BuyAgainCallback
import com.tokopedia.homenav.mainnav.view.interactor.listener.TokopediaPlusCallback
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.view.activity.HomeNavPerformanceInterface
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.recommendation_widget_common.DEFAULT_PAGE_NAME
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.asNavSource
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {

    private var mainNavDataFetched: Boolean = false

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

    var pageSource = NavSource.DEFAULT
    var pageSourcePath: String = ""
    var pageName = DEFAULT_PAGE_NAME
    var isActingAsAccountPage: Boolean = false

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

        pageSource = args.StringMainNavArgsSourceKey.asNavSource()
        pageSourcePath = args.StringMainNavArgsSourcePathKey
        isActingAsAccountPage = args.StringMainNavArgsIsActingAsAccountPageKey

        viewModel.setPageSource(pageSource)
        context?.let {
            viewModel.setProfileCache(getProfileCacheData(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(
                getString(
                    if (!isActingAsAccountPage) R.string.title_main_nav else R.string.title_main_nav_account_page
                )
            )
            it.setBackButtonType(
                if (!isActingAsAccountPage) {
                    NavToolbar.Companion.BackType.BACK_TYPE_CLOSE
                } else {
                    NavToolbar.Companion.BackType.BACK_TYPE_NONE
                }
            ) {
                TrackingOthers.onClickCloseButton(pageSource, pageSourcePath)
            }
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
                REQUEST_REVIEW_PRODUCT -> viewModel.refreshReviewData()
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

        viewModel.onAtcProductState.observe(viewLifecycleOwner) {
            val (isError, message) = it

            if (isError) {
                onShowToast(message, Toaster.TYPE_ERROR)
            } else {
                val succeedMessage = getString(R.string.transaction_buy_again_atc_message)
                val ctaTitle = getString(R.string.transaction_buy_again_atc_cta)

                onShowToast(succeedMessage, Toaster.TYPE_NORMAL, ctaTitle) {
                    startActivity(RouteManager.getIntent(requireContext(), ApplinkConst.CART))
                }
            }
        }
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
        TrackingProfileSection.onClickProfileSection(eventLabel, pageSource, pageSourcePath)
        if (applink == ApplinkConst.User.ACCOUNT && pageSource == NavSource.ACCOUNT) {
            activity?.onBackPressed()
        } else {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
    }

    override fun onTickerDescClicked(applink: String) {
        TrackingProfileSection.onClickProfileSection(TrackingProfileSection.CLICK_OPEN_SHOP, pageSource, pageSourcePath)
        RouteManager.route(context, applink)
    }

    override fun onProfileLoginClicked() {
        TrackingProfileSection.onClickLoginButton(pageSource, pageSourcePath)
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
        TrackingProfileSection.onClickRegisterButton(pageSource, pageSourcePath)
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.User.REGISTER), REQUEST_REGISTER)
    }

    override fun onErrorProfileRefreshClicked(position: Int) {
        viewModel.refreshProfileData()
    }

    override fun onErrorShopInfoRefreshClicked(position: Int) {
        viewModel.refreshUserShopData()
    }

    override fun onErrorTransactionListClicked(position: Int) {
        viewModel.refreshTransactionListData()
    }

    override fun onRefreshBuyAgainIfError() {
        viewModel.refreshBuyAgainData()
    }

    override fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel) {
        view?.let {
            hitClickTrackingBasedOnId(homeNavMenuDataModel)
            if (homeNavMenuDataModel.sectionId == MainNavConst.Section.ORDER ||
                homeNavMenuDataModel.sectionId == MainNavConst.Section.BU_ICON ||
                homeNavMenuDataModel.sectionId == MainNavConst.Section.ACTIVITY
            ) {
                if (homeNavMenuDataModel.applink.isNotEmpty()) {
                    handleClickFromPageSource(homeNavMenuDataModel.applink) {
                        RouteManager.route(context, homeNavMenuDataModel.applink)
                    }
                } else {
                    NavigationRouter.MainNavRouter.navigateTo(
                        it,
                        NavigationRouter.PAGE_CATEGORY,
                        bundleOf("title" to homeNavMenuDataModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuDataModel)
                    )
                }
            } else {
                RouteManager.route(requireContext(), homeNavMenuDataModel.applink)
            }
            handleAppLogEnterMethod(homeNavMenuDataModel)
        }
    }

    private fun handleAppLogEnterMethod(homeNavMenuDataModel: HomeNavMenuDataModel) {
        if (homeNavMenuDataModel.id == ID_WISHLIST_MENU) {
            AppLogAnalytics.putEnterMethod(EnterMethod.CLICK_WISHLIST_ICONACCOUNT)
        }
    }

    private fun handleClickFromPageSource(applink: String, routing: (String) -> Unit) {
        if (validateTargetMenu(applink)) {
            activity?.onBackPressed()
        } else routing.invoke(applink)
    }

    private fun validateTargetMenu(applink: String): Boolean {
        return validateHomeUohPage(applink) || validateHomeWishlistPage(applink)
    }

    private fun validateHomeUohPage(applink: String) =
        applink == ApplinkConst.PURCHASE_ORDER && pageSource == NavSource.HOME_UOH

    private fun validateHomeWishlistPage(applink: String) =
        applink == ApplinkConst.WISHLIST && pageSource == NavSource.HOME_WISHLIST

    private fun hitClickTrackingBasedOnId(homeNavMenuDataModel: HomeNavMenuDataModel) {
        if(homeNavMenuDataModel.sectionId == MainNavConst.Section.BU_ICON) {
            TrackingOthers.onClickBusinessUnitItem(homeNavMenuDataModel.itemTitle, pageSource, pageSourcePath)
        } else if(homeNavMenuDataModel.id == ID_HOME) {
            TrackingOthers.onClickBackToHome(pageSource, pageSourcePath)
        } else {
            TrackingOthers.clickOnUserMenu(homeNavMenuDataModel.itemTitle, pageSource, pageSourcePath)
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
            ClientMenuGenerator.IDENTIFIER_TITLE_ORDER_HISTORY -> TrackingTransactionSection.getClickViewAllTransaction(pageSource, pageSourcePath)
            ClientMenuGenerator.IDENTIFIER_TITLE_WISHLIST -> TrackingTransactionSection.clickOnWishlistViewAll(pageSource, pageSourcePath)
        }
        handleClickFromPageSource(homeNavTitleDataModel.applink) {
            RouteManager.route(context, homeNavTitleDataModel.applink)
        }
    }

    override fun onWishlistCardClicked(wishlistModel: NavWishlistModel, position: Int) {
        TrackingTransactionSection.clickOnWishlistItem(getUserId(), wishlistModel, position, pageSource, pageSourcePath)
        RouteManager.route(context, ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL, wishlistModel.id)
    }

    override fun onWishlistCardImpressed(wishlistModel: NavWishlistModel, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnWishlist(
                userId = userSession.userId,
                position = position,
                wishlistModel = wishlistModel,
                pageSource = pageSource
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
        TrackingTransactionSection.clickReviewCard(position, userSession.userId, element, isClickStar, ratingValue, pageSource, pageSourcePath)
        val intent = RouteManager.getIntent(context, uri)
        startActivityForResult(intent, REQUEST_REVIEW_PRODUCT)
    }

    override fun onReviewCardImpressed(element: NavReviewModel, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnReviewProduct(
                userId = userSession.userId,
                element = element,
                position = position,
                pageSource = pageSource
            )
        )
    }

    override fun onErrorReviewClicked() {
    }

    override fun onOrderCardClicked(
        applink: String,
        trackingLabel: String?,
        orderId: String,
        position: Int,
    ) {
        if(trackingLabel != null) {
            TrackingTransactionSection.clickOnOrderStatus(
                orderId,
                trackingLabel,
                pageSource,
                pageSourcePath,
                position,
                userSession.userId
            )
        } else {
            TrackingTransactionSection.getClickViewAllTransaction(pageSource, pageSourcePath)
        }
        handleClickFromPageSource(applink) { RouteManager.route(context, applink) }
    }

    override fun onOrderCardImpressed(trackingLabel: String, orderId: String, position: Int) {
        getTrackingQueueObj()?.putEETracking(
            TrackingTransactionSection.getImpressionOnOrderStatus(
                userId = userSession.userId,
                orderLabel = trackingLabel,
                orderId = orderId,
                position = position,
                pageSource = pageSource
            )
        )
    }

    override fun onViewAllCardClicked(sectionId: Int, applink: String) {
        TrackingTransactionSection.clickOnViewAllCard(sectionId, pageSource, pageSourcePath)
        RouteManager.route(context, applink)
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
            TokopediaPlusCallback(
                source = pageSource,
                pageSourcePath = pageSourcePath,
                onRefreshTokopediaPlus = {
                    viewModel.refreshTokopediaPlusData()
                }
            ),
            BuyAgainCallback(
                fragment = this,
                mainNavListener = this,
                addToCart = { productId, shopId ->
                    viewModel.addToCartProduct(productId, shopId)
                }
            ).also {
                it.setPageDetail(pageSource, pageSourcePath, pageName)
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

    private fun onShowToast(
        message: String,
        type: Int,
        actionText: String = "",
        clickListener: () -> Unit = {}
    ) {
        val view = view ?: return

        Toaster.build(
            view,
            message,
            Snackbar.LENGTH_SHORT,
            type,
            actionText = actionText,
            clickListener = {
                clickListener()
            }
        ).show()
    }

    override fun getScreenName() = ""

    companion object {
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
        private const val REQUEST_LOGIN = 1234
        private const val REQUEST_REGISTER = 2345
        private const val OFFSET_TO_SHADOW = 100
        private const val REQUEST_REVIEW_PRODUCT = 999
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
    }
}
