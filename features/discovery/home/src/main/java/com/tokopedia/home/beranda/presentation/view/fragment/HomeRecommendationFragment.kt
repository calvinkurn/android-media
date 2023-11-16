package com.tokopedia.home.beranda.presentation.view.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationAddWishlistLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationAddWishlistNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickNonLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationRemoveWishlistLogin
import com.tokopedia.home.beranda.di.BerandaComponent
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.helper.HomeFeedEndlessScrollListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeEggListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeFeedItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationItemGridViewHolder.Companion.LAYOUT
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationVideoWidgetManager
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

class HomeRecommendationFragment :
    Fragment(),
    HomeRecommendationListener,
    TopAdsBannerClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModel: HomeRecommendationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeRecommendationViewModel::class.java]
    }

    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view) }

    private val adapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        HomeRecommendationTypeFactoryImpl(
            this,
            this,
            HomeRecommendationVideoWidgetManager(recyclerView, viewLifecycleOwner)
        )
    }

    private val adapter by lazy { HomeRecommendationAdapter(adapterFactory) }

    private val staggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(
            DynamicChannelTabletConfiguration.getSpanCountForHomeRecommendationAdapter(
                requireContext()
            ),
            StaggeredGridLayoutManager.VERTICAL
        )
    }
    private var endlessRecyclerViewScrollListener: HomeFeedEndlessScrollListener? = null

    private var currentPage = 0
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
        observeStateFlow()
        observeLiveData()
    }

    override fun onPause() {
        TopAdsGtmTracker.getInstance().eventRecomendationProductView(
            trackingQueue,
            tabName.lowercase(Locale.getDefault()),
            userSessionInterface.isLoggedIn
        )
        trackingQueue.sendAll()
        super.onPause()
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
        if (HomeRecommendationController.isUsingRecommendationCard()) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.homeRecommendationCardState.collect {
                        when (it) {
                            is HomeRecommendationCardState.Success -> {
                                adapter.submitList(it.data.homeRecommendations)
                                updateScrollEndlessListener(it.data.isHasNextPage)
                            }

                            is HomeRecommendationCardState.Loading -> {
                                adapter.submitList(it.data.homeRecommendations)
                            }

                            is HomeRecommendationCardState.EmptyData -> {
                                adapter.submitList(it.data.homeRecommendations)
                            }

                            is HomeRecommendationCardState.Fail -> {
                                adapter.submitList(it.data.homeRecommendations)
                                showToasterError()
                            }

                            is HomeRecommendationCardState.FailNextPage -> {
                                adapter.submitList(it.data.homeRecommendations)
                                showToasterError()
                            }

                            is HomeRecommendationCardState.LoadingMore -> {
                                adapter.submitList(it.data.homeRecommendations)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showToasterError() {
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
        recyclerView?.addItemDecoration(HomeFeedItemDecoration(4f.toDpInt()))
        recyclerView?.adapter = adapter
        parentPool?.setMaxRecycledViews(
            LAYOUT,
            MAX_RECYCLED_VIEWS
        )
        recyclerView?.setRecycledViewPool(parentPool)
        createEndlessRecyclerViewListener()
        endlessRecyclerViewScrollListener?.let { recyclerView?.addOnScrollListener(it) }
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
                    currentPage = page
                    viewModel.fetchNextHomeRecommendation(
                        tabName,
                        recomId,
                        DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
                        page,
                        getLocationParamString(),
                        sourceType
                    )
                }
            }
    }

    override fun onProductImpression(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    ) {
        val tabNameLowerCase = tabName.lowercase(Locale.getDefault())
        if (homeRecommendationItemDataModel.recommendationProductItem.isTopAds) {
            context?.let {
                TopAdsUrlHitter(className).hitImpressionUrl(
                    it,
                    homeRecommendationItemDataModel.recommendationProductItem.trackerImageUrl,
                    homeRecommendationItemDataModel.recommendationProductItem.id,
                    homeRecommendationItemDataModel.recommendationProductItem.name,
                    homeRecommendationItemDataModel.recommendationProductItem.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT
                )
            }
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(
                    getRecommendationProductViewLoginTopAds(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    ) as HashMap<String, Any>
                )
            } else {
                trackingQueue.putEETracking(
                    getRecommendationProductViewNonLoginTopAds(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    ) as HashMap<String, Any>
                )
            }
        } else {
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(
                    getRecommendationProductViewLogin(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    ) as HashMap<String, Any>
                )
            } else {
                trackingQueue.putEETracking(
                    getRecommendationProductViewNonLogin(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    ) as HashMap<String, Any>
                )
            }
        }
    }

    override fun onProductClick(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    ) {
        val tabNameLowerCase = tabName.lowercase(Locale.getDefault())
        if (homeRecommendationItemDataModel.recommendationProductItem.isTopAds) {
            context?.let {
                TopAdsUrlHitter(className).hitClickUrl(
                    it,
                    homeRecommendationItemDataModel.recommendationProductItem.clickUrl,
                    homeRecommendationItemDataModel.recommendationProductItem.id,
                    homeRecommendationItemDataModel.recommendationProductItem.name,
                    homeRecommendationItemDataModel.recommendationProductItem.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT
                )
            }
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    getRecommendationProductClickLoginTopAds(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    )
                )
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    getRecommendationProductClickNonLoginTopAds(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    )
                )
            }
        } else {
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    getRecommendationProductClickLogin(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    )
                )
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    getRecommendationProductClickNonLogin(
                        tabNameLowerCase,
                        homeRecommendationItemDataModel
                    )
                )
            }
        }
        goToProductDetail(homeRecommendationItemDataModel.recommendationProductItem.id, position)
    }

    override fun onProductThreeDotsClick(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    ) {
        showProductCardOptions(
            this,
            createProductCardOptionsModel(homeRecommendationItemDataModel, position)
        )
    }

    override fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getBannerRecommendation(
                bannerRecommendationDataModel
            ) as HashMap<String, Any>
        )
    }

    override fun onBannerTopAdsOldClick(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        position: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            HomeRecommendationTracking.getClickBannerTopAdsOld(
                homeTopAdsRecommendationBannerDataModelDataModel.topAdsImageViewModel,
                tabIndex,
                position
            )
        )
        RouteManager.route(
            context,
            homeTopAdsRecommendationBannerDataModelDataModel.topAdsImageViewModel?.applink
        )
    }

    override fun onBannerTopAdsOldImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        position: Int
    ) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressionBannerTopAdsOld(
                homeTopAdsRecommendationBannerDataModelDataModel.topAdsImageViewModel,
                tabIndex,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun onBannerTopAdsClick(
        homeTopAdsRecommendationBannerDataUiModel: HomeRecommendationBannerTopAdsUiModel,
        position: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            HomeRecommendationTracking.getClickBannerTopAdsOld(
                homeTopAdsRecommendationBannerDataUiModel.topAdsImageViewModel,
                tabIndex,
                position
            )
        )

        HomeRecommendationTracking.sendClickBannerTopAdsTracking(
            homeTopAdsRecommendationBannerDataUiModel,
            position,
            userSessionInterface.userId
        )
        RouteManager.route(
            context,
            homeTopAdsRecommendationBannerDataUiModel.topAdsImageViewModel?.applink
        )
    }

    override fun onBannerTopAdsImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsUiModel,
        position: Int
    ) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressionBannerTopAdsOld(
                homeTopAdsRecommendationBannerDataModelDataModel.topAdsImageViewModel,
                tabIndex,
                position
            ) as HashMap<String, Any>
        )

        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressBannerTopAdsTracking(
                homeTopAdsRecommendationBannerDataModelDataModel,
                position,
                userSessionInterface.userId
            ) as HashMap<String, Any>
        )
    }

    override fun onEntityCardImpressionListener(
        item: RecomEntityCardUiModel,
        position: Int
    ) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressEntityCardTracking(
                item,
                position,
                userSessionInterface.userId
            )
        )
    }

    override fun onEntityCardClickListener(item: RecomEntityCardUiModel, position: Int) {
        HomeRecommendationTracking.sendClickEntityCardTracking(
            item,
            position,
            userSessionInterface.userId
        )
        context?.let {
            RouteManager.route(it, item.appLink)
        }
    }

    override fun onPlayVideoWidgetClick(
        element: HomeRecommendationPlayWidgetUiModel,
        position: Int
    ) {
        HomeRecommendationTracking.sendClickVideoRecommendationCardTracking(
            element,
            position,
            userSessionInterface.userId
        )
        context?.let {
            RouteManager.route(it, element.appLink)
        }
    }

    override fun onPlayVideoWidgetImpress(
        element: HomeRecommendationPlayWidgetUiModel,
        position: Int
    ) {
        trackingQueue.putEETracking(
            HomeRecommendationTracking.getImpressPlayVideoWidgetTracking(
                element,
                position,
                userSessionInterface.userId
            )
        )
    }

    override fun onRetryGetProductRecommendationData() {
        viewModel.fetchHomeRecommendation(
            tabName,
            recomId,
            DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
            getLocationParamString(),
            sourceType = sourceType
        )
    }

    override fun onRetryGetNextProductRecommendationData() {
        val currentPage = endlessRecyclerViewScrollListener?.currentPage ?: currentPage
        viewModel.fetchNextHomeRecommendation(
            tabName,
            recomId,
            DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE,
            currentPage,
            getLocationParamString(),
            sourceType
        )
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
    }

    private fun goToProductDetail(productId: String, position: Int) {
        activity?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, position)
            try {
                startActivityForResult(intent, REQUEST_FROM_PDP)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
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
                sourceType = sourceType
            )
        }
    }

    fun scrollToTop() {
        if (view == null) {
            return
        }
        val staggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager?
        if (staggeredGridLayoutManager != null && staggeredGridLayoutManager.findFirstVisibleItemPositions(
                null
            )[0] > BASE_POSITION
        ) {
            recyclerView?.scrollToPosition(BASE_POSITION)
        }
        recyclerView?.smoothScrollToPosition(0)
    }

    private fun createProductCardOptionsModel(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted =
            homeRecommendationItemDataModel.recommendationProductItem.isWishlist
        productCardOptionsModel.productId =
            homeRecommendationItemDataModel.recommendationProductItem.id
        productCardOptionsModel.isTopAds =
            homeRecommendationItemDataModel.recommendationProductItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl =
            homeRecommendationItemDataModel.recommendationProductItem.wishListUrl
        productCardOptionsModel.topAdsClickUrl =
            homeRecommendationItemDataModel.recommendationProductItem.clickUrl
        productCardOptionsModel.productName =
            homeRecommendationItemDataModel.recommendationProductItem.name
        productCardOptionsModel.productImageUrl =
            homeRecommendationItemDataModel.recommendationProductItem.imageUrl
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
                        getRecommendationAddWishlistLogin(
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
                        getRecommendationRemoveWishlistLogin(
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
                getRecommendationAddWishlistNonLogin(productCardOptionsModel.productId, tabName)
            )
            RouteManager.route(context, ApplinkConst.LOGIN)
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

    companion object {
        private const val className =
            "com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment"
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
        ): HomeRecommendationFragment {
            val homeFeedFragment = HomeRecommendationFragment()
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
        super.onDestroyView()
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        applink?.let { RouteManager.route(context, applink) }
    }
}
