package com.tokopedia.tokopedianow.search.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.tokopedianow.common.bottomsheet.TokoNowOnBoard20mBottomSheet
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil
import com.tokopedia.tokopedianow.common.util.TokoNowSharedPreference
import com.tokopedia.tokopedianow.common.util.TokoNowSwitcherUtil
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultTrackerListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowSearchCategoryBinding
import com.tokopedia.tokopedianow.home.presentation.view.listener.OnBoard20mBottomSheetCallback
import com.tokopedia.tokopedianow.search.analytics.SearchEmptyNoResultAdultAnalytics
import com.tokopedia.tokopedianow.search.analytics.SearchProductAdsAnalytics
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_CLICK_ATC_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_CLICK_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_IMPRESSION_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Category.CATEGORY_EMPTY_SEARCH_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Value.SRP_PRODUCT_ITEM_LABEL
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_SRP_RECOM_OOC
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_SRP_RECOM_OOC
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_DASH_SEARCH_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKOOW_SEARCH_RESULT_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.RECOM_LIST_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ATC_VARIANT
import com.tokopedia.tokopedianow.search.di.SearchComponent
import com.tokopedia.tokopedianow.search.presentation.listener.BroadMatchListener
import com.tokopedia.tokopedianow.search.presentation.listener.CTATokoNowHomeListener
import com.tokopedia.tokopedianow.search.presentation.listener.CategoryJumperListener
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactoryImpl
import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductAdsCarouselAnalytics
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTracker
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryPageLoadTimeMonitoring
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_LIST_OOC
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_TOPADS
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet.TokoNowProductFeedbackBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.CategoryChooserBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductCardCompactCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductCardCompactSimilarProductTrackerCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductRecommendationCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductRecommendationOocCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowSearchFragment :
    BaseDaggerFragment(),
    SuggestionListener,
    CategoryJumperListener,
    CTATokoNowHomeListener,
    BroadMatchListener,
    SwitcherWidgetListener,
    TokoNowEmptyStateNoResultTrackerListener,
    ChooseAddressListener,
    BannerComponentListener,
    TitleListener,
    CategoryFilterListener,
    QuickFilterListener,
    SortFilterBottomSheet.Callback,
    CategoryChooserBottomSheet.Callback,
    MiniCartWidgetListener,
    ProductItemListener,
    TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener {

    companion object {
        private const val AR_ORIGIN_TOKONOW_SEARCH_RESULT = 6

        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1
        private const val QUERY_PARAM_SERVICE_TYPE_NOW2H = "?service_type=2h"
        private const val TOP_LIST = 0
        private const val NO_PADDING = 0
        private const val WHILE_SCROLLING_VERTICALLY = 1

        @JvmStatic
        fun create(): TokoNowSearchFragment {
            return TokoNowSearchFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var productAdsAnalytics: SearchProductAdsAnalytics

    @Inject
    lateinit var searchEmptyNoResultAdultAnalytics: SearchEmptyNoResultAdultAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sharedPref: TokoNowSharedPreference

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TokoNowSearchViewModel::class.java]
    }

    private val toolbarPageName = "TokoNow Search"

    private var binding by autoClearedNullable<FragmentTokopedianowSearchCategoryBinding>()

    private var pltMonitoring: SearchCategoryPageLoadTimeMonitoring? = null
    private var searchCategoryAdapter: SearchCategoryAdapter? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null
    private var trackingQueue: TrackingQueue? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var container: ConstraintLayout? = null
    private var navToolbar: NavToolbar? = null
    private var recyclerView: RecyclerView? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var stickyView: StickySingleHeaderView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var statusBarBackground: View? = null
    private var headerBackground: AppCompatImageView? = null
    private var loaderUnify: LoaderUnify? = null
    private val recycledViewPool = RecyclerView.RecycledViewPool()

    private val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.SEARCH_PAGE

    private val miniCartWidgetSource: MiniCartSource
        get() = MiniCartSource.TokonowSRP

    private val searchCategoryToolbarHeight: Int
        get() {
            val defaultHeight =
                context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_status_height)
                    .orZero()
            val height = (navToolbar?.height ?: defaultHeight)
            val padding =
                context?.resources?.getDimensionPixelSize(unifyprinciplesR.dimen.spacing_lvl3)
                    .orZero()
            return height + padding
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring(false)
        super.onCreate(savedInstanceState)

        context?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowSearchCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews()

        configureNavToolbar()
        configureStickyView()
        configureSwipeRefreshLayout()
        configureStatusBar()
        configureRecyclerView()
        observeViewModel()

        pltMonitoring?.startNetworkPerformanceMonitoring()
        viewModel.onViewCreated(miniCartWidgetSource)
    }

    private fun findViews() {
        navToolbar = binding?.tokonowSearchCategoryNavToolbar
        recyclerView = binding?.tokonowSearchCategoryRecyclerView
        miniCartWidget = binding?.tokonowSearchCategoryMiniCart
        stickyView = binding?.tokonowSearchCategoryStickyView
        swipeRefreshLayout = binding?.tokonowSearchCategorySwipeRefreshLayout
        statusBarBackground = binding?.tokonowSearchCategoryStatusBarBackground
        headerBackground = binding?.tokonowSearchCategoryBackgroundImage
        loaderUnify = binding?.tokonowSearchCategoryLoader
        container = binding?.tokonowSearchCategoryContainer
    }

    private fun configureNavToolbar() {
        val navToolbar = navToolbar ?: return

        navToolbar.bringToFront()
        navToolbar.setToolbarPageName(toolbarPageName)
        navToolbar.setIcon(createNavToolbarIconBuilder())
        navToolbar.setupSearchbar(
            hints = getNavToolbarHint(),
            searchbarClickCallback = ::onSearchBarClick,
            disableDefaultGtmTracker = false
        )

        configureToolbarBackgroundInteraction()

        navToolbar.setOnBackButtonClickListener {
            if (viewModel.isProductFeedbackLoopVisible()) {
                viewModel.feedbackLoopTrackingMutableLivedata.value?.let {
                    sendProductFeedbackLoopEvent(
                        ProductFeedbackLoopTracker::sendClickBackBtnFeedbackLoop,
                        getUserId(),
                        it.first,
                        it.second
                    )
                }
            }
            activity?.onBackPressed()
        }
    }

    private fun updateToolbarNotification(update: Boolean) {
        if (update) navToolbar?.updateNotification()
    }

    private fun updateAdsProductCarousel(data: Pair<Int, TokoNowAdsCarouselUiModel>) {
        val visitables = viewModel.visitableListLiveData.value.orEmpty()
        val newList = visitables.toMutableList()
        newList[data.first] = data.second
        searchCategoryAdapter?.submitList(newList)
    }

    private fun initPerformanceMonitoring(isCategoryPage: Boolean) {
        pltMonitoring = SearchCategoryPageLoadTimeMonitoring()
        pltMonitoring?.initPerformanceMonitoring(isCategoryPage)
    }

    private fun stopPerformanceMonitoring(unit: Unit) {
        pltMonitoring?.stopPerformanceMonitoring()
    }

    private fun configureToolbarBackgroundInteraction() {
        val navToolbar = navToolbar ?: return

        activity?.let {
            navToolbar.setupToolbarWithStatusBar(activity = it)
        }
        viewLifecycleOwner.lifecycle.addObserver(navToolbar)

        recyclerView?.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
    }

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val toolbarTransitionRangePixel =
            context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range)
                .orZero()

        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = searchCategoryToolbarHeight,
            toolbarTransitionRangePixel = toolbarTransitionRangePixel,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) {
                }

                override fun onSwitchToLightToolbar() {
                }

                override fun onSwitchToDarkToolbar() {
                    navToolbar.hideShadow()
                }

                override fun onYposChanged(yOffset: Int) {
                }
            },
            fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
        )
    }

    private fun configureStickyView() {
        val context = context ?: return
        val top = NavToolbarExt.getToolbarHeight(context)
        stickyView?.setMargin(0.toDp(), top, 0.toDp(), 0.toDp())
    }

    private fun IconBuilder.addGlobalNav(): IconBuilder =
        if (viewModel.hasGlobalMenu) {
            this.addIcon(
                IconList.ID_NAV_GLOBAL,
                disableRouteManager = false,
                disableDefaultGtmTracker = false
            ) { }
        } else {
            this
        }

    private fun IconBuilder.addCart(): IconBuilder = this
        .addIcon(
            iconId = IconList.ID_CART,
            disableRouteManager = false,
            disableDefaultGtmTracker = false,
            onClick = {}
        )

    private fun createNavToolbarIconBuilder() =
        IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.TOKONOW))
            .addCart()
            .addGlobalNav()

    private fun onSearchBarClick(hint: String = "") {
        val autoCompleteApplink = getAutoCompleteApplink()
        val params = getModifiedAutoCompleteQueryParam(autoCompleteApplink)
        val finalApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" +
            UrlParamUtils.generateUrlParamString(params)

        RouteManager.route(context, finalApplink)
    }

    private fun getAutoCompleteApplink(): String {
        val viewModelAutoCompleteApplink = viewModel.autoCompleteApplink

        return if (viewModelAutoCompleteApplink.isEmpty()) {
            getBaseAutoCompleteApplink()
        } else {
            viewModelAutoCompleteApplink
        }
    }

    private fun getModifiedAutoCompleteQueryParam(
        autoCompleteApplink: String
    ): Map<String?, String> {
        val urlParser = URLParser(autoCompleteApplink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.BASE_SRP_APPLINK] = ApplinkConstInternalTokopediaNow.SEARCH
        params[SearchApiConst.PLACEHOLDER] =
            context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty()
        params[SearchApiConst.PREVIOUS_KEYWORD] = getKeyword()

        return params
    }

    private fun getKeyword() =
        viewModel.queryParam[SearchApiConst.Q] ?: ""

    private fun createTokoNowEmptyStateOocListener(eventCategory: String): TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
        return object : TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
            override fun onRefreshLayoutPage() {
                viewModel.onLocalizingAddressSelected()
            }

            override fun onGetFragmentManager(): FragmentManager = parentFragmentManager

            override fun onGetEventCategory(): String = eventCategory

            override fun onSwitchService() {
                viewModel.switchService()
            }
        }
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayout?.setOnRefreshListener {
            refreshLayout(
                needToResetQueryParams = false
            )
        }
    }

    private fun refreshLayout(
        needToResetQueryParams: Boolean = true
    ) {
        viewModel.onViewReloadPage(
            needToResetQueryParams = needToResetQueryParams
        )
        refreshProductRecommendation(RecomPageConstant.TOKONOW_NO_RESULT)
    }

    private fun refreshProductRecommendation(pageName: String) {
        productRecommendationViewModel.updateProductRecommendation(
            requestParam = viewModel.createProductRecommendationRequestParam(
                pageName = pageName
            )
        )
    }

    private fun configureStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */

        val activity = activity ?: return

        val statusBarBackgroundVisibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }

        statusBarBackground?.apply {
            layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
            visibility = statusBarBackgroundVisibility
        }

        setStatusBarAlpha()
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBackground?.background
        drawable?.alpha = 0
        statusBarBackground?.background = drawable
    }

    private fun configureRecyclerView() {
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())
        gridLayoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            endlessScrollListener = createEndlessScrollListener(this)
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (searchCategoryAdapter?.getItemViewType(position)) {
                        ProductItemViewHolder.LAYOUT -> SPAN_FULL_SPACE
                        else -> SPAN_COUNT
                    }
                }
            }
        }
        recyclerView?.apply {
            adapter = searchCategoryAdapter
            layoutManager = gridLayoutManager
            addProductItemDecoration()
            addOnScrollListener(createNavToolbarShadowOnScrollListener())
            endlessScrollListener?.let {
                addOnScrollListener(it)
            }
        }
    }

    private fun createEndlessScrollListener(layoutManager: GridLayoutManager) =
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                onLoadMore()
            }
        }

    private fun createNavToolbarShadowOnScrollListener() =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHeaderBackgroundOnScroll(recyclerView, dy)
            }
        }

    private fun evaluateHeaderBackgroundOnScroll(recyclerView: RecyclerView, dy: Int) {
        headerBackground?.translationY = viewModel.getTranslationYHeaderBackground(
            dy = dy
        )
        if (recyclerView.canScrollVertically(WHILE_SCROLLING_VERTICALLY)) {
            navToolbar?.showShadow(lineShadow = false)
        } else {
            navToolbar?.hideShadow(lineShadow = false)
        }
    }

    private fun onShopIdUpdated(shopId: String) {
        if (shopId.isEmpty()) return

        miniCartWidget?.initialize(
            shopIds = listOf(shopId),
            fragment = this,
            listener = this,
            autoInitializeData = false,
            pageName = miniCartWidgetPageName,
            source = miniCartWidgetSource
        )
    }

    private fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    private fun submitList(visitableList: List<Visitable<*>>) {
        if (visitableList.isNotEmpty()) showContent()
        searchCategoryAdapter?.submitList(visitableList)
        pltMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun showContent() {
        loaderUnify?.gone()
        stickyView?.visible()
    }

    private fun hideContent() {
        loaderUnify?.show()
        stickyView?.gone()
    }

    private fun updateEndlessScrollListener(hasNextPage: Boolean) {
        endlessScrollListener?.updateStateAfterGetData()
        endlessScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun onLoadMore() {
        viewModel.onLoadMore()
    }

    private fun getUserId(): String {
        val userId = userSession.userId ?: ""

        return if (userId.isEmpty()) "0" else userId
    }

    override fun onLocalizingAddressSelected() {
        viewModel.onLocalizingAddressSelected()
    }

    override fun getFragment() = this

    override fun onSeeAllCategoryClicked() {
        RouteManager.route(
            context,
            ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
            viewModel.warehouseId
        )
    }

    private fun openBottomSheetFilter(isFilterPageOpen: Boolean) {
        if (!isFilterPageOpen) return

        val mapParameter = viewModel.queryParam
        val dynamicFilterModel = viewModel.dynamicFilterModelLiveData.value

        sortFilterBottomSheet = SortFilterBottomSheet().also {
            it.show(
                parentFragmentManager,
                mapParameter,
                dynamicFilterModel,
                this
            )

            it.setOnDismissListener {
                sortFilterBottomSheet = null
                viewModel.onViewDismissFilterPage()
            }
        }
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        viewModel.onViewGetProductCount(mapParameter)
    }

    private fun onDynamicFilterModelChanged(dynamicFilterModel: DynamicFilterModel?) {
        dynamicFilterModel ?: return

        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
    }

    private fun setFilterProductCount(countText: String) {
        val productCountText = String.format(
            getString(R.string.tokopedianow_apply_filter_text),
            countText
        )

        sortFilterBottomSheet?.setResultCountText(productCountText)
        categoryChooserBottomSheet?.setResultCountText(productCountText)
    }

    private fun configureL3BottomSheet(filter: Filter?) {
        if (filter != null) {
            openCategoryChooserFilterPage(filter)
        } else {
            dismissCategoryChooserFilterPage()
        }
    }

    private fun openCategoryChooserFilterPage(filter: Filter) {
        if (categoryChooserBottomSheet != null) return

        categoryChooserBottomSheet = CategoryChooserBottomSheet()
        categoryChooserBottomSheet?.setOnDismissListener {
            viewModel.onViewDismissL3FilterPage()
        }
        categoryChooserBottomSheet?.show(
            parentFragmentManager,
            viewModel.queryParam,
            filter,
            this
        )
    }

    private fun dismissCategoryChooserFilterPage() {
        categoryChooserBottomSheet?.dismiss()
        categoryChooserBottomSheet = null
    }

    override fun getResultCount(selectedOption: Option) {
        viewModel.onViewGetProductCount(selectedOption)
    }

    private fun updateMiniCartWidget(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartSimplifiedData ?: return

        miniCartWidget?.updateData(miniCartSimplifiedData)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.updateToolbarNotification()
        viewModel.onViewUpdateCartItems(miniCartSimplifiedData)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetVisibility(isVisible: Boolean?) {
        miniCartWidget?.showWithCondition(isVisible == true)
        if (!isVisible()) miniCartWidget?.hideCoachMark()
        setupPadding(isVisible == true)
    }

    private fun setupPadding(isShowMiniCartWidget: Boolean) {
        miniCartWidget?.post {
            val paddingBottom = if (isShowMiniCartWidget) {
                getMiniCartHeight()
            } else {
                context?.resources?.getDimensionPixelSize(
                    unifyprinciplesR.dimen.layout_lvl0
                ).orZero()
            }
            stickyView?.setPadding(NO_PADDING, NO_PADDING, NO_PADDING, paddingBottom)
        }
    }

    private fun getMiniCartHeight(): Int {
        return miniCartWidget?.height.orZero() - context?.resources?.getDimension(unifyprinciplesR.dimen.unify_space_16)
            ?.toInt().orZero()
    }

    private fun notifyAdapterItemChange(indices: List<Int>) {
        val searchCategoryAdapter = searchCategoryAdapter ?: return

        indices.forEach {
            if (it in searchCategoryAdapter.list.indices) {
                searchCategoryAdapter.notifyItemChanged(it, true)
            }
        }
    }

    private fun openATCVariantBottomSheet(productId: String, shopId: String) {
        val context = context ?: return

        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = getCDListName(),
            startActivitResult = this::startActivityForResult
        )
    }

    override fun onProductNonVariantQuantityChanged(
        productItemDataView: ProductItemDataView,
        quantity: Int
    ) {
        viewModel.onViewATCProductNonVariant(productItemDataView, quantity)
    }

    private fun showSuccessAddToCartMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_toaster_see)) {
            miniCartWidget?.showMiniCartListBottomSheet(this)
        }
    }

    private fun showSuccessRemoveFromCartMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_toaster_ok))
    }

    private fun showToaster(
        message: String?,
        toasterType: Int,
        actionText: String = "",
        clickListener: (View) -> Unit = { }
    ) {
        val view = view ?: return
        message ?: return
        if (message.isEmpty()) return

        Toaster.toasterCustomBottomHeight = getMiniCartHeight()

        Toaster.build(
            view,
            message,
            Toaster.LENGTH_LONG,
            toasterType,
            actionText,
            clickListener
        ).show()
    }

    private fun showErrorATCMessage(message: String?) {
        showToaster(message, Toaster.TYPE_ERROR)
    }

    private fun updateHeaderBackgroundVisibility(isVisible: Boolean) {
        if (!isVisible) {
            headerBackground?.setImageResource(R.color.tokopedianow_dms_transparent)
        } else {
            context?.resources?.apply {
                val background = VectorDrawableCompat.create(
                    this,
                    R.drawable.tokopedianow_ic_header_background,
                    context?.theme
                )
                headerBackground?.setImageDrawable(background)
            }
        }
        headerBackground?.showWithCondition(isVisible)
    }

    private fun updateContentVisibility(isLoadingVisible: Boolean) {
        swipeRefreshLayout?.isRefreshing = isLoadingVisible

        if (isLoadingVisible) return
        showOnBoardingBottomSheet()
    }

    private fun showNetworkErrorHelper(throwable: Throwable?) {
        val context = activity ?: return
        val view = view ?: return

        NetworkErrorHelper.showEmptyState(
            context,
            view,
            ErrorHandler.getErrorMessage(context, throwable)
        ) {
            viewModel.onViewReloadPage()
        }

        pltMonitoring?.stopPerformanceMonitoring()
    }

    private fun routeApplink(applink: String?) {
        applink ?: return

        RouteManager.route(context, applink)
    }

    override fun onPause() {
        super.onPause()

        trackingQueue?.sendAll()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onViewResumed()
    }

    override fun goToTokopediaNowHome() {
        routeApplink(ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onRemoveFilterClick(option: Option) {
        viewModel.onViewRemoveFilter(option)
    }

    private fun sendAddToCartRecommendationTrackingEvent(
        addToCartDataTrackerModel: AddToCartDataTrackerModel
    ) {
        val product = addToCartDataTrackerModel.productRecommendation
        val recommendationItem =
            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
        SearchResultTracker.trackClickAddToCartProduct(
            eventLabel = getEventLabel(),
            userId = userSession.userId,
            quantity = addToCartDataTrackerModel.quantity,
            cartId = addToCartDataTrackerModel.cartId,
            product = recommendationItem,
            eventCategory = getEventCategory(false),
            eventAction = getAtcEventAction(),
            dimension40 = getListValue(false, recommendationItem)
        )
    }

    override fun onClickSwitcherTo15M() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onClickSwitcherTo2H() {
        hideContent()
        viewModel.setUserPreference(ServiceType.NOW_2H)
    }

    private fun setUserPreferenceData(result: Result<SetUserPreference.SetUserPreferenceData>) {
        showContent()
        when (result) {
            is Success -> {
                context?.apply {
                    // Set user preference data to local cache
                    updateLocalCacheModel(
                        data = result.data,
                        context = this
                    )

                    // Refresh the page
                    gridLayoutManager?.scrollToPosition(TOP_LIST)
                    refreshLayout()

                    // Show toaster
                    showOnBoardingToaster(
                        data = result.data
                    )

                    // Refresh mini cart
                    viewModel.refreshMiniCart()
                }
            }

            is Fail -> { /* do nothing */
            }
        }
    }

    private fun updateLocalCacheModel(
        data: SetUserPreference.SetUserPreferenceData,
        context: Context
    ) {
        ChooseAddressUtils.updateTokoNowData(
            context = context,
            warehouseId = data.warehouseId,
            shopId = data.shopId,
            serviceType = data.serviceType,
            warehouses = data.warehouses.map {
                LocalWarehouseModel(
                    it.warehouseId.toLongOrZero(),
                    it.serviceType
                )
            }
        )
    }

    private fun showOnBoardingToaster(data: SetUserPreference.SetUserPreferenceData) {
        /*
           Note :
           - Toaster will be shown when switching service type to 2 hours
           - When switching to 20 minutes, toaster will be shown if only OnBoard20mBottomSheet has been shown before
         */

        val needToShowOnBoardBottomSheet =
            viewModel.needToShowOnBoardBottomSheet(sharedPref.get20mBottomSheetOnBoardShown())
        if (!data.warehouseId.toLongOrZero().isZero() && !needToShowOnBoardBottomSheet) {
            showSwitcherToaster(data.serviceType)
        }
    }

    private fun showOnBoardingBottomSheet() {
        val needToShowOnBoardBottomSheet =
            viewModel.needToShowOnBoardBottomSheet(sharedPref.get20mBottomSheetOnBoardShown())
        if (needToShowOnBoardBottomSheet) {
            show20mOnBoardBottomSheet()
        }
    }

    private fun show20mOnBoardBottomSheet() {
        TokoNowOnBoard20mBottomSheet
            .newInstance()
            .show(
                childFragmentManager,
                OnBoard20mBottomSheetCallback(
                    onBackTo2hClicked = {
                        RouteManager.route(
                            context,
                            ApplinkConstInternalTokopediaNow.HOME + QUERY_PARAM_SERVICE_TYPE_NOW2H
                        )
                    },
                    onDismiss = {
                        sharedPref.set20mBottomSheetOnBoardShown(true)
                    }
                )
            )
    }

    private fun showSwitcherToaster(serviceType: String) {
        TokoNowServiceTypeUtil.getServiceTypeRes(
            key = TokoNowServiceTypeUtil.SWITCH_SERVICE_TYPE_TOASTER_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            showToaster(
                message = getString(it),
                toasterType = Toaster.TYPE_NORMAL
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AdultManager.handleActivityResult(
            activity,
            requestCode,
            resultCode,
            data,
            object : AdultManager.Callback {
                override fun onFail() {
                    activity?.finish()
                }

                override fun onVerificationSuccess(message: String?) {}

                override fun onLoginPreverified() {}
            }
        )
    }

    private fun createProductRecommendationOocCallback() = ProductRecommendationOocCallback(
        lifecycle = viewLifecycleOwner.lifecycle,
        userSession = userSession,
        trackingQueue = trackingQueue,
        activity = activity,
        eventLabel = getEventLabel(),
        eventActionClicked = getClickEventAction(true),
        eventActionImpressed = getImpressionEventAction(true),
        eventCategory = getEventCategory(true),
        getListValue = { recommendationItem ->
            getListValue(true, recommendationItem)
        }
    )

    private fun createProductRecommendationCallback() = ProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        searchViewModel = viewModel,
        activity = activity,
        startActivityForResult = ::startActivityForResult,
        trackingQueue = trackingQueue,
        eventLabel = getEventLabel(),
        eventActionClicked = getClickEventAction(false),
        eventActionImpressed = getImpressionEventAction(false),
        eventCategory = getEventCategory(false),
        onAddToCartBlocked = this::showToasterWhenAddToCartBlocked,
        getListValue = { recommendationItem ->
            getListValue(false, recommendationItem)
        }
    )

    private fun createSimilarProductCallback(isCategoryPage: Boolean): ProductCardCompactSimilarProductTrackerCallback {
        return ProductCardCompactSimilarProductTrackerCallback(isCategoryPage)
    }

    private fun createProductCardCompactCallback(): ProductCardCompactCallback =
        ProductCardCompactCallback { productId, similarProductTrackerListener ->
            context?.apply {
                val intent = TokoNowSimilarProductBottomSheetActivity.createNewIntent(
                    this,
                    productId,
                    similarProductTrackerListener
                )
                startActivity(intent)
            }
        }

    private fun createProductAdsCarouselCallback(analytics: ProductAdsCarouselAnalytics): ProductAdsCarouselListener {
        return ProductAdsCarouselListener(
            context = context,
            viewModel = viewModel,
            analytics = analytics,
            startActivityResult = ::startActivityForResult,
            showToasterWhenAddToCartBlocked = ::showToasterWhenAddToCartBlocked
        )
    }

    override fun onProductCardAddToCartBlocked() = showToasterWhenAddToCartBlocked()

    private fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            toasterType = Toaster.TYPE_ERROR
        )
    }

    private fun getNavToolbarHint() =
        listOf(HintData(viewModel.query, viewModel.query))

    private fun getBaseAutoCompleteApplink() =
        ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" +
            "${SearchApiConst.Q}=${viewModel.query}" + "&" +
            "${SearchApiConst.NAVSOURCE}=$TOKONOW"

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(SearchComponent::class.java).inject(this)
    }

    override fun trackingEventLabel(): String = ""

    private fun observeViewModel() {
        viewModel.visitableListLiveData.observe(::submitList)
        viewModel.hasNextPageLiveData.observe(::updateEndlessScrollListener)
        viewModel.isFilterPageOpenLiveData.observe(::openBottomSheetFilter)
        viewModel.dynamicFilterModelLiveData.observe(::onDynamicFilterModelChanged)
        viewModel.productCountAfterFilterLiveData.observe(::setFilterProductCount)
        viewModel.isL3FilterPageOpenLiveData.observe(::configureL3BottomSheet)
        viewModel.shopIdLiveData.observe(::onShopIdUpdated)
        viewModel.miniCartWidgetLiveData.observe(::updateMiniCartWidget)
        viewModel.isShowMiniCartLiveData.observe(::updateMiniCartWidgetVisibility)
        viewModel.updatedVisitableIndicesLiveData.observe(::notifyAdapterItemChange)
        viewModel.successAddToCartMessageLiveData.observe(::showSuccessAddToCartMessage)
        viewModel.successRemoveFromCartMessageLiveData.observe(::showSuccessRemoveFromCartMessage)
        viewModel.errorATCMessageLiveData.observe(::showErrorATCMessage)
        viewModel.isHeaderBackgroundVisibleLiveData.observe(::updateHeaderBackgroundVisibility)
        viewModel.isContentLoadingLiveData.observe(::updateContentVisibility)
        viewModel.quickFilterTrackingLiveData.observe(::sendTrackingQuickFilter)
        viewModel.addToCartTrackingLiveData.observe(::sendAddToCartTrackingEvent)
        viewModel.increaseQtyTrackingLiveData.observe(::sendIncreaseQtyTrackingEvent)
        viewModel.decreaseQtyTrackingLiveData.observe(::sendDecreaseQtyTrackingEvent)
        viewModel.isShowErrorLiveData.observe(::showNetworkErrorHelper)
        viewModel.routeApplinkLiveData.observe(::routeApplink)
        viewModel.deleteCartTrackingLiveData.observe(::sendDeleteCartTrackingEvent)
        viewModel.generalSearchEventLiveData.observe(::sendTrackingGeneralEvent)
        viewModel.oocOpenScreenTrackingEvent.observe(::sendOOCOpenScreenTracking)
        viewModel.setUserPreferenceLiveData.observe(::setUserPreferenceData)
        viewModel.querySafeLiveData.observe(::showDialogAgeRestriction)
        viewModel.updateToolbarNotification.observe(::updateToolbarNotification)
        viewModel.updateAdsCarouselLiveData.observe(::updateAdsProductCarousel)
        viewModel.stopPerformanceMonitoringLiveData.observe(::stopPerformanceMonitoring)

        productRecommendationViewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showSuccessAddToCartMessage(result.data.errorMessage.joinToString(separator = ", "))
                    viewModel.refreshMiniCart()
                    updateToolbarNotification(true)
                }

                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(
                            ErrorHandler.getErrorMessage(
                                activity,
                                result.throwable
                            )
                        )
                    }
                }
            }
        }

        productRecommendationViewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    viewModel.refreshMiniCart()
                }

                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(
                            ErrorHandler.getErrorMessage(
                                activity,
                                result.throwable
                            )
                        )
                    }
                }
            }
        }

        productRecommendationViewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showSuccessRemoveFromCartMessage(result.data.second)
                    viewModel.refreshMiniCart()
                }

                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(
                            ErrorHandler.getErrorMessage(
                                activity,
                                result.throwable
                            )
                        )
                    }
                }
            }
        }

        productRecommendationViewModel.atcDataTracker.observe(viewLifecycleOwner) {
            sendAddToCartRecommendationTrackingEvent(it)
        }

        productRecommendationViewModel.updateToolbarNotification.observe(viewLifecycleOwner) {
            updateToolbarNotification(it)
        }

        viewModel.generalSearchEventLiveData.observe(this::sendTrackingGeneralEvent)
        viewModel.addToCartBroadMatchTrackingLiveData.observe(this::sendATCBroadMatchTrackingEvent)
        viewModel.feedbackLoopTrackingMutableLivedata.observe(this::sendFeedbackLoopImpressionEvent)
        viewModel.firstPageSuccessTriggerLiveData.observe(this::triggerFirstPageExperiment)
        viewModel.loadMoreSuccessTriggerLiveData.observe(this::sendImpressPageExperiment)
    }

    private fun sendTrackingGeneralEvent(dataLayer: Map<String, Any>) {
        SearchTracking.sendGeneralEvent(dataLayer)
    }

    private fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>) {
        val (quantity, _, productItemDataView) = atcData
        val productType = productItemDataView.type

        if (productType == PRODUCT_ADS_CAROUSEL) {
            trackAdsProductAddToCart(productItemDataView, quantity)
        } else {
            trackSearchProductAddToCart(productItemDataView, quantity)
        }
    }

    private fun trackAdsProductAddToCart(
        productItemDataView: ProductItemDataView,
        quantity: Int
    ) {
        val title = getString(R.string.tokopedianow_product_ads_carousel_title)
        productAdsAnalytics.trackProductAddToCart(
            position = productItemDataView.position,
            title = title,
            quantity = quantity,
            shopId = productItemDataView.shopId,
            shopName = productItemDataView.shopName,
            shopType = productItemDataView.shopType,
            categoryBreadcrumbs = productItemDataView.categoryBreadcrumbs,
            product = productItemDataView.productCardModel
        )
    }

    private fun trackSearchProductAddToCart(
        productItemDataView: ProductItemDataView,
        quantity: Int
    ) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendAddToCartEvent(
            productItemDataView,
            viewModel.query,
            getUserId(),
            sortFilterParams,
            quantity
        )
    }

    private fun sendDeleteCartTrackingEvent(productId: String) {
        SearchTracking.sendDeleteCartEvent(productId)
    }

    private fun sendIncreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendIncreaseQtyEvent(viewModel.query, productId)
    }

    private fun sendDecreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendDecreaseQtyEvent(viewModel.query, productId)
    }

    private fun createTypeFactory(): SearchTypeFactoryImpl {
        return SearchTypeFactoryImpl(
            tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(
                TOKONOW_DASH_SEARCH_PAGE
            ),
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            productCardCompactSimilarProductTrackerListener = createSimilarProductCallback(false),
            switcherWidgetListener = this,
            tokoNowEmptyStateNoResultListener = this,
            tokoNowEmptyStateNoResultTrackerListener = this,
            suggestionListener = this,
            categoryJumperListener = this,
            ctaTokoNowHomeListener = this,
            broadMatchListener = this,
            feedbackWidgetListener = this,
            productRecommendationOocListener = createProductRecommendationOocCallback(),
            productRecommendationBindOocListener = createProductRecommendationOocCallback(),
            productRecommendationListener = createProductRecommendationCallback().copy(
                query = viewModel.query
            ),
            productCardCompactListener = createProductCardCompactCallback(),
            productAdsCarouselListener = createProductAdsCarouselCallback(productAdsAnalytics)
        )
    }

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        SearchTracking.sendSuggestionClickEvent(viewModel.query, suggestionDataView.suggestion)

        performNewProductSearch(suggestionDataView.query)
    }

    override fun onFindInTokopediaClick() {
        routeApplink(ApplinkConst.HOME)

        val queryParams = "${SearchApiConst.Q}=${viewModel.query}"
        val applinkToSearchResult = "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?$queryParams"

        RouteManager.route(context, applinkToSearchResult)
    }

    override fun onProductImpressed(productItemDataView: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return

        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendProductImpressionEvent(
            trackingQueue,
            productItemDataView,
            viewModel.query,
            getUserId(),
            sortFilterParams
        )
    }

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        if (isWishlistSelected) {
            SearchTracking.trackClickAddToWishlist(
                viewModel.warehouseId,
                productId
            )
        } else {
            SearchTracking.trackClickRemoveFromWishlist(
                viewModel.warehouseId,
                productId
            )
        }
        viewModel.updateWishlistStatus(
            productId,
            isWishlistSelected
        )
        showToaster(descriptionToaster, type, ctaToaster) {
            ctaClickListener?.invoke()
        }
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)
        val uri = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productItemDataView.productCardModel.productId
        )
        val appLink = viewModel.createAffiliateLink(uri)

        SearchTracking.sendProductClickEvent(
            productItemDataView,
            viewModel.query,
            getUserId(),
            sortFilterParams
        )

        RouteManager.route(context, appLink)
    }

    override fun openFilterPage() {
        SearchTracking.sendOpenFilterPageEvent()
        viewModel.onViewOpenFilterPage()
    }

    override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        val filterParamMap = applySortFilterModel.selectedFilterMapParameter
        val paramMapWithoutExclude =
            FilterHelper.createParamsWithoutExcludes(filterParamMap) as Map<String?, String>
        val filterParams = UrlParamUtils.generateUrlParamString(paramMapWithoutExclude)
        SearchTracking.sendApplySortFilterEvent(filterParams)

        viewModel.onViewApplySortFilter(applySortFilterModel)
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        SearchTracking.sendApplyCategoryL2FilterEvent(option.name)
        viewModel.onViewClickCategoryFilterChip(option, isSelected)
    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        val productId = productItemDataView.productCardModel.productId
        val shopId = productItemDataView.shop.id

        SearchTracking.sendChooseVariantEvent(
            viewModel.query,
            productItemDataView.productCardModel.productId
        )

        openATCVariantBottomSheet(productId, shopId)
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String, param: String) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendBannerClickEvent(
            channelModel,
            viewModel.query,
            getUserId(),
            sortFilterParams
        )

        context?.let { context ->
            TokoNowSwitcherUtil.switchService(
                context = context,
                param = param,
                onRefreshPage = {
                    viewModel.switchService()
                },
                onRedirectPage = {
                    RouteManager.route(it, applink)
                }
            )
        }
    }

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendBannerImpressionEvent(
            channelModel,
            viewModel.query,
            getUserId(),
            sortFilterParams
        )
    }

    override fun onApplyCategory(selectedOption: Option) {
        val filterParam = selectedOption.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) +
            "=" +
            selectedOption.value
        SearchTracking.sendApplyCategoryL3FilterEvent(filterParam)

        viewModel.onViewApplyFilterFromCategoryChooser(selectedOption)
    }

    override fun onCategoryJumperItemClick(item: CategoryJumperDataView.Item) {
        val context = context ?: return

        SearchTracking.sendClickCategoryJumperEvent(item.title)
        RouteManager.route(context, item.applink)
    }

    override fun onCTAToTokopediaNowHomeClick() {
        SearchTracking.sendClickCTAToHome()
        goToTokopediaNowHome()
    }

    override fun getRecyclerViewPool() = recycledViewPool

    override fun onBroadMatchItemImpressed(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        broadMatchIndex: Int
    ) {
        val trackingQueue = trackingQueue ?: return

        SearchTracking.sendBroadMatchImpressionEvent(
            trackingQueue = trackingQueue,
            broadMatchItemDataView = broadMatchItemDataView,
            keyword = viewModel.query,
            userId = getUserId(),
            position = broadMatchIndex
        )
    }

    override fun onBroadMatchItemClicked(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        broadMatchIndex: Int
    ) {
        SearchTracking.sendBroadMatchClickEvent(
            broadMatchItemDataView = broadMatchItemDataView,
            keyword = viewModel.query,
            userId = getUserId(),
            position = broadMatchIndex
        )
        RouteManager.route(context, broadMatchItemDataView.appLink)
    }

    override fun onBroadMatchItemATCNonVariant(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int
    ) {
        viewModel.onViewATCBroadMatchItem(broadMatchItemDataView, quantity, broadMatchIndex)
    }

    override fun onBroadMatchSeeAllClicked(title: String, appLink: String) {
        SearchTracking.sendBroadMatchSeeAllClickEvent(title, viewModel.query)

        RouteManager.route(context, getBroadMatchSeeAllApplink(appLink))
    }

    override fun onBroadMatchAddToCartBlocked() {
        showToasterWhenAddToCartBlocked()
    }

    override fun onFeedbackCtaClicked() {
        viewModel.feedbackLoopTrackingMutableLivedata.value?.let {
            sendProductFeedbackLoopEvent(
                ProductFeedbackLoopTracker::sendClickSarankanCtaFeedbackLoop,
                getUserId(),
                it.first,
                it.second
            )
            var trackData: Triple<String, String, Boolean>? = null
            viewModel.feedbackLoopTrackingMutableLivedata.value?.let { it1 ->
                trackData = Triple(getUserId(), it1.first, it1.second)
            }
            TokoNowProductFeedbackBottomSheet().also { it1 ->
                it1.showBottomSheet(activity?.supportFragmentManager, view, trackData)
            }
        }
    }

    override fun trackClickDefaultPrimaryButton() {
        searchEmptyNoResultAdultAnalytics.sendClickLearnMoreNoResultForAdultProductEvent(
            keyword = viewModel.query
        )
    }

    override fun trackImpressEmptyStateNoResult() {
        searchEmptyNoResultAdultAnalytics.sendImpressionNoResultForAdultProductEvent(
            keyword = viewModel.query
        )
    }

    private fun performNewProductSearch(queryParams: String) {
        val context = context ?: return

        val applinkToSearchResult = ApplinkConstInternalTokopediaNow.SEARCH + "?" + queryParams
        val modifiedApplinkToSearchResult = modifyApplinkToSearchResult(applinkToSearchResult)

        RouteManager.route(context, modifiedApplinkToSearchResult)
    }

    private fun modifyApplinkToSearchResult(applink: String): String {
        val urlParser = URLParser(applink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.PREVIOUS_KEYWORD] = viewModel.query

        return ApplinkConstInternalTokopediaNow.SEARCH + "?" +
            UrlParamUtils.generateUrlParamString(params)
    }

    private fun getQueryParamWithoutExcludes(): Map<String, String> {
        return FilterHelper.createParamsWithoutExcludes(viewModel.queryParam)
    }

    private fun getCDListName(): String {
        return TOKONOW_SEARCH_PRODUCT_ATC_VARIANT
    }

    private fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>) {
        SearchTracking.sendQuickFilterClickEvent(
            quickFilterTracking.first,
            quickFilterTracking.second
        )
    }

    private fun getImpressionEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            IMPRESSION_SRP_RECOM_OOC
        } else {
            ACTION_IMPRESSION_SRP_PRODUCT
        }
    }

    private fun getClickEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            CLICK_SRP_RECOM_OOC
        } else {
            ACTION_CLICK_SRP_PRODUCT
        }
    }

    private fun getAtcEventAction(): String {
        return ACTION_CLICK_ATC_SRP_PRODUCT
    }

    private fun getEventCategory(isOOC: Boolean): String {
        return if (isOOC) TOKOOW_SEARCH_RESULT_PAGE else CATEGORY_EMPTY_SEARCH_RESULT
    }

    private fun getListValue(isOOC: Boolean, recommendationItem: RecommendationItem): String {
        return if (isOOC) {
            String.format(
                VALUE_LIST_OOC,
                RECOM_LIST_PAGE,
                recommendationItem.recommendationType,
                if (recommendationItem.isTopAds) VALUE_TOPADS else ""
            )
        } else {
            String.format(
                SRP_PRODUCT_ITEM_LABEL,
                "${recommendationItem.recommendationType} - " +
                    "${recommendationItem.pageName} - ${recommendationItem.header}"
            )
        }
    }

    private fun getEventLabel(): String {
        return viewModel.query
    }

    private fun sendATCBroadMatchTrackingEvent(
        atcTrackingData: Triple<Int, String, ProductCardCompactCarouselItemUiModel>
    ) {
        val (quantity, _, broadMatchItemDataView) = atcTrackingData

        SearchTracking.sendBroadMatchAddToCartEvent(
            broadMatchItemDataView,
            viewModel.query,
            getUserId(),
            quantity
        )
    }

    private fun getBroadMatchSeeAllApplink(appLink: String) =
        if (appLink.startsWith(ApplinkConst.TokopediaNow.SEARCH)) {
            modifyApplinkToSearchResult(appLink)
        } else {
            appLink
        }

    private fun sendOOCOpenScreenTracking(isTracked: Boolean) {
        SearchTracking.sendOOCOpenScreenTracking(userSession.isLoggedIn)
    }

    private fun showDialogAgeRestriction(querySafeModel: QuerySafeModel) {
        if (!querySafeModel.isQuerySafe) {
            AdultManager.showAdultPopUp(
                this,
                AR_ORIGIN_TOKONOW_SEARCH_RESULT,
                "${querySafeModel.warehouseId} - ${viewModel.query}"
            )
        }
    }

    private fun sendFeedbackLoopImpressionEvent(data: Pair<String, Boolean>?) {
        data?.let {
            sendProductFeedbackLoopEvent(
                ProductFeedbackLoopTracker::sendImpressionFeedbackLoop,
                getUserId(),
                it.first,
                it.second
            )
        }
    }

    private fun sendProductFeedbackLoopEvent(
        event: (userId: String, warehouseId: String, isSearchResult: Boolean) -> Unit,
        userId: String,
        warehouseId: String,
        isSearchResult: Boolean
    ) {
        event.invoke(userId, warehouseId, isSearchResult)
    }

    private fun triggerFirstPageExperiment(unit: Unit) {
        pltMonitoring?.startRenderPerformanceMonitoring()
        sendImpressPageExperiment(unit)
    }

    private fun sendImpressPageExperiment(unit: Unit) {
        SearchResultTracker.sendImpressSearchPageExperimentEvent(
            keyword = viewModel.query,
            numberOfProduct = viewModel.getRows(),
            warehouseId = viewModel.warehouseId
        )
    }
}
