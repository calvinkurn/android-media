package com.tokopedia.tokopedianow.recentpurchase.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.EXTRA_SELECTED_CATEGORY_FILTER
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.REQUEST_CODE_CATEGORY_FILTER_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.ConstantKey
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.ATC_QUANTITY_ERROR
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.CHOOSE_ADDRESS_ERROR
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.ErrorType.ERROR_ADD_TO_CART
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.ErrorType.ERROR_CHOOSE_ADDRESS
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.ErrorType.ERROR_LAYOUT
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.LOAD_LAYOUT_ERROR
import com.tokopedia.tokopedianow.common.util.TokoMartRepurchaseErrorLogger.LOAD_MORE_ERROR
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.recentpurchase.di.component.DaggerRecentPurchaseComponent
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseAdapter
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseAdapterTypeFactory
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.differ.RecentPurchaseListDiffer
import com.tokopedia.tokopedianow.recentpurchase.presentation.listener.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel.TokoNowRecentPurchaseViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.*
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.*
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.*
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.*
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecommendationCarouselViewHolder.*
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.EXTRA_SELECTED_DATE_FILTER
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.REQUEST_CODE_DATE_FILTER_BOTTOMSHEET
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.view.decoration.RepurchaseGridItemDecoration
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseEmptyStateNoHistoryViewHolder.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseSortFilterViewHolder.*
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.REQUEST_CODE_SORT_FILTER_BOTTOMSHEET
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.SORT_VALUE
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject

