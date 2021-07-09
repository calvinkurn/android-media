package com.tokopedia.tokopedianow.searchcategory.presentation.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.ChooseAddressBottomSheetListener
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.utils.ErrorHandler.getErrorMessage
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.CategoryChooserBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokopedianow.searchcategory.presentation.itemdecoration.ProductItemDecoration
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.EmptyProductListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OutOfCoverageListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.CustomStaggeredGridLayoutManager
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

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
        EmptyProductListener,
        ChooseAddressBottomSheetListener,
        OutOfCoverageListener {

    companion object {
        protected const val OUT_OF_COVERAGE_CHOOSE_ADDRESS = "OUT_OF_COVERAGE_CHOOSE_ADDRESS"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    protected var recyclerViewLayoutManager: CustomStaggeredGridLayoutManager? = null
    protected var searchCategoryAdapter: SearchCategoryAdapter? = null
    protected var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    protected var sortFilterBottomSheet: SortFilterBottomSheet? = null
    protected var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null
    protected var trackingQueue: TrackingQueue? = null

    protected var container: ConstraintLayout? = null
    protected var navToolbar: NavToolbar? = null
    protected var recyclerView: RecyclerView? = null
    protected var miniCartWidget: MiniCartWidget? = null
    protected var stickyView: StickySingleHeaderView? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected var statusBarBackground: View? = null
    protected var headerBackground: AppCompatImageView? = null
    protected var loaderUnify: LoaderUnify? = null

    private var movingPosition = 0

    protected abstract val toolbarPageName: String

    private val searchCategoryToolbarHeight: Int
        get() {
            val defaultHeight = resources
                    .getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_status_height)

            val height = (navToolbar?.height ?: defaultHeight)
            val padding = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

            return height + padding
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let { trackingQueue = TrackingQueue(it) }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(
                R.layout.fragment_tokopedianow_search_category,
                container,
                false
        )
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
        navToolbar = view.findViewById(R.id.tokonowSearchCategoryNavToolbar)
        recyclerView = view.findViewById(R.id.tokonowSearchCategoryRecyclerView)
        miniCartWidget = view.findViewById(R.id.tokonowSearchCategoryMiniCart)
        stickyView = view.findViewById(R.id.tokonowSearchCategoryStickyView)
        swipeRefreshLayout = view.findViewById(R.id.tokonowSearchCategorySwipeRefreshLayout)
        statusBarBackground = view.findViewById(R.id.tokonowSearchCategoryStatusBarBackground)
        headerBackground = view.findViewById(R.id.tokonowSearchCategoryBackgroundImage)
        loaderUnify = view.findViewById(R.id.tokonowSearchCategoryLoader)
        container = view.findViewById(R.id.tokonowSearchCategoryContainer)
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
            navToolbar.setupToolbarWithStatusBar(it)
        }

        viewLifecycleOwner.lifecycle.addObserver(navToolbar)

        recyclerView?.addOnScrollListener(createNavRecyclerViewOnScrollListener(navToolbar))
    }

    private fun createNavRecyclerViewOnScrollListener(
            navToolbar: NavToolbar,
    ): NavRecyclerViewScrollListener {
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
        val top = NavToolbarExt.getFullToolbarHeight(context)
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

    protected open fun getNavToolbarHint() =
            listOf(HintData("", ""))

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

        return params
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayout?.setOnRefreshListener {
            getViewModel().onViewReloadPage()
        }
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
        recyclerViewLayoutManager = CustomStaggeredGridLayoutManager()
        recyclerViewLayoutManager?.gapStrategy = GAP_HANDLING_NONE

        endlessScrollListener = recyclerViewLayoutManager?.let{ createEndlessScrollListener(it) }
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())

        recyclerView?.adapter = searchCategoryAdapter
        recyclerView?.layoutManager = recyclerViewLayoutManager
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
        headerBackground?.y = -(movingPosition.toFloat())
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
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
        getViewModel().isRecyclerViewScrollEnabledLiveData.observe(this::updateRecyclerViewScrollable)
        getViewModel().quickFilterTrackingLiveData.observe(this::sendTrackingQuickFilter)
        getViewModel().addToCartTrackingLiveData.observe(this::sendAddToCartTrackingEvent)
        getViewModel().increaseQtyTrackingLiveData.observe(this::sendIncreaseQtyTrackingEvent)
        getViewModel().decreaseQtyTrackingLiveData.observe(this::sendDecreaseQtyTrackingEvent)
        getViewModel().isShowErrorLiveData.observe(this::showNetworkErrorHelper)
        getViewModel().routeApplinkLiveData.observe(this::routeApplink)
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
        searchCategoryAdapter?.submitList(visitableList)
        if (visitableList.isNotEmpty()) showContent()
    }

    private fun showContent() {
        loaderUnify?.gone()
        stickyView?.visible()
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

    override fun onBannerClick(channelModel: ChannelModel, applink: String) {
        RouteManager.route(context, applink)
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

    override fun onApplyCategory(selectedOption: Option) {
        getViewModel().onViewApplyFilterFromCategoryChooser(selectedOption)
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        getViewModel().onViewClickCategoryFilterChip(option, isSelected)
    }

    private fun updateMiniCartWidget(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartSimplifiedData ?: return

        miniCartWidget?.updateData(miniCartSimplifiedData)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getViewModel().onViewUpdateCartItems(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetVisibility(isVisible: Boolean?) {
        miniCartWidget?.showWithCondition(isVisible == true)
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
        val context = context ?: return

        AtcVariantHelper.goToAtcVariant(
                context = context,
                productId = productItemDataView.id,
                pageSource = "tokonow",
                isTokoNow = true,
                shopId = productItemDataView.shop.id,
                startActivitResult = this::startActivityForResult,
        )
    }

    override fun onProductNonVariantQuantityChanged(
            productItemDataView: ProductItemDataView,
            quantity: Int,
    ) {
        getViewModel().onViewATCProductNonVariant(productItemDataView, quantity)
    }

    protected open fun showSuccessATCMessage(message: String?) {
        showToaster(message, Toaster.TYPE_NORMAL, getString(R.string.tokopedianow_lihat)) {
            RouteManager.route(context, ApplinkConst.CART)
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

    protected open fun updateRecyclerViewScrollable(isScrollable: Boolean) {
        recyclerViewLayoutManager?.setScrollEnabled(isScrollable)
    }

    protected abstract fun sendIncreaseQtyTrackingEvent(productId: String)

    protected abstract fun sendDecreaseQtyTrackingEvent(productId: String)

    protected open fun showNetworkErrorHelper(throwable: Throwable?) {
        val context = activity ?: return
        val view = view ?: return

        NetworkErrorHelper.showEmptyState(context, view, getErrorMessage(context, throwable)) {
            getViewModel().onViewReloadPage()
        }
    }

    protected abstract fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>)

    protected abstract fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>)

    protected open fun routeApplink(applink: String?) {
        applink ?: return

        RouteManager.route(context, applink)
    }

    override fun onPause() {
        super.onPause()

        trackingQueue?.sendAll()
    }

    override fun onResume() {
        super.onResume()

        getViewModel().onViewResumed()
    }

    override fun onGoToGlobalSearch() {

    }

    override fun onChangeKeywordButtonClick() {
        onSearchBarClick()
    }

    override fun onRemoveFilterClick(option: Option) {
        getViewModel().onViewRemoveFilter(option)
    }

    override fun onChangeAddressClicked() {
        val parentFragmentManager = parentFragmentManager

        ChooseAddressBottomSheet().also {
            it.setListener(this)
            it.show(parentFragmentManager, OUT_OF_COVERAGE_CHOOSE_ADDRESS)
        }
    }

    override fun onReturnClick() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun getLocalizingAddressHostSourceBottomSheet() =
            SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

    override fun onAddressDataChanged() {
        getViewModel().onLocalizingAddressSelected()
    }

    override fun onLocalizingAddressServerDown() { }

    override fun onLocalizingAddressLoginSuccessBottomSheet() { }

    override fun onDismissChooseAddressBottomSheet() { }
}