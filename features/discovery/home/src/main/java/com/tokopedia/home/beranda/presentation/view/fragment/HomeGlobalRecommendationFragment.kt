package com.tokopedia.home.beranda.presentation.view.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogGlidePageInterface
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.analytics.byteio.GlidePageTrackObject
import com.tokopedia.analytics.byteio.addVerticalTrackListener
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation.sendCardClickAppLog
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation.sendCardShowAppLog
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation.sendProductClickAppLog
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation.sendProductShowAppLog
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asCardTrackModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.beranda.di.BerandaComponent
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.helper.HomeFeedEndlessScrollListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeEggListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.GlobalHomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeFeedItemDecoration
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController
import com.tokopedia.home.beranda.presentation.view.helper.ScrollToTopAdapterDataObserver
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.home.beranda.presentation.viewModel.HomeGlobalRecommendationViewModel
import com.tokopedia.home.util.HomeRefreshType
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home.util.toRecomRequestType
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.byteio.RefreshType
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayVideoWidgetManager
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardGridViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.RecomTemporary
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

@RecomTemporary
class HomeGlobalRecommendationFragment :
    BaseRecommendationFragment(),
    GlobalRecomListener,
    TopAdsBannerClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModel: HomeGlobalRecommendationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeGlobalRecommendationViewModel::class.java]
    }

    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view) }

    private val playVideoWidgetViewListener by lazy {
        object : PlayVideoWidgetView.Listener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) = Unit
            override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) = Unit
        }
    }

    private val adapter by lazy {
        val factory = ForYouRecommendationTypeFactoryImpl(
            this,
            PlayVideoWidgetManager(recyclerView, viewLifecycleOwner)
        )

        GlobalHomeRecommendationAdapter(factory)
    }

    private val staggeredGridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        StaggeredGridLayoutManager(
            DynamicChannelTabletConfiguration.getSpanCountForHomeRecommendationAdapter(
                requireContext()
            ),
            StaggeredGridLayoutManager.VERTICAL
        )
    }

    private val mScrollTouchListener by lazy {
        createScrollTouchListener()
    }

    private val observeRecyclerViewScrollToTop by lazy {
        ScrollToTopAdapterDataObserver(recyclerView)
    }

    private var endlessRecyclerViewScrollListener: HomeFeedEndlessScrollListener? = null

    private var totalScrollY = 0
    private var tabIndex = 0
    private var recomId = 0
    private var sourceType = ""
    private var tabName: String = ""
    private var hasLoadData = false
    private var homeEggListener: HomeEggListener? = null
    private var homeTabFeedListener: HomeTabFeedListener? = null
    private var parentPool: RecyclerView.RecycledViewPool? = null
    private var homeCategoryListener: HomeCategoryListener? = null
    private var component: BerandaComponent? = null
    private var coachmarkLocalCache: CoachMarkLocalCache? = null
    private var homeRecomCurrentPage = 1

    private var startY = 0.0F
    private var startX = 0.0F

    private var hasApplogScrollListener = false
    private var refreshType = HomeRefreshType.FIRST_OPEN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_home_feed_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel.topAdsBannerNextPage = homeCategoryListener?.getTopAdsBannerNextPage() ?: ""
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.coachmarkLocalCache = CoachMarkLocalCache(context = context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP && data != null && data.hasExtra(
                WIHSLIST_STATUS_IS_WISHLIST
            )
        ) {
            val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID) ?: ""
            val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false)
            val position = data.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1)
            updateWishlist(id, wishlistStatusFromPdp, position)
        }
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArgs()
        fetchHomeRecommendationRollence()
        setupRecyclerView()
        loadFirstPageData()
        initListeners()
        observeLiveData()
        observeStateFlow()
    }

    override fun onPause() {
        TopAdsGtmTracker.getInstance().eventRecomendationProductView(
            trackingQueue,
            tabName.lowercase(Locale.getDefault()),
            userSessionInterface.isLoggedIn
        )
        trackingQueue.sendAll()
        super.onPause()
        removeOnItemTouchListener()
    }

    override fun onResume() {
        super.onResume()
        handlingNestedRecyclerView()
    }

    private fun fetchHomeRecommendationRollence() {
        HomeRecommendationController.fetchRecommendationCardRollence()
    }

    private fun setupArgs() {
        tabIndex = arguments?.getInt(ARG_TAB_INDEX) ?: -1
        recomId = arguments?.getInt(ARG_RECOM_ID) ?: -1
        tabName = arguments?.getString(ARG_TAB_NAME) ?: ""
        sourceType = arguments?.getString(ARG_SOURCE_TYPE) ?: ""
    }

    private fun initInjector() {
        if (activity != null) {
            if (component == null) {
                component = initBuilderComponent().build()
            }
            component?.inject(this)
        }
    }

    private fun initBuilderComponent(): DaggerBerandaComponent.Builder {
        return DaggerBerandaComponent.builder()
            .baseAppComponent((requireActivity().application as BaseMainApplication).baseAppComponent)
    }

    private fun observeLiveData() {
        if (!HomeRecommendationController.isUsingRecommendationCard()) {
            viewModel.homeRecommendationLiveData.observe(
                viewLifecycleOwner
            ) { data ->
                adapter.submitList(data.homeRecommendations)
            }

            viewModel.homeRecommendationNetworkLiveData.observe(
                viewLifecycleOwner
            ) { result ->
                if (result.isFailure) {
                    view?.let {
                        if (adapter.itemCount > 1) {
                            Toaster.build(
                                it,
                                getString(R.string.home_error_connection),
                                Snackbar.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(abstractionR.string.title_try_again),
                                View.OnClickListener {
                                    endlessRecyclerViewScrollListener?.loadMoreNextPage()
                                }
                            ).show()
                        }
                    }
                } else {
                    updateScrollEndlessListener(result.getOrNull()?.isHasNextPage ?: false)
                }
            }
        }
    }

    private fun observeStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeRecommendationCardState.collect {
                    when (it) {
                        is HomeRecommendationCardState.Success -> {
                            adapter.submitList(it.data.homeRecommendations) {
                                updateScrollEndlessListener(it.data.isHasNextPage)
                            }
                        }

                        is HomeRecommendationCardState.Loading -> {
                            adapter.submitList(it.data.homeRecommendations)
                        }

                        is HomeRecommendationCardState.EmptyData -> {
                            adapter.submitList(it.data.homeRecommendations)
                        }

                        is HomeRecommendationCardState.Fail -> {
                            adapter.submitList(it.data.homeRecommendations)
                        }

                        is HomeRecommendationCardState.FailNextPage -> {
                            adapter.submitList(it.data.homeRecommendations)
                        }

                        is HomeRecommendationCardState.LoadingMore -> {
                            adapter.submitList(it.data.homeRecommendations)
                        }
                    }
                }
            }
        }
    }

    private fun updateScrollEndlessListener(hasNextPage: Boolean) {
        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun setupRecyclerView() {
        recyclerView?.layoutManager = staggeredGridLayoutManager
        (recyclerView?.layoutManager as? StaggeredGridLayoutManager?)?.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView?.addItemDecoration(HomeFeedItemDecoration())
        recyclerView?.adapter = adapter
        parentPool?.setMaxRecycledViews(
            RecommendationCardGridViewHolder.LAYOUT,
            MAX_RECYCLED_VIEWS
        )
        recyclerView?.setRecycledViewPool(parentPool)
        createEndlessRecyclerViewListener()
        endlessRecyclerViewScrollListener?.let { recyclerView?.addOnScrollListener(it) }

        // fixes staggered inflation issue
        adapter.registerAdapterDataObserver(observeRecyclerViewScrollToTop)
    }

    private fun createScrollTouchListener() = object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = e.y
                    startX = e.x
                }

                MotionEvent.ACTION_MOVE -> {
                    val currentY = e.y
                    val currentX = e.x
                    val deltaY = currentY - startY
                    val deltaX = currentX - startX

                    // case 1: position > 0, then scroll to down disable recyclerview parent
                    // case 2: position == 0, then scroll up / down, enable recyclerview parent
                    // Check if deltaY is more than deltaX will be scrolling to vertical
                    if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        if (isScrolledToTop()) {
                            // Check if deltaY is negative, indicating scroll to down
                            if (deltaY < 0) {
                                rv.parent.requestDisallowInterceptTouchEvent(true)
                            } else {
                                rv.parent.requestDisallowInterceptTouchEvent(false)
                            }
                        } else {
                            rv.parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }

                    // Update startY for the next event
                    startY = currentY
                    startX = currentX
                }
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    private fun handlingNestedRecyclerView() {
        recyclerView?.addOnItemTouchListener(mScrollTouchListener)
    }

    private fun removeOnItemTouchListener() {
        recyclerView?.removeOnItemTouchListener(mScrollTouchListener)
    }

    fun setListener(
        homeCategoryListener: HomeCategoryListener?,
        homeEggListener: HomeEggListener?,
        homeTabFeedListener: HomeTabFeedListener?
    ) {
        this.homeCategoryListener = homeCategoryListener
        this.homeEggListener = homeEggListener
        this.homeTabFeedListener = homeTabFeedListener
    }

    fun setParentPool(parentPool: RecyclerView.RecycledViewPool?) {
        this.parentPool = parentPool
    }

    private fun createEndlessRecyclerViewListener() {
        endlessRecyclerViewScrollListener =
            object : HomeFeedEndlessScrollListener(recyclerView?.layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    observeRecyclerViewScrollToTop.forciblyFirstTimeScrollToTop = false

                    homeRecomCurrentPage = page
                    viewModel.fetchNextHomeRecommendation(
                        tabIndex,
                        tabName,
                        recomId,
                        DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
                        page,
                        getLocationParamString(),
                        sourceType,
                        adapter.currentList.toList()
                    )
                }
            }
    }

    override fun onProductCardImpressed(model: RecommendationCardModel, position: Int) {
        sendProductShowAppLog(
            model.asProductTrackModel()
        )
        val tabNameLowerCase = tabName.lowercase(Locale.getDefault())
        if (model.recommendationProductItem.isTopAds) {
            context?.let {
                TopAdsUrlHitter(className).hitImpressionUrl(
                    it,
                    model.recommendationProductItem.trackerImageUrl,
                    model.recommendationProductItem.id,
                    model.recommendationProductItem.name,
                    model.recommendationProductItem.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT
                )
            }
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(
                    HomeRecommendationTracking.getRecommendationProductViewLoginTopAds(
                        tabNameLowerCase,
                        model
                    ) as HashMap<String, Any>
                )
            } else {
                trackingQueue.putEETracking(
                    HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds(
                        tabNameLowerCase,
                        model
                    ) as HashMap<String, Any>
                )
            }
        } else {
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(
                    HomeRecommendationTracking.getRecommendationProductViewLogin(
                        tabNameLowerCase,
                        model
                    ) as HashMap<String, Any>
                )
            } else {
                trackingQueue.putEETracking(
                    HomeRecommendationTracking.getRecommendationProductViewNonLogin(
                        tabNameLowerCase,
                        model
                    ) as HashMap<String, Any>
                )
            }
        }
    }

    override fun onProductCardClicked(model: RecommendationCardModel, position: Int) {
        sendProductClickAppLog(model.asProductTrackModel())
        val tabNameLowerCase = tabName.lowercase(Locale.getDefault())
        if (model.recommendationProductItem.isTopAds) {
            context?.let {
                TopAdsUrlHitter(className).hitClickUrl(
                    it,
                    model.recommendationProductItem.clickUrl,
                    model.recommendationProductItem.id,
                    model.recommendationProductItem.name,
                    model.recommendationProductItem.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT
                )
            }
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    HomeRecommendationTracking.getRecommendationProductClickLoginTopAds(
                        tabNameLowerCase,
                        model
                    )
                )
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    HomeRecommendationTracking.getRecommendationProductClickNonLoginTopAds(
                        tabNameLowerCase,
                        model
                    )
                )
            }
        } else {
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    HomeRecommendationTracking.getRecommendationProductClickLogin(
                        tabNameLowerCase,
                        model
                    )
                )
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    HomeRecommendationTracking.getRecommendationProductClickNonLogin(
                        tabNameLowerCase,
                        model
                    )
                )
            }
        }
        goToProductDetail(model.recommendationProductItem.id, position)
    }

    override fun onProductCardThreeDotsClicked(model: RecommendationCardModel, position: Int) {
        showProductCardOptions(
            this,
            createProductCardOptionsModel(model, position)
        )
    }

    override fun onBannerImpressed(model: BannerRecommendationModel) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getBannerRecommendation(model) as HashMap<String, Any>
        )
    }

    override fun onBannerTopAdsOldClick(model: BannerOldTopAdsModel, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            HomeRecommendationTracking.getClickBannerTopAdsOld(
                model.topAdsImageUiModel,
                tabIndex,
                position
            )
        )

        val rvContext = recyclerView?.context

        rvContext?.let {
            RouteManager.route(it, model.topAdsImageUiModel?.applink)
        }
    }

    override fun onBannerTopAdsOldImpress(model: BannerOldTopAdsModel, position: Int) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressionBannerTopAdsOld(
                model.topAdsImageUiModel,
                tabIndex,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun onBannerTopAdsClick(model: BannerTopAdsModel, position: Int) {
        sendCardClickAppLog(model.asCardTrackModel())
        AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format("${model.pageName}_${position + 1}"))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            HomeRecommendationTracking.getClickBannerTopAdsOld(
                model.topAdsImageUiModel,
                tabIndex,
                position
            )
        )

        HomeRecommendationTracking.sendClickBannerTopAdsTracking(
            model,
            position,
            userSessionInterface.userId
        )

        val rvContext = recyclerView?.context
        rvContext?.let {
            RouteManager.route(
                it,
                model.topAdsImageUiModel?.applink
            )
        }
    }

    override fun onBannerTopAdsImpress(model: BannerTopAdsModel, position: Int) {
        sendCardShowAppLog(
            model.asCardTrackModel()
        )
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressionBannerTopAdsOld(
                model.topAdsImageUiModel,
                tabIndex,
                position
            ) as HashMap<String, Any>
        )

        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressBannerTopAdsTracking(
                model,
                position,
                userSessionInterface.userId
            ) as HashMap<String, Any>
        )
    }

    override fun onContentCardImpressed(item: ContentCardModel, position: Int) {
        sendCardShowAppLog(
            item.asCardTrackModel()
        )
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressEntityCardTracking(
                item,
                position,
                userSessionInterface.userId
            )
        )
    }

    override fun onContentCardClicked(item: ContentCardModel, position: Int) {
        sendCardClickAppLog(item.asCardTrackModel())
        AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format("${item.pageName}_${position + 1}"))
        HomeRecommendationTracking.sendClickEntityCardTracking(
            item,
            position,
            userSessionInterface.userId
        )
        val rvContext = recyclerView?.context
        rvContext?.let {
            RouteManager.route(it, item.appLink)
        }
    }

    override fun onPlayCardClicked(element: PlayCardModel, position: Int) {
        sendCardClickAppLog(element.asCardTrackModel())
        AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format("${element.pageName}_${position + 1}"))
        HomeRecommendationTracking.sendClickVideoRecommendationCardTracking(
            element,
            position,
            userSessionInterface.userId
        )
        val rvContext = recyclerView?.context
        rvContext?.let {
            RouteManager.route(it, element.appLink)
        }
    }

    override fun onPlayCardImpressed(element: PlayCardModel, position: Int) {
        sendCardShowAppLog(
            element.asCardTrackModel()
        )
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressPlayVideoWidgetTracking(
                element,
                position,
                userSessionInterface.userId
            )
        )
    }

    override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
        playVideoWidgetViewListener.onVideoFinishedPlaying(view)
    }

    override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) {
        playVideoWidgetViewListener.onVideoError(view, error)
    }

    override fun onBannerClicked(model: BannerRecommendationModel) {
        HomePageTracking.eventClickOnBannerFeed(model, model.tabName)
        RouteManager.route(requireContext(), model.applink)
    }

    override fun onRetryGetProductRecommendationData() {
        viewModel.fetchHomeRecommendation(
            tabName,
            recomId,
            DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
            getLocationParamString(),
            sourceType = sourceType,
            refreshType = RefreshType.REFRESH,
            tabIndex = tabIndex
        )
    }

    override fun onRetryGetNextProductRecommendationData() {
        endlessRecyclerViewScrollListener?.loadMoreNextPage()
    }

    private fun initListeners() {
        if (view == null) return
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalScrollY += dy
                if (!userVisibleHint) {
                    return
                }

                homeEggListener?.hideEggOnScroll()
                if (homeTabFeedListener != null) {
                    homeTabFeedListener?.onFeedContentScrolled(dy, totalScrollY)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!userVisibleHint) {
                    return
                }
                if (homeTabFeedListener != null) {
                    homeTabFeedListener?.onFeedContentScrollStateChanged(newState)
                }
            }
        })
        trackVerticalScroll()
    }

    private fun trackVerticalScroll() {
        if(hasApplogScrollListener) return
        recyclerView?.addVerticalTrackListener {
            GlidePageTrackObject(
                listName = tabName,
                listNum = tabIndex,
                distanceToTop = (parentFragment as? AppLogGlidePageInterface)?.getDistanceToTop().orZero() + totalScrollY
            )
        }
        hasApplogScrollListener = true
    }

    private fun goToProductDetail(productId: String, position: Int) {
        val rvContext = recyclerView?.context
        rvContext?.let {
            if (it is Activity) {
                val mActivity = it as? Activity
                val intent = RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productId
                )
                intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, position)
                try {
                    mActivity?.startActivityForResult(intent, REQUEST_FROM_PDP)
                } catch (exception: ActivityNotFoundException) {
                    exception.printStackTrace()
                }
            }
        }
    }

    private fun updateWishlist(id: String, isWishlist: Boolean, position: Int) {
        if (position > -1 && adapter.itemCount > 0 &&
            adapter.itemCount > position
        ) {
            viewModel.updateWhistlist(id, position, isWishlist)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadFirstPageData()
    }

    private fun loadFirstPageData() {
        if (userVisibleHint && isAdded && activity != null && !hasLoadData) {
            hasLoadData = true

            viewModel.fetchHomeRecommendation(
                tabName,
                recomId,
                DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
                getLocationParamString(),
                sourceType = sourceType,
                refreshType = refreshType.toRecomRequestType(),
                tabIndex = tabIndex
            )
        }
    }

    override fun scrollToTop() {
        if (view == null) {
            return
        }
        val staggeredGridLayoutManager =
            recyclerView?.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null && staggeredGridLayoutManager.findFirstVisibleItemPositions(
                null
            )[0] > BASE_POSITION
        ) {
            recyclerView?.scrollToPosition(BASE_POSITION)
        }
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun setRefreshType(refreshType: HomeRefreshType) {
        this.refreshType = refreshType
    }

    private fun createProductCardOptionsModel(
        model: RecommendationCardModel,
        position: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted =
            model.recommendationProductItem.isWishlist
        productCardOptionsModel.productId =
            model.recommendationProductItem.id
        productCardOptionsModel.isTopAds =
            model.recommendationProductItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl =
            model.recommendationProductItem.wishListUrl
        productCardOptionsModel.topAdsClickUrl =
            model.recommendationProductItem.clickUrl
        productCardOptionsModel.productName =
            model.recommendationProductItem.name
        productCardOptionsModel.productImageUrl =
            model.recommendationProductItem.imageUrl
        productCardOptionsModel.productPosition = position
        return productCardOptionsModel
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                        HomeRecommendationTracking.getRecommendationAddWishlistLogin(
                            productCardOptionsModel.productId,
                            tabName
                        )
                    )

                    showMessageSuccessAddWishlistV2(wishlistResult)
                    if (productCardOptionsModel.isTopAds) {
                        hitWishlistClickUrl(
                            productCardOptionsModel
                        )
                    }
                } else {
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                        HomeRecommendationTracking.getRecommendationRemoveWishlistLogin(
                            productCardOptionsModel.productId,
                            tabName
                        )
                    )
                    showMessageSuccessRemoveWishlistV2(wishlistResult)
                }
                updateWishlist(
                    productCardOptionsModel.productId,
                    wishlistResult.isAddWishlist,
                    productCardOptionsModel.productPosition
                )
            } else {
                showMessageFailedWishlistV2Action(wishlistResult)
            }
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                HomeRecommendationTracking.getRecommendationAddWishlistNonLogin(
                    productCardOptionsModel.productId,
                    tabName
                )
            )
            val rvContext = recyclerView?.context
            rvContext?.let {
                RouteManager.route(it, ApplinkConst.LOGIN)
            }
        }
    }

    private fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                productCardOptionsModel.topAdsClickUrl + CLICK_TYPE_WISHLIST,
                productCardOptionsModel.productId,
                productCardOptionsModel.productName,
                productCardOptionsModel.productImageUrl
            )
        }
    }

    private fun showMessageSuccessAddWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)

        context?.let { context ->
            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                wishlistResult,
                context,
                view
            )
        }
    }

    private fun showMessageSuccessRemoveWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)

        context?.let { context ->
            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                wishlistResult,
                context,
                view
            )
        }
    }

    private fun showMessageFailedWishlistV2Action(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = activity?.findViewById<View>(android.R.id.content)

        var msgError = ErrorHandler.getErrorMessage(activity, Throwable())
        if (wishlistResult.messageV2.isNotEmpty()) msgError = wishlistResult.messageV2

        if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
            activity?.let { activity ->
                view?.let {
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                        msgError,
                        wishlistResult.ctaTextV2,
                        wishlistResult.ctaActionV2,
                        it,
                        activity
                    )
                }
            }
        } else {
            view?.let { AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(msgError, it) }
        }
    }

    private fun isScrolledToTop(): Boolean {
        val layoutManager = recyclerView?.layoutManager as? StaggeredGridLayoutManager
        return layoutManager?.findFirstVisibleItemPositions(null)?.firstOrNull() == 0
    }

    companion object {
        private const val className =
            "com.tokopedia.home.beranda.presentation.view.fragment.HomeGlobalRecommendationFragment"
        private const val HOME_RECOMMENDATION_FRAGMENT = "home_recommendation_fragment"
        const val ARG_TAB_INDEX = "ARG_TAB_INDEX"
        const val ARG_RECOM_ID = "ARG_RECOM_ID"
        const val ARG_SOURCE_TYPE = "ARG_SOURCE_TYPE"
        const val ARG_TAB_NAME = "ARG_TAB_NAME"
        const val DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE = 12

        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 349
        private const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"

        private const val MAX_RECYCLED_VIEWS = 20
        private const val BASE_POSITION = 10

        fun newInstance(
            tabIndex: Int,
            recomId: Int,
            tabName: String,
            sourceType: String
        ): HomeGlobalRecommendationFragment {
            val homeFeedFragment = HomeGlobalRecommendationFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TAB_INDEX, tabIndex)
            bundle.putInt(ARG_RECOM_ID, recomId)
            bundle.putString(ARG_TAB_NAME, tabName)
            bundle.putString(ARG_SOURCE_TYPE, sourceType)
            homeFeedFragment.arguments = bundle
            return homeFeedFragment
        }
    }

    private fun getLocationParamString(): String {
        return ChooseAddressUtils.getLocalizingAddressData(requireContext())
            ?.convertToLocationParams() ?: ""
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        adapter.unregisterAdapterDataObserver(observeRecyclerViewScrollToTop)
        super.onDestroyView()
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        val rvContext = recyclerView?.context
        rvContext?.let { mContext ->
            applink?.let {
                RouteManager.route(mContext, applink)
            }
        }
    }
}
