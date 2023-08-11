package com.tokopedia.tokopedianow.searchcategory.presentation.view

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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_NO_RESULT
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_SHARE
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.tokopedianow.common.bottomsheet.TokoNowOnBoard20mBottomSheet
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_2H
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapProductItemToRecommendationItem
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil
import com.tokopedia.tokopedianow.common.util.TokoNowSharedPreference
import com.tokopedia.tokopedianow.common.util.TokoNowSwitcherUtil.switchService
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowSearchCategoryBinding
import com.tokopedia.tokopedianow.home.presentation.view.listener.OnBoard20mBottomSheetCallback
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductAdsCarouselAnalytics
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
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductRecommendationCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductRecommendationOocCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductCardCompactSimilarProductTrackerCallback
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity
import com.tokopedia.track.TrackApp
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
import kotlin.collections.set

abstract class BaseSearchCategoryFragment:
    BaseDaggerFragment(),
    ChooseAddressListener,
    BannerComponentListener,
    TitleListener,
    CategoryFilterListener,
    QuickFilterListener,
    SortFilterBottomSheet.Callback,
    CategoryChooserBottomSheet.Callback,
    MiniCartWidgetListener,
    ProductItemListener,
    SwitcherWidgetListener,
    TokoNowEmptyStateNoResultListener,
    TokoNowProductCardListener,
    FeedbackWidgetListener {

    companion object {
        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1
        private const val QUERY_PARAM_SERVICE_TYPE_NOW2H = "?service_type=2h"
        private const val TOP_LIST = 0
        private const val NO_PADDING = 0
        private const val WHILE_SCROLLING_VERTICALLY = 1
    }

    private var binding by autoClearedNullable<FragmentTokopedianowSearchCategoryBinding>()

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sharedPref: TokoNowSharedPreference

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    protected var searchCategoryAdapter: SearchCategoryAdapter? = null
    protected var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    protected var sortFilterBottomSheet: SortFilterBottomSheet? = null
    protected var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null
    protected var trackingQueue: TrackingQueue? = null
    protected var gridLayoutManager: GridLayoutManager? = null
    protected var container: ConstraintLayout? = null
    protected var navToolbar: NavToolbar? = null
    protected var recyclerView: RecyclerView? = null
    protected var miniCartWidget: MiniCartWidget? = null
    protected var stickyView: StickySingleHeaderView? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected var statusBarBackground: View? = null
    protected var headerBackground: AppCompatImageView? = null
    protected var loaderUnify: LoaderUnify? = null
    protected val recycledViewPool = RecyclerView.RecycledViewPool()

    protected abstract val toolbarPageName: String

    private val searchCategoryToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_status_height).orZero()
            val height = (navToolbar?.height ?: defaultHeight)
            val padding = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).orZero()
            return height + padding
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { trackingQueue = TrackingQueue(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTokopedianowSearchCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)

        configureNavToolbar()
        configureStickyView()
        configureSwipeRefreshLayout()
        configureStatusBar()
        configureRecyclerView()
        observeViewModel()

        getViewModel().onViewCreated(miniCartWidgetSource)
    }

    protected open fun findViews(view: View) {
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

    protected open fun configureNavToolbar() {
        val navToolbar = navToolbar ?: return

        navToolbar.bringToFront()
        navToolbar.setToolbarPageName(toolbarPageName)
        navToolbar.setIcon(createNavToolbarIconBuilder())
        navToolbar.setupSearchbar(
                hints = getNavToolbarHint(),
                searchbarClickCallback = ::onSearchBarClick,
                disableDefaultGtmTracker = isDisableSearchBarDefaultGtmTracker,
        )

        configureToolbarBackgroundInteraction()
    }

    private fun updateToolbarNotification(update: Boolean) {
        if(update) navToolbar?.updateNotification()
    }

    private fun updateAdsProductCarousel(data: Pair<Int, TokoNowAdsCarouselUiModel>) {
        val visitables = getViewModel().visitableListLiveData.value.orEmpty()
        val newList = visitables.toMutableList()
        newList[data.first] = data.second
        searchCategoryAdapter?.submitList(newList)
    }

    protected open fun updateProductRecommendation(needToUpdate: Boolean) { /* override to use this function */ }

    protected open val isDisableSearchBarDefaultGtmTracker: Boolean
        get() = false

    protected open fun configureToolbarBackgroundInteraction() {
        val navToolbar = navToolbar ?: return

        activity?.let {
            navToolbar.setupToolbarWithStatusBar(activity = it)
        }
        viewLifecycleOwner.lifecycle.addObserver(navToolbar)

        recyclerView?.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
    }

    private fun createNavRecyclerViewOnScrollListener(
            navToolbar: NavToolbar,
    ): RecyclerView.OnScrollListener {
        val toolbarTransitionRangePixel = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()

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

    protected open fun configureStickyView() {
        val context = context ?: return
        val top = NavToolbarExt.getToolbarHeight(context)
        stickyView?.setMargin(0.toDp(), top, 0.toDp(), 0.toDp())
    }

    protected open val disableDefaultCartTracker
        get() = false

    protected open val disableDefaultShareTracker
        get() = false

    protected open fun onNavToolbarCartClicked() { /* override to use this function */ }

    protected open fun onNavToolbarShareClicked() { /* override to use this function */ }

    protected fun IconBuilder.addGlobalNav(): IconBuilder =
            if (getViewModel().hasGlobalMenu)
                this.addIcon(
                        ID_NAV_GLOBAL,
                        disableRouteManager = false,
                        disableDefaultGtmTracker = false
                ) { }
            else this

    protected fun IconBuilder.addCart(): IconBuilder = this
        .addIcon(
            iconId = ID_CART,
            disableRouteManager = false,
            disableDefaultGtmTracker = disableDefaultCartTracker,
            onClick = ::onNavToolbarCartClicked,
        )

    protected fun IconBuilder.addShare(): IconBuilder = this
        .addIcon(
            iconId = ID_SHARE,
            disableRouteManager = false,
            disableDefaultGtmTracker = disableDefaultShareTracker,
            onClick = ::onNavToolbarShareClicked,
        )

    protected open fun createNavToolbarIconBuilder() = IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.TOKONOW))
        .addCart()
        .addGlobalNav()

    protected open fun getNavToolbarHint(): List<HintData> {
        val hint = getString(R.string.tokopedianow_search_bar_hint)

        return listOf(HintData(hint, hint))
    }

    protected open fun onSearchBarClick(hint: String = "") {
        val autoCompleteApplink = getAutoCompleteApplink()
        val params = getModifiedAutoCompleteQueryParam(autoCompleteApplink)
        val finalApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" +
                UrlParamUtils.generateUrlParamString(params)

        RouteManager.route(context, finalApplink)
    }

    protected open fun getAutoCompleteApplink(): String {
        val viewModelAutoCompleteApplink = getViewModel().autoCompleteApplink

        return if (viewModelAutoCompleteApplink.isEmpty())
            getBaseAutoCompleteApplink()
        else
            viewModelAutoCompleteApplink
    }

    protected open fun getBaseAutoCompleteApplink() =
            ApplinkConstInternalDiscovery.AUTOCOMPLETE

    protected open fun getModifiedAutoCompleteQueryParam(
            autoCompleteApplink: String
    ): Map<String?, String> {
        val urlParser = URLParser(autoCompleteApplink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.BASE_SRP_APPLINK] = ApplinkConstInternalTokopediaNow.SEARCH
        params[SearchApiConst.PLACEHOLDER] = context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty()
        params[SearchApiConst.PREVIOUS_KEYWORD] = getKeyword()

        return params
    }

    protected open fun getKeyword() =
        getViewModel().queryParam[SearchApiConst.Q] ?: ""

    protected open fun createTokoNowEmptyStateOocListener(eventCategory: String): TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
        return object : TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
            override fun onRefreshLayoutPage() {
                getViewModel().onLocalizingAddressSelected()
            }

            override fun onGetFragmentManager(): FragmentManager = parentFragmentManager

            override fun onGetEventCategory(): String = eventCategory

            override fun onSwitchService() {
                getViewModel().switchService()
            }
        }
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayout?.setOnRefreshListener {
            refreshLayout()
        }
    }

    protected open fun refreshLayout() {
        getViewModel().onViewReloadPage()
        refreshProductRecommendation(TOKONOW_NO_RESULT)
    }

    protected open fun refreshProductRecommendation(pageName: String) {
        productRecommendationViewModel.updateProductRecommendation(
            requestParam = getViewModel().createProductRecommendationRequestParam(
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    View.INVISIBLE
                else
                    View.VISIBLE

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

    protected open fun configureRecyclerView() {
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())
        gridLayoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            endlessScrollListener =  createEndlessScrollListener(this)
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
        headerBackground?.translationY = getViewModel().getTranslationYHeaderBackground(
            dy = dy
        )
        if (recyclerView.canScrollVertically(WHILE_SCROLLING_VERTICALLY)) {
            navToolbar?.showShadow(lineShadow = false)
        } else {
            navToolbar?.hideShadow(lineShadow = false)
        }
    }

    abstract fun createTypeFactory(): BaseSearchCategoryTypeFactory

    protected open fun observeViewModel() {
        getViewModel().visitableListLiveData.observe(::submitList)
        getViewModel().hasNextPageLiveData.observe(::updateEndlessScrollListener)
        getViewModel().isFilterPageOpenLiveData.observe(::openBottomSheetFilter)
        getViewModel().dynamicFilterModelLiveData.observe(::onDynamicFilterModelChanged)
        getViewModel().productCountAfterFilterLiveData.observe(::setFilterProductCount)
        getViewModel().isL3FilterPageOpenLiveData.observe(::configureL3BottomSheet)
        getViewModel().shopIdLiveData.observe(::onShopIdUpdated)
        getViewModel().miniCartWidgetLiveData.observe(::updateMiniCartWidget)
        getViewModel().isShowMiniCartLiveData.observe(::updateMiniCartWidgetVisibility)
        getViewModel().updatedVisitableIndicesLiveData.observe(::notifyAdapterItemChange)
        getViewModel().successAddToCartMessageLiveData.observe(::showSuccessAddToCartMessage)
        getViewModel().successRemoveFromCartMessageLiveData.observe(::showSuccessRemoveFromCartMessage)
        getViewModel().errorATCMessageLiveData.observe(::showErrorATCMessage)
        getViewModel().isHeaderBackgroundVisibleLiveData.observe(::updateHeaderBackgroundVisibility)
        getViewModel().isContentLoadingLiveData.observe(::updateContentVisibility)
        getViewModel().quickFilterTrackingLiveData.observe(::sendTrackingQuickFilter)
        getViewModel().addToCartTrackingLiveData.observe(::sendAddToCartTrackingEvent)
        getViewModel().increaseQtyTrackingLiveData.observe(::sendIncreaseQtyTrackingEvent)
        getViewModel().decreaseQtyTrackingLiveData.observe(::sendDecreaseQtyTrackingEvent)
        getViewModel().isShowErrorLiveData.observe(::showNetworkErrorHelper)
        getViewModel().routeApplinkLiveData.observe(::routeApplink)
        getViewModel().deleteCartTrackingLiveData.observe(::sendDeleteCartTrackingEvent)
        getViewModel().generalSearchEventLiveData.observe(::sendTrackingGeneralEvent)
        getViewModel().addToCartRepurchaseWidgetTrackingLiveData.observe(::sendAddToCartRepurchaseProductTrackingEvent)
        getViewModel().oocOpenScreenTrackingEvent.observe(::sendOOCOpenScreenTracking)
        getViewModel().setUserPreferenceLiveData.observe(::setUserPreferenceData)
        getViewModel().querySafeLiveData.observe(::showDialogAgeRestriction)
        getViewModel().updateToolbarNotification.observe(::updateToolbarNotification)
        getViewModel().needToUpdateProductRecommendationLiveData.observe(::updateProductRecommendation)
        getViewModel().updateAdsCarouselLiveData.observe(::updateAdsProductCarousel)

        getViewModel().blockAddToCartLiveData.observe(viewLifecycleOwner) {
            showToasterWhenAddToCartBlocked()
        }

        productRecommendationViewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showSuccessAddToCartMessage(result.data.errorMessage.joinToString(separator = ", "))
                    getViewModel().refreshMiniCart()
                    updateToolbarNotification(true)
                }
                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(ErrorHandler.getErrorMessage(activity, result.throwable))
                    }
                }
            }
        }

        productRecommendationViewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    getViewModel().refreshMiniCart()
                }
                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(ErrorHandler.getErrorMessage(activity, result.throwable))
                    }
                }
            }
        }

        productRecommendationViewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showSuccessRemoveFromCartMessage(result.data.second)
                    getViewModel().refreshMiniCart()
                }
                is Fail -> {
                    activity?.apply {
                        showErrorATCMessage(ErrorHandler.getErrorMessage(activity, result.throwable))
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
    }

    protected open fun onShopIdUpdated(shopId: String) {
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

    abstract val miniCartWidgetPageName: MiniCartAnalytics.Page

    abstract val miniCartWidgetSource: MiniCartSource

    abstract fun getViewModel(): BaseSearchCategoryViewModel

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    protected open fun submitList(visitableList: List<Visitable<*>>) {
        if (visitableList.isNotEmpty()) showContent()
        searchCategoryAdapter?.submitList(visitableList)
    }

    private fun showContent() {
        loaderUnify?.gone()
        stickyView?.visible()
    }

    private fun hideContent() {
        loaderUnify?.show()
        stickyView?.gone()
    }

    protected open fun updateEndlessScrollListener(hasNextPage: Boolean) {
        endlessScrollListener?.updateStateAfterGetData()
        endlessScrollListener?.setHasNextPage(hasNextPage)
    }

    protected open fun onLoadMore() {
        getViewModel().onLoadMore()
    }

    protected fun getUserId(): String {
        val userId = userSession.userId ?: ""

        return if (userId.isEmpty()) "0" else userId
    }

    override fun onLocalizingAddressSelected() {
        getViewModel().onLocalizingAddressSelected()
    }

    override fun getFragment() = this

    override fun onSeeAllCategoryClicked() {
        RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
                getViewModel().warehouseId,
        )
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String, param: String) {
        context?.let { context ->
            switchService(
                context = context,
                param = param,
                onRefreshPage = {
                    getViewModel().switchService()
                },
                onRedirectPage = {
                    RouteManager.route(it, applink)
                }
            )
        }
    }

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) { /* override to use this function */ }

    override fun openFilterPage() {
        getViewModel().onViewOpenFilterPage()
    }

    private fun openBottomSheetFilter(isFilterPageOpen: Boolean) {
        if (!isFilterPageOpen) return

        val mapParameter = getViewModel().queryParam
        val dynamicFilterModel = getViewModel().dynamicFilterModelLiveData.value

        sortFilterBottomSheet = SortFilterBottomSheet().also {
            it.show(
                    parentFragmentManager,
                    mapParameter,
                    dynamicFilterModel,
                    this
            )

            it.setOnDismissListener {
                sortFilterBottomSheet = null
                getViewModel().onViewDismissFilterPage()
            }
        }
    }

    override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        getViewModel().onViewApplySortFilter(applySortFilterModel)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        getViewModel().onViewGetProductCount(mapParameter)
    }

    private fun onDynamicFilterModelChanged(dynamicFilterModel: DynamicFilterModel?) {
        dynamicFilterModel ?: return

        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
    }

    protected open fun setFilterProductCount(countText: String) {
        val productCountText = String.format(
                getString(R.string.tokopedianow_apply_filter_text),
                countText
        )

        sortFilterBottomSheet?.setResultCountText(productCountText)
        categoryChooserBottomSheet?.setResultCountText(productCountText)
    }

    protected open fun configureL3BottomSheet(filter: Filter?) {
        if (filter != null)
            openCategoryChooserFilterPage(filter)
        else
            dismissCategoryChooserFilterPage()
    }

    protected open fun openCategoryChooserFilterPage(filter: Filter) {
        if (categoryChooserBottomSheet != null) return

        categoryChooserBottomSheet = CategoryChooserBottomSheet()
        categoryChooserBottomSheet?.setOnDismissListener {
            getViewModel().onViewDismissL3FilterPage()
        }
        categoryChooserBottomSheet?.show(
                parentFragmentManager,
                getViewModel().queryParam,
                filter,
                this,
        )
    }

    protected open fun dismissCategoryChooserFilterPage() {
        categoryChooserBottomSheet?.dismiss()
        categoryChooserBottomSheet = null
    }

    override fun getResultCount(selectedOption: Option) {
        getViewModel().onViewGetProductCount(selectedOption)
    }

    override fun onApplyCategory(selectedOption: Option) {
        getViewModel().onViewApplyFilterFromCategoryChooser(selectedOption)
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        getViewModel().onViewClickCategoryFilterChip(option, isSelected)
    }

    private fun updateMiniCartWidget(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartSimplifiedData ?: return

        miniCartWidget?.updateData(miniCartSimplifiedData)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getViewModel().updateToolbarNotification()
        getViewModel().onViewUpdateCartItems(miniCartSimplifiedData)
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
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            }
            stickyView?.setPadding(NO_PADDING, NO_PADDING, NO_PADDING, paddingBottom)
        }
    }

    private fun getMiniCartHeight(): Int {
        return miniCartWidget?.height.orZero() - context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)?.toInt().orZero()
    }

    private fun notifyAdapterItemChange(indices: List<Int>) {
        val searchCategoryAdapter = searchCategoryAdapter ?: return

        indices.forEach {
            if (it in searchCategoryAdapter.list.indices)
                searchCategoryAdapter.notifyItemChanged(it, true)
        }
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        val uri = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productItemDataView.productCardModel.productId
        )
        val appLink = getViewModel().createAffiliateLink(uri)
        RouteManager.route(context, appLink)
    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        val productId = productItemDataView.productCardModel.productId
        val shopId = productItemDataView.shop.id

        openATCVariantBottomSheet(productId, shopId)
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
                startActivitResult = this::startActivityForResult,
        )
    }

    protected abstract fun getCDListName(): String

    override fun onProductNonVariantQuantityChanged(
            productItemDataView: ProductItemDataView,
            quantity: Int,
    ) {
        getViewModel().onViewATCProductNonVariant(productItemDataView, quantity)
    }

    protected open fun showSuccessAddToCartMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_toaster_see)) {
            miniCartWidget?.showMiniCartListBottomSheet(this)
        }
    }

    protected open fun showSuccessRemoveFromCartMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_toaster_ok))
    }

    protected open fun showToaster(
            message: String?,
            toasterType: Int,
            actionText: String = "",
            clickListener: (View) -> Unit = { },
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
                clickListener,
        ).show()
    }

    protected open fun showErrorATCMessage(message: String?) {
        showToaster(message, Toaster.TYPE_ERROR)
    }

    protected open fun updateHeaderBackgroundVisibility(isVisible: Boolean) {
        if (!isVisible) {
            headerBackground?.setImageResource(R.color.tokopedianow_dms_transparent)
        } else {
            context?.resources?.apply {
                val background = VectorDrawableCompat.create(this, R.drawable.tokopedianow_ic_header_background, context?.theme)
                headerBackground?.setImageDrawable(background)
            }
        }
        headerBackground?.showWithCondition(isVisible)
    }

    protected open fun updateContentVisibility(isLoadingVisible: Boolean) {
        swipeRefreshLayout?.isRefreshing = isLoadingVisible

        if (isLoadingVisible) return
        showOnBoardingBottomSheet()
    }

    protected abstract fun sendIncreaseQtyTrackingEvent(productId: String)

    protected abstract fun sendDecreaseQtyTrackingEvent(productId: String)

    protected open fun showNetworkErrorHelper(throwable: Throwable?) {
        val context = activity ?: return
        val view = view ?: return

        NetworkErrorHelper.showEmptyState(context, view, ErrorHandler.getErrorMessage(context, throwable)) {
            getViewModel().onViewReloadPage()
        }
    }

    protected abstract fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>)

    protected abstract fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>)

    protected open fun routeApplink(applink: String?) {
        applink ?: return

        RouteManager.route(context, applink)
    }

    protected abstract fun sendDeleteCartTrackingEvent(productId: String)

    override fun onPause() {
        super.onPause()

        trackingQueue?.sendAll()
    }

    override fun onResume() {
        super.onResume()

        getViewModel().onViewResumed()
    }

    override fun onFindInTokopediaClick() {
        routeApplink(ApplinkConst.HOME)
    }

    override fun goToTokopediaNowHome() {
        routeApplink(ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onRemoveFilterClick(option: Option) {
        getViewModel().onViewRemoveFilter(option)
    }

    private fun sendAddToCartRecommendationTrackingEvent(
        addToCartDataTrackerModel: AddToCartDataTrackerModel
    ) {
        val product = addToCartDataTrackerModel.productRecommendation
        val recommendationItem = mapProductItemToRecommendationItem(product)
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

    abstract fun getListValue(isOOC: Boolean, recommendationItem: RecommendationItem): String
    abstract fun getImpressionEventAction(isOOC: Boolean): String
    abstract fun getClickEventAction(isOOC: Boolean): String
    abstract fun getAtcEventAction(): String
    abstract fun getEventCategory(isOOC: Boolean): String
    abstract fun getEventLabel(): String

    private fun sendTrackingGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    override fun onCartQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int) {
        getViewModel().onViewATCRepurchaseWidget(data, quantity)
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel) {

    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {

    }

    override fun createAffiliateLink(url: String): String {
        return getViewModel().createAffiliateLink(url)
    }

    override fun onAddVariantClicked(data: TokoNowProductCardUiModel) {
        openATCVariantBottomSheet(
            data.productId,
            data.shopId,
        )
    }

    protected open fun sendAddToCartRepurchaseProductTrackingEvent(
        addToCartRepurchaseProductData: Triple<Int, String, TokoNowProductCardUiModel>
    ) {

    }

    override fun onClickSwitcherTo15M() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onClickSwitcherTo2H() {
        hideContent()
        getViewModel().setUserPreference(NOW_2H)
    }

    private fun setUserPreferenceData(result: Result<SetUserPreference.SetUserPreferenceData>) {
        showContent()
        when(result) {
            is Success -> {
                context?.apply {
                    //Set user preference data to local cache
                    updateLocalCacheModel(
                        data = result.data,
                        context = this
                    )

                    //Refresh the page
                    gridLayoutManager?.scrollToPosition(TOP_LIST)
                    refreshLayout()

                    //Show toaster
                    showOnBoardingToaster(
                        data = result.data
                    )

                    //Refresh mini cart
                    getViewModel().refreshMiniCart()
                }
            }
            is Fail -> { /* do nothing */ }
        }
    }

    protected abstract fun showDialogAgeRestriction(querySafeModel: QuerySafeModel)

    private fun updateLocalCacheModel(data: SetUserPreference.SetUserPreferenceData, context: Context) {
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

        val needToShowOnBoardBottomSheet = getViewModel().needToShowOnBoardBottomSheet(sharedPref.get20mBottomSheetOnBoardShown())
        if (!data.warehouseId.toLongOrZero().isZero() && !needToShowOnBoardBottomSheet) {
            showSwitcherToaster(data.serviceType)
        }
    }

    private fun showOnBoardingBottomSheet() {
        val needToShowOnBoardBottomSheet = getViewModel().needToShowOnBoardBottomSheet(sharedPref.get20mBottomSheetOnBoardShown())
        if (needToShowOnBoardBottomSheet) {
            show20mOnBoardBottomSheet()
        }
    }

    private fun show20mOnBoardBottomSheet() {
        TokoNowOnBoard20mBottomSheet
            .newInstance()
            .show(childFragmentManager, OnBoard20mBottomSheetCallback(
                onBackTo2hClicked = {
                    RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME + QUERY_PARAM_SERVICE_TYPE_NOW2H)
                },
                onDismiss = {
                    sharedPref.set20mBottomSheetOnBoardShown(true)
                }
            ))
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

    protected abstract fun sendOOCOpenScreenTracking(isTracked: Boolean)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AdultManager.handleActivityResult(activity, requestCode, resultCode, data, object : AdultManager.Callback {
            override fun onFail() {
                activity?.finish()
            }

            override fun onVerificationSuccess(message: String?) {}

            override fun onLoginPreverified() {}
        })
    }

    override fun onFeedbackCtaClicked() {
        TokoNowProductFeedbackBottomSheet().also {
            it.showBottomSheet(activity?.supportFragmentManager,view)
        }
    }

    protected open fun createProductRecommendationOocCallback() = ProductRecommendationOocCallback(
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
        },
    )

    protected open fun createProductRecommendationCallback() = ProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        baseSearchCategoryViewModel = getViewModel(),
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
        },
    )

    protected open fun createSimilarProductCallback(isCategoryPage: Boolean) : ProductCardCompactSimilarProductTrackerCallback {
        return ProductCardCompactSimilarProductTrackerCallback(isCategoryPage)
    }

    protected open fun createProductCardCompactCallback(): ProductCardCompactCallback = ProductCardCompactCallback { productId, similarProductTrackerListener ->
        context?.apply {
            val intent = TokoNowSimilarProductBottomSheetActivity.createNewIntent(this, productId, similarProductTrackerListener)
            startActivity(intent)
        }
    }

    protected fun createProductAdsCarouselCallback(analytics: ProductAdsCarouselAnalytics): ProductAdsCarouselListener {
        return ProductAdsCarouselListener(
            context = context,
            viewModel = getViewModel(),
            analytics = analytics,
            startActivityResult = ::startActivityForResult,
            showToasterWhenAddToCartBlocked = ::showToasterWhenAddToCartBlocked
        )
    }

    override fun onProductCardAddToCartBlocked() = showToasterWhenAddToCartBlocked()

    protected fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            toasterType = Toaster.TYPE_ERROR
        )
    }
}
