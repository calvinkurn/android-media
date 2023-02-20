package com.tokopedia.tokopedianow.repurchase.presentation.fragment

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.EXTRA_SELECTED_CATEGORY_FILTER
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.REQUEST_CODE_CATEGORY_FILTER_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
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
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowRepurchaseBinding
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.EXTRA_SELECTED_DATE_FILTER
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.REQUEST_CODE_DATE_FILTER_BOTTOMSHEET
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.VALUE.REPURCHASE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.repurchase.di.component.DaggerRepurchaseComponent
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseAdapter
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseAdapterTypeFactory
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.differ.RepurchaseListDiffer
import com.tokopedia.tokopedianow.repurchase.presentation.listener.CategoryMenuCallback
import com.tokopedia.tokopedianow.repurchase.presentation.listener.ProductRecommendationCallback
import com.tokopedia.tokopedianow.repurchase.presentation.listener.ProductRecommendationOocCallback
import com.tokopedia.tokopedianow.repurchase.presentation.listener.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.repurchase.presentation.listener.TokoNowSimilarProductTrackerCallback
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedDateFilter
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedSortFilter
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseEmptyStateNoHistoryViewHolder.RepurchaseEmptyStateNoHistoryListener
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseSortFilterViewHolder.SortFilterListener
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.REQUEST_CODE_SORT_FILTER_BOTTOMSHEET
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.SORT_VALUE
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowRepurchaseFragment:
    Fragment(),
    MiniCartWidgetListener,
    TokoNowView,
    TokoNowChooseAddressWidgetListener,
    TokoNowEmptyStateNoResultListener,
    TokoNowRecommendationCarouselListener,
    RepurchaseEmptyStateNoHistoryListener,
    SortFilterListener,
    ServerErrorListener
{

    companion object {
        const val SOURCE = "tokonow"
        const val CATEGORY_LEVEL_DEPTH = 1

        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1

        fun newInstance(): TokoNowRepurchaseFragment {
            return TokoNowRepurchaseFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowRepurchaseViewModel
    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var analytics: RepurchaseAnalytics

    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var rvRepurchase: RecyclerView? = null
    private var navToolbar: NavToolbar? = null
    private var statusBarBg: View? = null
    private var miniCartWidget: MiniCartWidget? = null
    private val carouselScrollPosition = SparseIntArray()

    private var binding by autoClearedNullable<FragmentTokopedianowRepurchaseBinding>()

    private val adapter by lazy {
        RepurchaseAdapter(
            RepurchaseAdapterTypeFactory(
                productCardListener = createProductCardListener(),
                tokoNowSimilarProductTrackerListener = createSimilarProductTrackerCallback(),
                tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(),
                tokoNowChooseAddressWidgetListener = this,
                tokoNowListener = this,
                tokoNowCategoryMenuListener = createCategoryMenuCallback(),
                tokoNowEmptyStateNoResultListener = this,
                tokoNowRecommendationCarouselListener = this,
                emptyStateNoHistorylistener = this,
                sortFilterListener = this,
                serverErrorListener = this,
                tokonowRecomBindPageNameListener = createProductRecommendationOocListener(),
                productRecommendationListener = createProductRecommendationListener()
            ),
            RepurchaseListDiffer()
        )
    }

    private var localCacheModel: LocalCacheModel? = null
    private val loadMoreListener by lazy { createLoadMoreListener() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowRepurchaseBinding.inflate(inflater, container, false)
        return binding?.root
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
        if (isChooseAddressDataUpdated()) {
            refreshLayout()
        } else {
            getMiniCart()
        }
        updateToolbarNotification()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        }
        viewModel.updateToolbarNotification()
        viewModel.setProductAddToCartQuantity(miniCartSimplifiedData)
        setupPadding(miniCartSimplifiedData.isShowMiniCartWidget)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onChooseAddressWidgetRemoved() {
        if(rvRepurchase?.isComputingLayout == false) {
            viewModel.removeChooseAddressWidget()
        }
    }

    override fun onClickChooseAddressWidgetTracker() {
        analytics.onClickChangeAddress(userSession.userId)
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = refreshLayout()

    override fun onFindInTokopediaClick() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun goToTokopediaNowHome() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onClickEmptyStateNoHistoryBtn() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    override fun onImpressEmptyStateNoHistory() {
        analytics.onImpressNoHistoryRepurchase(userSession.userId)
    }

    override fun onRemoveFilterClick(option: Option) { /* noting to do */ }

    override fun onClickRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
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

    override fun onAddVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) {
        val productId = recomItem.productId.toString()
        val shopId = recomItem.shopId.toString()

        openATCVariantBottomSheet(productId, shopId)
    }

    override fun onSeeMoreClick(data: RecommendationCarouselData, applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) { /* nothing to do */ }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int = 0

    override fun onATCNonVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        recommendationCarouselPosition: Int,
        quantity: Int
    ) { /* nothing to do */ }

    override fun onBindRecommendationCarousel(
        model: TokoNowProductRecommendationOocUiModel,
        adapterPosition: Int
    ) { /* nothing to do */ }

    override fun onImpressedRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) { /* nothing to do */ }

    override fun onClickSortFilter() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.SORT_FILTER)
        val selectedFilter = viewModel.getSelectedSortFilter()
        intent.putExtra(SORT_VALUE, selectedFilter)
        startActivityForResult(intent, REQUEST_CODE_SORT_FILTER_BOTTOMSHEET)
        analytics.onClickMostPurchaseFilter(userSession.userId)
    }

    override fun onClickDateFilter() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.DATE_FILTER)
        val selectedFilter = viewModel.getSelectedDateFilter()
        intent.putExtra(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
        startActivityForResult(intent, REQUEST_CODE_DATE_FILTER_BOTTOMSHEET)
        analytics.onClickDateFilter(userSession.userId)
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
        analytics.onClickCategoryFilter(userSession.userId)
    }

    override fun onClearAllFilter() = refreshLayout()

    override fun onClickRetryButton() = refreshLayout()

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { /* nothing to do */ }

    private fun initInjector() {
        DaggerRepurchaseComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView() {
        swipeRefreshLayout = binding?.swipeRefreshLayout
        rvRepurchase = binding?.rvRepurchase
        navToolbar = binding?.navToolbar
        statusBarBg = binding?.statusBarBg
        miniCartWidget = binding?.miniCartWidget
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

    private fun updateToolbarNotification() {
        navToolbar?.updateNotification()
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
            viewLifecycleOwner.lifecycle.addObserver(toolbar)

            activity?.let {
                toolbar.setupToolbarWithStatusBar(
                    activity = it,
                    applyPadding = false
                )
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

    private fun onClickCartButton() {
        analytics.onClickCartNav(userSession.userId)
    }

    private fun isNavOld(): Boolean = false

    private fun setupRecyclerView() {
        context?.let {
            rvRepurchase?.apply {
                addProductItemDecoration()
                adapter = this@TokoNowRepurchaseFragment.adapter
                itemAnimator = null
                layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (adapter?.getItemViewType(position)) {
                                RepurchaseProductViewHolder.LAYOUT -> SPAN_FULL_SPACE
                                else -> SPAN_COUNT
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            val marginZero = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            val toolbarHeight = NavToolbarExt.getToolbarHeight(it)
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
                productRecommendationViewModel.updateMiniCartSimplified(it.data)
            }
        }

        observe(viewModel.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }
                is Fail -> {
                    showEmptyStateOoc()
                    logChooseAddressError(it.throwable)
                }
            }
        }

        observe(viewModel.miniCartAdd) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        actionText = getString(R.string.tokopedianow_toaster_see),
                        message = it.data.errorMessage.joinToString(separator = ", "),
                        type = Toaster.TYPE_NORMAL,
                        clickListener = {
                            miniCartWidget?.showMiniCartListBottomSheet(this)
                        }
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
                        actionText = getString(R.string.tokopedianow_toaster_ok),
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

        observe(viewModel.repurchaseAddToCartTracker) {
            when(it.data) {
                is RepurchaseProductUiModel -> {
                    trackRepurchaseAddToCart(
                        quantity = it.quantity,
                        data = it.data
                    )
                }
            }
        }

        observe(viewModel.openScreenTracker) { screenName ->
            TokoNowCommonAnalytics.onOpenScreen(
                isLoggedInStatus = userSession.isLoggedIn,
                screenName = screenName
            )
        }

        observe(viewModel.setUserPreference) {
            if(it is Success) {
                onSuccessSetUserPreference(it.data)
            }
        }

        observe(viewModel.updateToolbarNotification) { update ->
            if(update) {
                updateToolbarNotification()
            }
        }

        observe(productRecommendationViewModel.miniCartAdd) { result ->
            when (result) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        actionText = getString(R.string.tokopedianow_toaster_see),
                        message = result.data.errorMessage.joinToString(separator = ", "),
                        type = Toaster.TYPE_NORMAL,
                        clickListener = {
                            miniCartWidget?.showMiniCartListBottomSheet(this)
                        }
                    )
                }
                is Fail -> {
                    showToaster(
                        message = result.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(productRecommendationViewModel.miniCartUpdate) { result ->
            when (result) {
                is Success -> {
                    val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
                    miniCartWidget?.updateData(shopIds)
                }
                is Fail -> {
                    showToaster(
                        message = result.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        observe(productRecommendationViewModel.miniCartRemove) { result ->
            when (result) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        actionText = getString(R.string.tokopedianow_toaster_ok),
                        message = result.data.second,
                        type = Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    val message = result.throwable.message.orEmpty()
                    showToaster(message = message, type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessSetUserPreference(data: SetUserPreferenceData) {
        val warehouses = data.warehouses.map {
            LocalWarehouseModel(
                it.warehouseId.toLongOrZero(),
                it.serviceType
            )
        }

        ChooseAddressUtils.updateTokoNowData(
            requireContext(),
            data.warehouseId,
            data.shopId,
            warehouses,
            data.serviceType
        )

        refreshLayout()
    }

    private fun trackRepurchaseAddToCart(quantity: Int, data: RepurchaseProductUiModel) {
        analytics.onClickAddToCart(userSession.userId, quantity, data)
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
                shopId = chooseAddressData.tokonow.shopId.toString(),
                warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(chooseAddressData.tokonow.warehouses),
                serviceType = chooseAddressData.tokonow.serviceType,
                lastUpdate = chooseAddressData.tokonow.tokonowLastUpdate
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
        rvRepurchase?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListeners() {
        rvRepurchase?.addOnScrollListener(loadMoreListener)
    }

    private fun onCategoryFilterActivityResult(data: Intent?) {
        val selectedFilter = data?.getParcelableExtra<SelectedSortFilter>(EXTRA_SELECTED_CATEGORY_FILTER)
        viewModel.applyCategoryFilter(selectedFilter)
        analytics.onClickApplyCategoryFilter(userSession.userId)
    }

    private fun onSortFilterActivityResult(data: Intent?) {
        val selectedFilter = data?.getIntExtra(SORT_VALUE, FREQUENTLY_BOUGHT).orZero()
        viewModel.applySortFilter(selectedFilter)
        analytics.onClickApplyMostPurchaseFilter(userSession.userId)
    }

    private fun onDateFilterActivityResult(data: Intent?) {
        data?.getParcelableExtra<SelectedDateFilter>(EXTRA_SELECTED_DATE_FILTER)?.let {
            viewModel.applyDateFilter(it)
            analytics.onClickApplyDateFilter(userSession.userId) }
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
                if (isChooseAddressDataUpdated()) {
                    updateCurrentPageLocalCacheModelData()
                }
            }
        }
    }

    private fun isChooseAddressDataUpdated(): Boolean {
        localCacheModel?.let {
           return ChooseAddressUtils.isLocalizingAddressHasUpdated(requireContext(), it)
        }
        return false
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
        context?.let {
            when {
                shopId == 0L -> viewModel.getChooseAddress(SOURCE)
                warehouseId == 0L -> {
                    showEmptyStateOoc()
                }
                else -> {
                    showLayout()
                    viewModel.trackOpeningScreen(REPURCHASE_TOKONOW)
                }
            }
        }
    }

    private fun submitList(data: RepurchaseLayoutUiModel) {
        adapter.submitList(data.layoutList)
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        val showMiniCartWidget = data.isShowMiniCartWidget
        val outOfCoverage = localCacheModel?.isOutOfCoverage() == true

        if(showMiniCartWidget && !outOfCoverage) {
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            miniCartWidget?.initialize(shopIds, this, this, pageName = MiniCartAnalytics.Page.HOME_PAGE, source = MiniCartSource.TokonowRepurchasePage)
            miniCartWidget?.show()
        } else {
            miniCartWidget?.hide()
        }
    }

    private fun setupPadding(isShowMiniCartWidget: Boolean) {
        miniCartWidget?.post {
            val outOfCoverage = localCacheModel?.isOutOfCoverage() == true
            val paddingBottom = if (isShowMiniCartWidget && !outOfCoverage) {
                miniCartWidget?.height.orZero() - context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_font_16).toIntSafely()
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

    private fun showEmptyStateOoc() {
        viewModel.showEmptyState(EMPTY_STATE_OOC)
        miniCartWidget?.hide()
        setupPadding(false)
        viewModel.trackOpeningScreen(SCREEN_NAME_TOKONOW_OOC + REPURCHASE_TOKONOW)
    }

    private fun getMiniCartHeight(): Int {
        return miniCartWidget?.height.orZero() - context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)?.toInt().orZero()
    }

    private fun showToaster(message: String, duration: Int = Toaster.LENGTH_SHORT, type: Int, actionText: String = "", clickListener: () -> Unit = {}) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = {
                        clickListener()
                    }
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
        val layoutManager = rvRepurchase?.layoutManager as? StaggeredGridLayoutManager
        val index = layoutManager?.findLastCompletelyVisibleItemPositions(null)
        val itemCount = layoutManager?.itemCount.orZero()
        viewModel.onScrollProductList(index, itemCount)
    }

    private fun refreshLayout() {
        carouselScrollPosition.clear()
        viewModel.clearSelectedFilters()
        viewModel.showLoading()
        refreshMiniCart()
        refreshProductRecommendation()
    }

    private fun refreshProductRecommendation() {
        productRecommendationViewModel.updateProductRecommendation(
            GetRecommendationRequestParam(
                pageName = PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM,
                xSource = RECOM_WIDGET,
                isTokonow = true,
                pageNumber = PAGE_NUMBER_RECOM_WIDGET,
                xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE,
            )
        )
    }

    private fun refreshMiniCart() {
        checkIfChooseAddressWidgetDataUpdated()
        getMiniCart()
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
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
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
            analytics,
            this::startActivityForResult
        ) { descriptionToaster, type, ctaToaster, clickListener ->
            showToaster(
                message = descriptionToaster,
                type = type,
                actionText = ctaToaster,
                clickListener = clickListener
            )
        }
    }

    private fun createTokoNowEmptyStateOocListener(): TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
        return object : TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
            override fun onRefreshLayoutPage() {
                refreshLayout()
            }

            override fun onGetFragmentManager(): FragmentManager = parentFragmentManager

            override fun onGetEventCategory(): String = ""

            override fun onSwitchService() {
                viewModel.switchService()
            }
        }
    }

    private fun createProductRecommendationOocListener(): ProductRecommendationOocCallback {
        return ProductRecommendationOocCallback(
            activity = activity,
            lifecycle = viewLifecycleOwner.lifecycle
        )
    }

    private fun createProductRecommendationListener(): ProductRecommendationCallback {
        return ProductRecommendationCallback(
            productRecommendationViewModel = productRecommendationViewModel,
            tokoNowRepurchaseViewModel = viewModel,
            activity = activity,
            startActivityForResult=::startActivityForResult
        )
    }

    private fun createSimilarProductTrackerCallback(): TokoNowSimilarProductTrackerCallback {
        return TokoNowSimilarProductTrackerCallback(analytics)
    }

    private fun createCategoryMenuCallback(): CategoryMenuCallback {
        return CategoryMenuCallback(
            analytics = analytics,
            viewModel = viewModel
        )
    }
}
