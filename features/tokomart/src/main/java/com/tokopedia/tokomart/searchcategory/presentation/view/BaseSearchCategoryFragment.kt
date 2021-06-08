package com.tokopedia.tokomart.searchcategory.presentation.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_SHARE
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.viewholder.HomeCategoryGridViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokomart.searchcategory.presentation.customview.CategoryChooserBottomSheet
import com.tokopedia.tokomart.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokomart.searchcategory.presentation.itemdecoration.ProductItemDecoration
import com.tokopedia.tokomart.searchcategory.presentation.listener.*
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.Toaster

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
        ProductItemListener {

    companion object {
        protected const val DEFAULT_SPAN_COUNT = 2
    }

    protected var searchCategoryAdapter: SearchCategoryAdapter? = null
    protected var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    protected var sortFilterBottomSheet: SortFilterBottomSheet? = null
    protected var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null

    protected var navToolbar: NavToolbar? = null
    protected var recyclerView: RecyclerView? = null
    protected var miniCartWidget: MiniCartWidget? = null
    protected var stickyView: StickySingleHeaderView? = null
    private var statusBarBackground: View? = null
    private var headerBackground: AppCompatImageView? = null
    private var movingPosition = 0

    protected abstract val toolbarPageName: String

    private val searchCategoryToolbarHeight: Int
        get() {
            var height = navToolbar?.height ?: resources.getDimensionPixelSize(R.dimen.tokomart_default_toolbar_status_height)
            height += resources.getDimensionPixelSize(R.dimen.dp_8)
            return height
        }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_tokomart_search_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)

        configureNavToolbar()
        configureStatusBar()
        configureMiniCart()
        configureRecyclerView()
        observeViewModel()

        getViewModel().onViewCreated()
    }

    protected open fun findViews(view: View) {
        navToolbar = view.findViewById(R.id.tokonowSearchCategoryNavToolbar)
        recyclerView = view.findViewById(R.id.tokonowSearchCategoryRecyclerView)
        miniCartWidget = view.findViewById(R.id.tokonowSearchCategoryMiniCart)
        stickyView = view.findViewById(R.id.tokonowSearchCategoryStickyView)
        statusBarBackground = view.findViewById(R.id.tokonowSearchCategoryStatusBarBackground)
        headerBackground = view.findViewById(R.id.tokonowSearchCategoryBackgroundImage)
    }

    protected open fun configureNavToolbar() {
        val navToolbar = navToolbar ?: return

        navToolbar.bringToFront()
        navToolbar.setToolbarPageName(toolbarPageName)
        navToolbar.setIcon(createNavToolbarIconBuilder())
        navToolbar.setupSearchbar(
                hints = getNavToolbarHint(),
                searchbarClickCallback = ::onSearchBarClick,
        )
        configureToolbarBackgroundInteraction()
    }

    private fun configureToolbarBackgroundInteraction() {
        val context = context ?: return

        navToolbar?.let { toolbar ->
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
            }

            viewLifecycleOwner.lifecycle.addObserver(toolbar)

            recyclerView?.addOnScrollListener(NavRecyclerViewScrollListener(
                    navToolbar = toolbar,
                    startTransitionPixel = searchCategoryToolbarHeight,
                    toolbarTransitionRangePixel = resources.getDimensionPixelSize(R.dimen.tokomart_searchbar_transition_range),
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) {

                        }

                        override fun onSwitchToLightToolbar() {

                        }

                        override fun onSwitchToDarkToolbar() {
                            navToolbar?.hideShadow()
                        }

                        override fun onYposChanged(yOffset: Int) {

                        }
                    },
                    fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            ))
        }

        configureEmptySpace()
    }

    private fun configureEmptySpace() {
        val context = context ?: return
        val top = NavToolbarExt.getFullToolbarHeight(context)
        stickyView?.setMargin(0.toDp(),top, 0.toDp(), 0.toDp())
    }

    protected abstract fun createNavToolbarIconBuilder(): IconBuilder

    protected fun IconBuilder.addShare(): IconBuilder = this
            .addIcon(ID_SHARE, disableRouteManager = false, disableDefaultGtmTracker = false) { }

    protected fun IconBuilder.addCart(): IconBuilder = this
            .addIcon(ID_CART, disableRouteManager = false, disableDefaultGtmTracker = false) { }

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

    protected open fun onSearchBarClick(hint: String) {
        val context = context ?: return

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
        params[SearchApiConst.BASE_SRP_APPLINK] = ApplinkConstInternalTokoMart.SEARCH

        return params
    }

    private fun configureStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        activity?.let {
            statusBarBackground?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBackground?.background
        drawable?.alpha = 0
        statusBarBackground?.background = drawable
    }

    protected open fun configureMiniCart() {
        val shopIds = listOf(getViewModel().shopId)

        miniCartWidget?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                autoInitializeData = false,
        )
    }

    protected open fun configureRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, VERTICAL)
        staggeredGridLayoutManager.gapStrategy = GAP_HANDLING_NONE

        endlessScrollListener = createEndlessScrollListener(staggeredGridLayoutManager)
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())

        recyclerView?.adapter = searchCategoryAdapter
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.addProductItemDecoration()

        endlessScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
        recyclerView?.addOnScrollListener(createScrollListener())
    }

    private fun createEndlessScrollListener(layoutManager: StaggeredGridLayoutManager) =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore()
                }
            }

    private fun createScrollListener() =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    evaluateSearchCategoryComponentOnScroll(recyclerView, dy)
                }
            }

    private fun evaluateSearchCategoryComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        headerBackground?.y = -(movingPosition.toFloat())
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
            navToolbar?.showShadow(lineShadow = true)
        } else {
            navToolbar?.hideShadow(lineShadow = true)
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
        getViewModel().miniCartWidgetLiveData.observe(this::updateMiniCartWidget)
        getViewModel().isShowMiniCartLiveData.observe(this::updateMiniCartWidgetVisibility)
        getViewModel().isRefreshPageLiveData.observe(this::scrollToTop)
        getViewModel().updatedVisitableIndicesLiveData.observeEvent(this::notifyAdapterItemChange)
        getViewModel().addToCartErrorMessageLiveData.observe(this::showAddToCartMessage)
    }

    abstract fun getViewModel(): BaseSearchCategoryViewModel

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    protected fun <T> LiveData<Event<T>>.observeEvent(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, EventObserver(onChanged))
    }

    protected open fun submitList(visitableList: List<Visitable<*>>) {
        searchCategoryAdapter?.submitList(visitableList)
    }

    protected open fun updateEndlessScrollListener(hasNextPage: Boolean) {
        endlessScrollListener?.updateStateAfterGetData()
        endlessScrollListener?.setHasNextPage(hasNextPage)
    }

    protected open fun onLoadMore() {
        getViewModel().onLoadMore()
    }

    protected open fun scrollToTop(isRefresh: Boolean) {
        stickyView?.scrollToTop()
    }

    override fun onLocalizingAddressSelected() {

    }

    override fun getFragment() = this

    override fun onSeeAllCategoryClicked() {
        RouteManager.route(
                context,
                ApplinkConstInternalTokoMart.CATEGORY_LIST,
                SearchApiConst.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
        )
    }

    override fun onBannerClick(applink: String) {
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
        categoryChooserBottomSheet?.setResultCountText(productCountText)
    }

    private fun configureL3BottomSheet(filter: Filter?) {
        if (filter != null)
            openCategoryChooserFilterPage(filter)
        else
            dismissCategoryChooserFilterPage()
    }

    private fun openCategoryChooserFilterPage(filter: Filter) {
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

    private fun dismissCategoryChooserFilterPage() {
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

    private fun updateMiniCartWidget(miniCartWidgetData: MiniCartWidgetData?) {
        miniCartWidgetData ?: return

        miniCartWidget?.updateData(miniCartWidgetData)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getViewModel().onViewUpdateCartItems(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetVisibility(isVisible: Boolean) {
        miniCartWidget?.showWithCondition(isVisible)
    }

    private fun notifyAdapterItemChange(indices: List<Int>) {
        indices.forEach {
            searchCategoryAdapter?.notifyItemChanged(it)
        }
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

    private fun showAddToCartMessage(message: String?) {
        val view = view ?: return
        message ?: return

        if (message.isEmpty()) {
            val successMessage = getString(R.string.tokomart_add_to_cart_success)
            Toaster.build(view, successMessage, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
        } else {
            Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onResume() {
        super.onResume()

        getViewModel().onViewResumed()
    }
}