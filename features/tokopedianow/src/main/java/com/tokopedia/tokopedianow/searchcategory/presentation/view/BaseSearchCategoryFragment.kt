package com.tokopedia.tokopedianow.searchcategory.presentation.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.viewutil.initRecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_2H
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowSwitcherUtil.switchService
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecommendationCarouselViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowSearchCategoryBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.CategoryChooserBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokopedianow.searchcategory.presentation.itemdecoration.ProductItemDecoration
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
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
    TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    TokoNowProductCardListener,
    TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
    TokoNowRecommendationCarouselViewHolder.TokonowRecomBindPageNameListener {

    companion object {
        protected const val DEFAULT_SPAN_COUNT = 2
        protected const val REQUEST_CODE_LOGIN = 69
        private const val DEFAULT_POSITION = 0
    }

    private var binding by autoClearedNullable<FragmentTokopedianowSearchCategoryBinding>()

    @Inject
    lateinit var userSession: UserSessionInterface

    protected var searchCategoryAdapter: SearchCategoryAdapter? = null
    protected var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    protected var sortFilterBottomSheet: SortFilterBottomSheet? = null
    protected var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null
    protected var trackingQueue: TrackingQueue? = null
    protected var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    protected var container: ConstraintLayout? = null
    protected var navToolbar: NavToolbar? = null
    protected var recyclerView: RecyclerView? = null
    protected var miniCartWidget: MiniCartWidget? = null
    protected var stickyView: StickySingleHeaderView? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected var statusBarBackground: View? = null
    protected var headerBackground: AppCompatImageView? = null
    protected var loaderUnify: LoaderUnify? = null
    protected val carouselScrollPosition = SparseIntArray()
    protected val recycledViewPool = RecyclerView.RecycledViewPool()

    private var movingPosition = 0

    protected abstract val toolbarPageName: String

    private val recomWidgetViewModel: RecomWidgetViewModel? by initRecomWidgetViewModel()

    private val searchCategoryToolbarHeight: Int
        get() {
            val defaultHeight = resources
                .getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_status_height)

            val height = (navToolbar?.height ?: defaultHeight)
            val padding =
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

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

        getViewModel().onViewCreated()
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
        val toolbarTransitionRangePixel =
                resources.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range)

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

    protected open fun createNavToolbarIconBuilder() = IconBuilder()
            .addCart()
            .addGlobalNav()

    protected fun IconBuilder.addCart(): IconBuilder = this
            .addIcon(
                    iconId = ID_CART,
                    disableRouteManager = false,
                    disableDefaultGtmTracker = disableDefaultCartTracker,
                    onClick = ::onNavToolbarCartClicked,
            )

    protected open val disableDefaultCartTracker
        get() = false

    protected open fun onNavToolbarCartClicked() {

    }

    protected fun IconBuilder.addGlobalNav(): IconBuilder =
            if (getViewModel().hasGlobalMenu)
                this.addIcon(
                        ID_NAV_GLOBAL,
                        disableRouteManager = false,
                        disableDefaultGtmTracker = false
                ) { }
            else this

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
        params[SearchApiConst.HINT] = resources.getString(R.string.tokopedianow_search_bar_hint)
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
                getViewModel().refreshMiniCart()
                getViewModel().switchService()
            }
        }
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayout?.setOnRefreshListener {
            refreshLayout()
        }
    }

    private fun refreshLayout() {
        resetMovingPosition()
        carouselScrollPosition.clear()
        getViewModel().onViewReloadPage()
    }

    private fun resetMovingPosition() {
        movingPosition = DEFAULT_POSITION
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
        staggeredGridLayoutManager = StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager?.gapStrategy = GAP_HANDLING_NONE

        staggeredGridLayoutManager?.let {
            endlessScrollListener =  createEndlessScrollListener(it)
        }
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())

        recyclerView?.adapter = searchCategoryAdapter
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.addProductItemDecoration()

        endlessScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
        recyclerView?.addOnScrollListener(createNavToolbarShadowOnScrollListener())
    }

    private fun createEndlessScrollListener(layoutManager: StaggeredGridLayoutManager) =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore()
                }
            }

    private fun createNavToolbarShadowOnScrollListener() =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    evaluateNavToolbarShadow(recyclerView, dy)
                }
            }

    private fun evaluateNavToolbarShadow(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        headerBackground?.y = if(movingPosition >= DEFAULT_POSITION) {
            -(movingPosition.toFloat())
        } else {
            resetMovingPosition()
            movingPosition.toFloat()
        }
        if (recyclerView.canScrollVertically(1) || movingPosition != DEFAULT_POSITION) {
            navToolbar?.showShadow(lineShadow = false)
        } else {
            navToolbar?.hideShadow(lineShadow = false)
        }
    }

    abstract fun createTypeFactory(): BaseSearchCategoryTypeFactory

    protected open fun RecyclerView.addProductItemDecoration() {
        try {
            val context = context ?: return
            val unifySpace16 = com.tokopedia.unifyprinciples.R.dimen.unify_space_16
            val spacing = context.getDimensionPixelSize(unifySpace16)

            if (itemDecorationCount >= 1)
                invalidateItemDecorations()

            addItemDecoration(ProductItemDecoration(spacing))
        } catch (throwable: Throwable) {

        }
    }

    private fun Context.getDimensionPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

    protected open fun observeViewModel() {
        getViewModel().visitableListLiveData.observe(this::submitList)
        getViewModel().hasNextPageLiveData.observe(this::updateEndlessScrollListener)
        getViewModel().isFilterPageOpenLiveData.observe(this::openBottomSheetFilter)
        getViewModel().dynamicFilterModelLiveData.observe(this::onDynamicFilterModelChanged)
        getViewModel().productCountAfterFilterLiveData.observe(this::setFilterProductCount)
        getViewModel().isL3FilterPageOpenLiveData.observe(this::configureL3BottomSheet)
        getViewModel().shopIdLiveData.observe(this::onShopIdUpdated)
        getViewModel().miniCartWidgetLiveData.observe(this::updateMiniCartWidget)
        getViewModel().isShowMiniCartLiveData.observe(this::updateMiniCartWidgetVisibility)
        getViewModel().updatedVisitableIndicesLiveData.observe(this::notifyAdapterItemChange)
        getViewModel().successATCMessageLiveData.observe(this::showSuccessATCMessage)
        getViewModel().errorATCMessageLiveData.observe(this::showErrorATCMessage)
        getViewModel().isHeaderBackgroundVisibleLiveData.observe(this::updateHeaderBackgroundVisibility)
        getViewModel().isContentLoadingLiveData.observe(this::updateContentVisibility)
        getViewModel().quickFilterTrackingLiveData.observe(this::sendTrackingQuickFilter)
        getViewModel().addToCartTrackingLiveData.observe(this::sendAddToCartTrackingEvent)
        getViewModel().increaseQtyTrackingLiveData.observe(this::sendIncreaseQtyTrackingEvent)
        getViewModel().decreaseQtyTrackingLiveData.observe(this::sendDecreaseQtyTrackingEvent)
        getViewModel().isShowErrorLiveData.observe(this::showNetworkErrorHelper)
        getViewModel().routeApplinkLiveData.observe(this::routeApplink)
        getViewModel().deleteCartTrackingLiveData.observe(this::sendDeleteCartTrackingEvent)
        getViewModel().addToCartRecommendationItemTrackingLiveData.observe(
                this::sendAddToCartRecommendationTrackingEvent
        )
        getViewModel().generalSearchEventLiveData.observe(this::sendTrackingGeneralEvent)
        getViewModel().addToCartRepurchaseWidgetTrackingLiveData.observe(
            ::sendAddToCartRepurchaseProductTrackingEvent
        )
        getViewModel().oocOpenScreenTrackingEvent.observe(::sendOOCOpenScreenTracking)
        getViewModel().setUserPreferenceLiveData.observe(::setUserPreferenceData)
    }

    protected open fun onShopIdUpdated(shopId: String) {
        if (shopId.isEmpty()) return

        miniCartWidget?.initialize(
                shopIds = listOf(shopId),
                fragment = this,
                listener = this,
                autoInitializeData = false,
                pageName = miniCartWidgetPageName,
        )
    }

    abstract val miniCartWidgetPageName: MiniCartAnalytics.Page

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

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) {

    }

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
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
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
        recomWidgetViewModel?.updateMiniCartWithPageData(miniCartSimplifiedData)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getViewModel().onViewUpdateCartItems(miniCartSimplifiedData)
        recomWidgetViewModel?.updateMiniCartWithPageData(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetVisibility(isVisible: Boolean?) {
        miniCartWidget?.showWithCondition(isVisible == true)
        if (!isVisible()) miniCartWidget?.hideCoachMark()
    }

    private fun notifyAdapterItemChange(indices: List<Int>) {
        val searchCategoryAdapter = searchCategoryAdapter ?: return

        indices.forEach {
            if (it in searchCategoryAdapter.list.indices)
                searchCategoryAdapter.notifyItemChanged(it)
        }
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productItemDataView.id,
        )
    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        val productId = productItemDataView.id
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

    protected open fun showSuccessATCMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_lihat)) {
            miniCartWidget?.showMiniCartListBottomSheet(this)
        }
    }

    protected open fun showToaster(
            message: String?,
            toasterType: Int,
            actionText: String = "",
            clickListener: (View) -> Unit = { },
    ) {
        val view = view ?: return
        val context = context ?: return
        message ?: return
        if (message.isEmpty()) return

        val defaultHeight = context.resources.getDimensionPixelSize(R.dimen.tokopedianow_searchcategory_minicart_widget_height)
        val miniCartWidget = miniCartWidget
        Toaster.toasterCustomBottomHeight = miniCartWidget?.height ?: defaultHeight

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
        if (!isVisible) headerBackground?.setImageResource(R.color.tokopedianow_dms_transparent)
        else headerBackground?.setImageResource(R.drawable.tokopedianow_ic_header_background)
        headerBackground?.showWithCondition(isVisible)
    }

    protected open fun updateContentVisibility(isLoadingVisible: Boolean) {
        swipeRefreshLayout?.isRefreshing = isLoadingVisible
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

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) {
        carouselScrollPosition.put(adapterPosition, scrollPosition)
    }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int {
        return carouselScrollPosition.get(adapterPosition)
    }

    override fun onBindRecommendationCarousel(
            element: TokoNowRecommendationCarouselUiModel,
            adapterPosition: Int,
    ) {
        getViewModel().onBindRecommendationCarousel(element, adapterPosition)
    }

    override fun onImpressedRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
    ) {
        val isOOC = recommendationCarouselDataView?.isOutOfCoverage()?:false
        trackingQueue?.putEETracking(
            ProductRecommendationTracking.getImpressionProductTracking(
                recommendationItem = recomItem,
                eventCategory = getEventCategory(isOOC),
                headerTitle = data.recommendationData.title,
                position = itemPosition,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = getEventLabel(isOOC),
                eventAction = getImpressionEventAction(isOOC),
                listValue = getListValue(isOOC, recomItem),
            )
        )
    }

    override fun onClickRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
    ) {
        val isOOC = recommendationCarouselDataView?.isOutOfCoverage()?:false
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ProductRecommendationTracking.getClickProductTracking(
                recommendationItem = recomItem,
                eventCategory = getEventCategory(isOOC),
                headerTitle = data.recommendationData.title,
                position = itemPosition,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = getEventLabel(isOOC),
                eventAction = getClickEventAction(isOOC),
                listValue = getListValue(isOOC, recomItem),
            )
        )
        RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                recomItem.productId.toString(),
        )
    }

    override fun onATCNonVariantRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
    ) {
        getViewModel().onViewATCRecommendationItemNonVariant(
                recommendationItem = recomItem,
                adapterPosition = recommendationCarouselPosition,
                quantity = quantity,
        )
    }

    override fun onAddVariantRecommendationCarouselItem(
        recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
    ) {
        val productId = recomItem.productId.toString()
        val shopId = recomItem.shopId.toString()

        openATCVariantBottomSheet(productId, shopId)
    }

    protected open fun sendAddToCartRecommendationTrackingEvent(
        atcTrackingData: Triple<Int, String, RecommendationItem>
    ) {
        val (quantity, cartId, recommendationItem) = atcTrackingData
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ProductRecommendationTracking.getAddToCartClickProductTracking(
                recommendationItem = recommendationItem,
                position = recommendationItem.position,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = getEventLabel(false),
                headerTitle = "",
                quantity = quantity,
                cartId = cartId,
                eventAction = getAtcEventAction(false),
                eventCategory = getEventCategory(false),
                listValue = getListValue(false, recommendationItem),
            )
        )
    }

    override fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getViewModel().refreshMiniCart()
    }

    override fun onRecomTokonowAtcSuccess(message: String) {
        showSuccessATCMessage(message)
    }

    override fun onRecomTokonowAtcFailed(throwable: Throwable) {
        context?.let {
            showErrorATCMessage(ErrorHandler.getErrorMessage(it, throwable))
        }
    }

    override fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ProductRecommendationTracking.getAddToCartClickProductTracking(
                recommendationItem = recommendationItem,
                position = recommendationItem.position,
                isLoggedIn = userSession.isLoggedIn,
                userId = userSession.userId,
                eventLabel = getEventLabel(false),
                headerTitle = "",
                quantity = recommendationItem.quantity,
                cartId = recommendationItem.cartId,
                eventAction = getAtcEventAction(false),
                eventCategory = getEventCategory(false),
                listValue = getListValue(false, recommendationItem),
            )
        )
    }

    override fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {
    }

    override fun onClickItemNonLoginState() {
        goToLogin()
    }

    abstract fun getListValue(isOOC: Boolean, recommendationItem: RecommendationItem): String
    abstract fun getImpressionEventAction(isOOC: Boolean): String
    abstract fun getClickEventAction(isOOC: Boolean): String
    abstract fun getAtcEventAction(isOOC: Boolean): String
    abstract fun getEventCategory(isOOC: Boolean): String
    abstract fun getEventLabel(isOOC: Boolean): String

    private fun sendTrackingGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    override fun onProductQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int) {
        getViewModel().onViewATCRepurchaseWidget(data, quantity)
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel) {

    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {

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
        hideContent()
        getViewModel().setUserPreference(NOW_15M)
    }

    override fun onClickSwitcherTo2H() {
        hideContent()
        getViewModel().setUserPreference(NOW_2H)
    }

    private fun setUserPreferenceData(result: Result<SetUserPreference.SetUserPreferenceData>) {
        showContent()
        when(result) {
            is Success -> {
                swipeRefreshLayout
                context?.apply {
                    //Set user preference data to local cache
                    ChooseAddressUtils.updateTokoNowData(
                        context = this,
                        warehouseId = result.data.warehouseId,
                        shopId = result.data.shopId,
                        serviceType = result.data.serviceType,
                        warehouses = result.data.warehouses.map {
                            LocalWarehouseModel(
                                it.warehouseId.toLongOrZero(),
                                it.serviceType
                            )
                        }
                    )

                    //Refresh the page
                    staggeredGridLayoutManager?.scrollToPosition(DEFAULT_POSITION)
                    refreshLayout()
                }
            }
            is Fail -> { /* no op */ }
        }
    }

    protected open fun goToLogin() {
        activity?.let {
            startActivityForResult(
                RouteManager.getIntent(it, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
        }
    }

    override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
        viewLifecycleOwner.lifecycle.addObserver(observer)
    }

    protected abstract fun sendOOCOpenScreenTracking(isTracked: Boolean)
}