class TokoNowRecentPurchaseFragment:
    Fragment(),
    MiniCartWidgetListener,
    TokoNowView,
    TokoNowChooseAddressWidgetListener,
    TokoNowCategoryGridListener,
    TokoNowEmptyStateNoResultListener,
    TokoNowRecommendationCarouselListener,
    RepurchaseEmptyStateNoHistoryListener,
    SortFilterListener,
    ServerErrorListener
{

    companion object {
        const val SOURCE = "tokonow"
        const val CATEGORY_LEVEL_DEPTH = 1

        private const val GRID_SPAN_COUNT = 2

        fun newInstance(): TokoNowRecentPurchaseFragment {
            return TokoNowRecentPurchaseFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRecentPurchaseViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var rvRecentPurchase: RecyclerView? = null
    private var navToolbar: NavToolbar? = null
    private var statusBarBg: View? = null
    private var miniCartWidget: MiniCartWidget? = null
    private val carouselScrollPosition = SparseIntArray()

    private val adapter by lazy {
        RecentPurchaseAdapter(
            RecentPurchaseAdapterTypeFactory(
                productCardListener = createProductCardListener(),
                tokoNowChooseAddressWidgetListener = this,
                tokoNowListener = this,
                tokoNowCategoryGridListener = this,
                tokoNowEmptyStateNoResultListener = this,
                tokoNowRecommendationCarouselListener = this,
                emptyStateNoHistorylistener = this,
                sortFilterListener = this,
                serverErrorListener = this
            ),
            RecentPurchaseListDiffer()
        )
    }

    private var localCacheModel: LocalCacheModel? = null
    private val loadMoreListener by lazy { createLoadMoreListener() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_recent_purchase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupStatusBar()
        setupNavToolbar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()

        viewModel.showLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_CATEGORY_FILTER_BOTTOM_SHEET -> onCategoryFilterActivityResult(data)
            REQUEST_CODE_SORT_FILTER_BOTTOMSHEET -> onSortFilterActivityResult(data)
            REQUEST_CODE_DATE_FILTER_BOTTOMSHEET -> onDateFilterActivityResult(data)
        }
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
        getMiniCart()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        }
        viewModel.setProductAddToCartQuantity(miniCartSimplifiedData)
        setupPadding(miniCartSimplifiedData.isShowMiniCartWidget)
    }

    override fun onChooseAddressWidgetRemoved() {
        if(rvRecentPurchase?.isComputingLayout == false) {
            viewModel.removeChooseAddressWidget()
        }
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = refreshLayout()

    override fun onCategoryRetried() {
        // TO-DO : retry to get category
    }

    override fun onAllCategoryClicked() {
        // TO-DO : analytics
    }

    override fun onCategoryClicked(position: Int, categoryId: String) {
        // TO-DO : analytics
    }

    override fun onFindInTokopediaClick() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun goToTokopediaNowHome() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onClickEmptyStateNoHistoryBtn() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onRemoveFilterClick(option: Option) { /* noting to do */ }

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) {
        carouselScrollPosition.put(adapterPosition, scrollPosition)
    }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int {
        return carouselScrollPosition.get(adapterPosition)
    }

    override fun onBindRecommendationCarousel(
        model: TokoNowRecommendationCarouselUiModel,
        adapterPosition: Int
    ) {
        // TO-DO :
    }

    override fun onImpressedRecommendationCarouselItem(
        model: TokoNowRecommendationCarouselUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        // TO-DO :
    }

    override fun onClickRecommendationCarouselItem(
        model: TokoNowRecommendationCarouselUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            recomItem.productId.toString(),
        )
    }

    override fun onATCNonVariantRecommendationCarouselItem(
        model: TokoNowRecommendationCarouselUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        recommendationCarouselPosition: Int,
        quantity: Int
    ) {
        if (userSession.isLoggedIn) {
            viewModel.onClickAddToCart(recomItem.productId.toString(), quantity, recomItem.shopId.toString())
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onAddVariantRecommendationCarouselItem(
        model: TokoNowRecommendationCarouselUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) {
        val productId = recomItem.productId.toString()
        val shopId = recomItem.shopId.toString()

        openATCVariantBottomSheet(productId, shopId)
    }

    override fun onSeeMoreClick(data: RecommendationCarouselData, applink: String) {
        // TO-DO: Implement see more click tracking
    }

    override fun onClickSortFilter() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.SORT_FILTER)
        val selectedFilter = viewModel.getSelectedSortFilter()
        intent.putExtra(SORT_VALUE, selectedFilter)
        startActivityForResult(intent, REQUEST_CODE_SORT_FILTER_BOTTOMSHEET)
    }

    override fun onClickDateFilter() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.DATE_FILTER)
        val selectedFilter = viewModel.getSelectedDateFilter()
        intent.putExtra(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
        startActivityForResult(intent, REQUEST_CODE_DATE_FILTER_BOTTOMSHEET)
    }

    override fun onClickCategoryFilter() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalTokopediaNow.CATEGORY_FILTER,
            localCacheModel?.warehouse_id.orEmpty()
        )
        val selectedFilter = viewModel.getSelectedCategoryFilter()
        intent.putExtra(EXTRA_SELECTED_CATEGORY_FILTER, selectedFilter)
        startActivityForResult(intent, REQUEST_CODE_CATEGORY_FILTER_BOTTOM_SHEET)
    }

    override fun onClearAllFilter() = refreshLayout()

    override fun onClickRetryButton() = refreshLayout()

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {

    }

    private fun initInjector() {
        DaggerRecentPurchaseComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView() {
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        rvRecentPurchase = view?.findViewById(R.id.rv_recent_purchase)
        navToolbar = view?.findViewById(R.id.nav_toolbar)
        statusBarBg = view?.findViewById(R.id.status_bar_bg)
        miniCartWidget = view?.findViewById(R.id.mini_cart_widget)
    }

    private fun setupStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        activity?.let {
            statusBarBg?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBg?.background
        drawable?.alpha = 0
        statusBarBg?.background = drawable
    }


    private fun setupNavToolbar() {
        setupTopNavigation()
        navAbTestCondition (
            ifNavRevamp = {
                setIconNewTopNavigation()
            },
            ifNavOld = {
                setIconOldTopNavigation()
            }
        )
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else {
            ifNavOld.invoke()
        }
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
            }
        }
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
            .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setIconOldTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
        navToolbar?.setIcon(icons)
    }

    private fun onClickCartButton() {}

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    private fun isNavOld(): Boolean {
        return try {
            getAbTestPlatform().getString(ConstantKey.AB_TEST_EXP_NAME, ConstantKey.AB_TEST_VARIANT_OLD) == ConstantKey.AB_TEST_VARIANT_OLD
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    private fun setupRecyclerView() {
        context?.let {
            rvRecentPurchase?.apply {
                adapter = this@TokoNowRecentPurchaseFragment.adapter
                layoutManager = object: StaggeredGridLayoutManager(GRID_SPAN_COUNT, VERTICAL) {
                    override fun supportsPredictiveItemAnimations() = false
                }.apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
                addItemDecoration(RepurchaseGridItemDecoration())
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            val marginZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            val toolbarHeight = NavToolbarExt.getFullToolbarHeight(it)
            swipeRefreshLayout?.setMargin(marginZero, toolbarHeight, marginZero, marginZero)
            swipeRefreshLayout?.setOnRefreshListener {
                refreshLayout()
            }
        }
    }

    private fun observeLiveData() {
        observe(viewModel.getLayout) {
            removeScrollListeners()

            when(it) {
                is Success -> onSuccessGetLayout(it.data)
                is Fail -> logGetLayoutError(it.throwable)
            }

            addScrollListeners()
            resetSwipeLayout()
        }

        observe(viewModel.loadMore) {
            removeScrollListeners()
            when(it) {
                is Success -> submitList(it.data)
                is Fail -> logLoadMoreError(it.throwable)
            }
            addScrollListeners()
        }

        observe(viewModel.atcQuantity) {
            removeScrollListeners()
            when(it) {
                is Success -> submitList(it.data)
                is Fail -> logATCQuantityError(it.throwable)
            }
            addScrollListeners()
        }

        observe(viewModel.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
                setupPadding(it.data.isShowMiniCartWidget)
            }
        }

        observe(viewModel.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }
                is Fail -> {
                    showEmptyState(EMPTY_STATE_OOC)
                    logChooseAddressError(it.throwable)
                }
            }
        }

        observe(viewModel.miniCartAdd) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.errorMessage.joinToString(separator = ", "),
                        type = Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModel.miniCartUpdate) {
            when(it) {
                is Success -> {
                    val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
                    miniCartWidget?.updateData(shopIds)
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModel.miniCartRemove) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.second,
                        type = Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    val message = it.throwable.message.orEmpty()
                    showToaster(message = message, type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun setupChooseAddress(data: GetStateChosenAddressResponse) {
        data.let { chooseAddressData ->
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = requireContext(),
                addressId = chooseAddressData.data.addressId.toString(),
                cityId = chooseAddressData.data.cityId.toString(),
                districtId = chooseAddressData.data.districtId.toString(),
                lat = chooseAddressData.data.latitude,
                long = chooseAddressData.data.longitude,
                label = String.format(
                    "%s %s",
                    chooseAddressData.data.addressName,
                    chooseAddressData.data.receiverName
                ),
                postalCode = chooseAddressData.data.postalCode,
                warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                shopId = chooseAddressData.tokonow.shopId.toString()
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        checkStateNotInServiceArea(
            warehouseId = data.tokonow.warehouseId
        )
    }

    private fun logGetLayoutError(throwable: Throwable) {
        TokoMartRepurchaseErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_LAYOUT,
            deviceId = userSession.deviceId,
            description = LOAD_LAYOUT_ERROR
        )
    }

    private fun logLoadMoreError(throwable: Throwable) {
        TokoMartRepurchaseErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_LAYOUT,
            deviceId = userSession.deviceId,
            description = LOAD_MORE_ERROR
        )
    }

    private fun logATCQuantityError(throwable: Throwable) {
        TokoMartRepurchaseErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_ADD_TO_CART,
            deviceId = userSession.deviceId,
            description = ATC_QUANTITY_ERROR
        )
    }

    private fun logChooseAddressError(throwable: Throwable) {
        TokoMartRepurchaseErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_CHOOSE_ADDRESS,
            deviceId = userSession.deviceId,
            description = CHOOSE_ADDRESS_ERROR
        )
    }

    private fun removeScrollListeners() {
        rvRecentPurchase?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListeners() {
        rvRecentPurchase?.addOnScrollListener(loadMoreListener)
    }

    private fun onCategoryFilterActivityResult(data: Intent?) {
        val selectedFilter = data?.getParcelableExtra<SelectedSortFilter>(EXTRA_SELECTED_CATEGORY_FILTER)
        viewModel.applyCategoryFilter(selectedFilter)
    }

    private fun onSortFilterActivityResult(data: Intent?) {
        val selectedFilter = data?.getIntExtra(SORT_VALUE, FREQUENTLY_BOUGHT).orZero()
        viewModel.applySortFilter(selectedFilter)
    }

    private fun onDateFilterActivityResult(data: Intent?) {
        val selectedFilter = data?.getParcelableExtra<SelectedDateFilter>(EXTRA_SELECTED_DATE_FILTER)
        viewModel.applyDateFilter(selectedFilter)
    }

    private fun onSuccessGetLayout(data: RepurchaseLayoutUiModel) {
        submitList(data)

        when(data.state) {
            TokoNowLayoutState.LOADING -> onLoadingLayout()
            TokoNowLayoutState.SHOW -> viewModel.getLayoutData()
            TokoNowLayoutState.LOADED -> viewModel.getAddToCartQuantity()
        }
    }

    private fun onLoadingLayout() {
        checkAddressDataAndServiceArea()
        if (!isChooseAddressWidgetShowed()) {
            viewModel.removeChooseAddressWidget()
        }
    }

    private fun isChooseAddressWidgetShowed(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(
            TokoNowChooseAddressWidgetViewHolder.ENABLE_CHOOSE_ADDRESS_WIDGET,
            true
        )
    }

    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
        val shopId = localCacheModel?.shop_id.toLongOrZero()
        val warehouseId = localCacheModel?.warehouse_id.toLongOrZero()
        checkStateNotInServiceArea(shopId = shopId, warehouseId = warehouseId)
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
            viewModel.setLocalCacheModel(localCacheModel)
        }
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(this, it)) {
                    updateCurrentPageLocalCacheModelData()
                }
            }
        }
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
        context?.let {
            when {
                shopId == 0L -> viewModel.getChooseAddress(SOURCE)
                warehouseId == 0L -> showEmptyState(EMPTY_STATE_OOC)
                else -> showLayout()
            }
        }
    }

    private fun submitList(data: RepurchaseLayoutUiModel) {
        adapter.submitList(data.layoutList)
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if(data.isShowMiniCartWidget) {
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            miniCartWidget?.initialize(shopIds, this, this, pageName = MiniCartAnalytics.Page.HOME_PAGE)
            miniCartWidget?.show()
        } else {
            miniCartWidget?.hide()
        }
    }

    private fun setupPadding(isShowMiniCartWidget: Boolean) {
        miniCartWidget?.post {
            val paddingBottom = if (isShowMiniCartWidget) {
                miniCartWidget?.height.orZero()
            } else {
                activity?.resources?.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            }
            swipeRefreshLayout?.setPadding(0, 0, 0, paddingBottom)
        }
    }

    private fun getMiniCart() {
        val shopId = listOf(localCacheModel?.shop_id.orEmpty())
        val warehouseId = localCacheModel?.warehouse_id
        viewModel.getMiniCart(shopId, warehouseId)
    }

    private fun showEmptyState(id: String) {
        viewModel.showEmptyState(id)
        miniCartWidget?.hide()
        setupPadding(false)
    }

    private fun showToaster(message: String, duration: Int = Toaster.LENGTH_SHORT, type: Int) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type
                ).show()
            }
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollProductList()
            }
        }
    }

    private fun showLayout() {
        // TO-DO: Show Layout (Repurchase page + Minicart)
        viewModel.getLayoutList()
    }

    private fun onScrollProductList() {
        val layoutManager = rvRecentPurchase?.layoutManager as? StaggeredGridLayoutManager
        val index = layoutManager?.findLastCompletelyVisibleItemPositions(null)
        val itemCount = layoutManager?.itemCount.orZero()
        viewModel.onScrollProductList(index, itemCount)
    }

    private fun refreshLayout() {
        carouselScrollPosition.clear()
        viewModel.clearSelectedFilters()
        viewModel.showLoading()
    }

    private fun resetSwipeLayout() {
        swipeRefreshLayout?.apply {
            isEnabled = true
            isRefreshing = false
        }
    }

    private fun openATCVariantBottomSheet(productId: String, shopId: String) {
        val context = context ?: return

        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = productId,
            pageSource = SOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = "",
            startActivitResult = this::startActivityForResult,
        )
    }

    private fun createProductCardListener(): RepurchaseProductCardListener {
        return RepurchaseProductCardListener(
            requireContext(),
            viewModel,
            userSession,
            this::startActivityForResult
        )
    }
}