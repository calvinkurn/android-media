package com.tokopedia.tokopedianow.shoppinglist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantKey
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowShoppingListBinding
import com.tokopedia.tokopedianow.home.domain.model.Data
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.common.abstraction.AbstractEmptyStateOocListener
import com.tokopedia.tokopedianow.common.abstraction.AbstractProductRecommendationListener
import com.tokopedia.tokopedianow.common.abstraction.AbstractThematicHeaderListener
import com.tokopedia.tokopedianow.common.helper.LinearLayoutManagerWithAccurateOffset
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLocalLoadViewHolder
import com.tokopedia.tokopedianow.shoppinglist.abstraction.AbstractShoppingListHorizontalProductCardItemListener
import com.tokopedia.tokopedianow.shoppinglist.presentation.activity.TokoNowShoppingListActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapterTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet.TokoNowShoppingListAnotherOptionBottomSheet
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.CoachMarkModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_MULTI_PRODUCTS_TO_CART
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListExpandCollapseViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListRetryViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListTopCheckAllViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify.Companion.COLOR_WHITE
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.DELETE_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListCartProductViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListLoadingMoreViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.searchbar.R as searchbarR

class TokoNowShoppingListFragment :
    Fragment(),
    TokoNowView,
    TokoNowChooseAddressWidgetListener,
    MiniCartWidgetListener
{
    companion object {
        private const val TOP_CHECK_ALL_MAX_ALPHA = 255f
        private const val COACH_MARK_NOW_SHOPPING_LIST_PAGE = "coach_mark_shopping_list_page"
        private const val COACH_MARK_NOW_SHOPPING_LIST_BOTTOM_BULK_ATC = "coach_mark_shopping_list_page_bottom_bulk_atc"
        private const val FIRST_INSTALL_CACHE_VALUE = 1800000L
        private const val MAX_VIEW_POOL = 20
        private const val ITEM_VIEW_CACHE_SIZE = 20

        fun newInstance(): TokoNowShoppingListFragment = TokoNowShoppingListFragment()
    }

    /**
     * -- private variable section --
     */

    private val adapter: ShoppingListAdapter by lazy {
        ShoppingListAdapter(
            ShoppingListAdapterTypeFactory(
                tokoNowView = this@TokoNowShoppingListFragment,
                headerListener = createHeaderCallback(),
                productCardItemListener = createHorizontalProductCardItemCallback(),
                retryListener = createRetryCallback(),
                errorListener = createErrorCallback(),
                expandCollapseListener = createExpandCollapseCallback(),
                topCheckAllListener = createTopCheckAllCallback(),
                cartProductListener = createCartProductCallback(),
                emptyStateOocListener = createEmptyStateOocCallback(),
                productRecommendationOocListener = createProductRecommendationCallback(),
                productRecommendationOocBindListener = createProductRecommendationCallback(),
                localLoadListener = createLocalLoadCallback()
            )
        )
    }

    private var isNavToolbarScrollingBehaviourEnabled: Boolean = true
    private var isStickyTopCheckAllScrollingBehaviorEnabled: Boolean = false
    private var isLightToolbarSwitched: Boolean = true
    private var isShowFirstInstallSearch: Boolean = false

    private var loader: LoaderDialog? = null
    private var pageCoachMark: CoachMarkModel? = null
    private var collectJob: Job? = null
    private var toaster: Snackbar? = null

    private val mRecycledViewPool: RecyclerView.RecycledViewPool
        get() = RecyclerView.RecycledViewPool()

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModel: TokoNowShoppingListViewModel

    @Inject
    lateinit var analytic: ShoppingListAnalytic

    /**
     * -- internal variable section --
     */

    internal val loadMoreListener: RecyclerView.OnScrollListener
        by lazy { createLoadMoreCallback() }

    internal var binding: FragmentTokopedianowShoppingListBinding?
        by autoClearedNullable()

    internal var layoutManager: LinearLayoutManager?
        by autoClearedNullable()

    /**
     * -- override function section --
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(activity)
        firebaseRemoteConfig.let {
            isShowFirstInstallSearch = it.getBoolean(ConstantKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowShoppingListBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchToDarkStatusBar()

        binding?.apply {
            collectStateFlow()

            setupRecyclerView()
            setupSwipeToRefreshLayout()
            setupFloatingActionButton()
            setupBottomBulkAtc()
            setupMiniCart()
            setupNavigationToolbar()
            setupOnScrollListener()
        }

        viewModel.loadLayout()
    }

    override fun onResume() {
        super.onResume()
        resumeLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRecycledViewPool.clear()
        viewModel.clear()
        collectJob?.cancel()
        collectJob = null
    }

    override fun getFragmentPage(): Fragment = this@TokoNowShoppingListFragment

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.setMiniCartData(miniCartSimplifiedData)
        binding?.navToolbar?.updateNotification()
    }

    override fun refreshLayoutPage() {
        binding?.apply {
            rvShoppingList.removeOnScrollListener(loadMoreListener)
            rvShoppingList.addOnScrollListener(loadMoreListener)
            viewModel.refreshLayout()
        }
    }

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { }

    override fun onChooseAddressWidgetRemoved() { }

    override fun onClickChooseAddressWidgetTracker() { }

    /**
     * -- private suspend function section --
     */

    private suspend fun collectLayoutState(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.layoutState.collect { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    val layout = uiState.data?.layout
                    if (!layout.isNullOrEmpty()) {
                        adapter.submitList(layout)
                    }
                }
                is UiState.Error -> {
                    val layout = uiState.data?.layout
                    if (!layout.isNullOrEmpty()) {
                        adapter.submitList(layout)
                    }
                }
                is UiState.Success -> {
                    if (uiState.data.layout.isNotEmpty()) {
                        adapter.submitList(uiState.data.layout)
                    }

                    if (uiState.data.isRequiredToScrollUp) {
                        binding.rvShoppingList.scrollToPosition(Int.ZERO)
                    }
                }
            }
        }
    }

    private suspend fun collectMiniCartState(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.miniCartState.collect {
            binding.apply {
                when(it) {
                    is UiState.Error -> hideMiniCart()
                    is UiState.Success -> showMiniCart(it.data)
                    is UiState.Loading -> { /* nothing to do */ }
                }
            }
        }
    }

    private suspend fun collectScrollBehavior(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isOnScrollNotNeeded.collect { isNotNeededToScroll ->
            if (isNotNeededToScroll) binding.rvShoppingList.removeOnScrollListener(loadMoreListener)
        }
    }

    private suspend fun collectTopCheckAllSelected(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isTopCheckAllSelected.collect { isSelected ->
            binding.stickyTopCheckAll.cbTopCheckAll.apply {
                setOnCheckedChangeListener(null)
                isChecked = isSelected
                setOnCheckedChangeListener(createTopCheckAllCheckedChangeCallback())
            }
        }
    }

    private suspend fun collectProductAvailability(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isProductAvailable.collect { isAvailable ->
            binding.apply {
                isStickyTopCheckAllScrollingBehaviorEnabled = isAvailable

                if (isAvailable) {
                    bottomBulkAtcView.show()
                    analytic.getShoppingListBottomBulkAtcAnalytic().trackImpressMiniCartShoppingList()
                    showBottomBulkAtcCoachMark()
                } else {
                    bottomBulkAtcView.hide()
                }

                adjustRecyclerViewBottomPadding()
            }
        }
    }

    private suspend fun collectBottomBulkAtc(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.bottomBulkAtcData.collect { model ->
            if (model != null) {
                binding.apply {
                    if (!isVisible) {
                        bottomBulkAtcView.show()
                        analytic.getShoppingListBottomBulkAtcAnalytic().trackImpressMiniCartShoppingList()
                        adjustRecyclerViewBottomPadding()
                    }
                    binding.bottomBulkAtcView.bind(
                        counter = model.counter,
                        priceInt = model.price
                    )
                }
            }
        }
    }

    private suspend fun collectNavToolbarBehavior(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isNavToolbarScrollingBehaviourEnabled.collect { isEnabled ->
            binding.navToolbar.apply {
                isNavToolbarScrollingBehaviourEnabled = isEnabled
                binding.strRefreshLayout.isEnabled = isEnabled
                setShowShadowEnabled(!isEnabled)

                if (isEnabled) {
                    setToolbarContentType(TOOLBAR_TYPE_TITLE)
                    switchToDarkToolbar(force = true)
                    switchToDarkStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White))
                    hideShadow()
                } else {
                    setToolbarContentType(TOOLBAR_TYPE_SEARCH)
                    switchToLightToolbar(force = true)
                    switchToLightStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, (if (context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                    showShadow()
                    setHint(SearchPlaceholder(Data(null, context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty(), "")))
                }
            }
        }
    }

    private suspend fun collectLoaderDialogShown() {
        viewModel.isLoaderDialogShown.collect { isShown ->
            if (isShown) {
                loader = context?.let {
                    val loader = LoaderDialog(it)
                    loader.setLoadingText(String.EMPTY)
                    loader.show()
                    loader
                }
            } else {
                loader?.dismiss()
            }
        }
    }

    private suspend fun collectToasterErrorShown(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.toasterData.collect { data ->
            if (data != null) {
                when(data.event) {
                    ADD_WISHLIST -> {
                        showToaster(data) {
                            if (data.any is ShoppingListHorizontalProductCardItemUiModel) {
                                analytic.getShoppingListHorizontalProductCardAnalytic().trackClickAddToShoppingListOnProduct(data.any)
                                viewModel.addToWishlist(data.any)
                            }
                        }
                    }
                    DELETE_WISHLIST -> {
                        showToaster(data) {
                            if (data.any is ShoppingListHorizontalProductCardItemUiModel) {
                                if (data.type == Toaster.TYPE_NORMAL) {
                                    analytic.getShoppingListHorizontalProductCardAnalytic().trackClickAddToShoppingListOnProduct(data.any)
                                    viewModel.addToWishlist(data.any)
                                } else {
                                    analytic.getShoppingListHorizontalProductCardAnalytic().trackClickDeleteButtonOnProduct(data.any)
                                    viewModel.deleteFromWishlist(data.any)
                                }
                            }
                        }
                    }
                    ADD_MULTI_PRODUCTS_TO_CART -> {
                        showToaster(data) {
                            viewModel.addMultiProductsToCart(
                                onTrackAddToCartMulti = {
                                    analytic.getShoppingListBottomBulkAtcAnalytic().trackClickAtcMiniCartShoppingList(it)
                                }
                            )
                        }
                        if(data.any != null && data.any is MiniCartSimplifiedData) {
                            binding.miniCartWidget.updateData(data.any)
                            binding.navToolbar.updateNotification()
                        }
                    }
                }
            }
        }
    }

    private suspend fun collectIsCoachMarkShown(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isCoachMarkShown.collect { isShown ->
            if (isShown) binding.showPageCoachMark()
        }
    }

    private suspend fun collectIsPageImpressionTracked() {
        viewModel.isPageImpressionTracked.collect { isTracked ->
            if (isTracked) analytic.trackImpressShoppingListPage()
        }
    }

    /**
     * -- private function section --
     */

    private fun initInjector() {
        DaggerShoppingListComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .shoppingListModule(ShoppingListModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun showToaster(
        model: ToasterModel,
        clickListener: View.OnClickListener
    ) {
        view?.let { view ->
            if (model.text.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getBottomSpace()
                toaster?.dismiss()
                toaster = Toaster.buildWithAction(
                    view = view,
                    text = model.text,
                    duration = model.duration,
                    type = model.type,
                    actionText = model.actionText,
                    clickListener = clickListener
                ).apply {
                    show()
                }
            }
        }
    }

    private fun getBottomSpace(): Int = if (binding?.miniCartWidget?.isVisible.orFalse() || binding?.bottomBulkAtcView?.isVisible.orFalse()) context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_bottom_view_height).orZero() else Int.ZERO

    private fun FragmentTokopedianowShoppingListBinding.collectStateFlow() {
        collectJob = viewLifecycleOwner.lifecycleScope.launch {
            withStarted {
                launch { collectLayoutState(this@collectStateFlow) }
                launch { collectMiniCartState(this@collectStateFlow) }
                launch { collectScrollBehavior(this@collectStateFlow) }
                launch { collectNavToolbarBehavior(this@collectStateFlow) }
                launch { collectTopCheckAllSelected(this@collectStateFlow) }
                launch { collectProductAvailability(this@collectStateFlow) }
                launch { collectBottomBulkAtc(this@collectStateFlow) }
                launch { collectLoaderDialogShown() }
                launch { collectToasterErrorShown(this@collectStateFlow) }
                launch { collectIsCoachMarkShown(this@collectStateFlow) }
                launch { collectIsPageImpressionTracked() }
            }
        }
    }

    private fun resumeLayout() {
        viewModel.resumeLayout()
        binding?.navToolbar?.updateNotification()
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!viewModel.isLoggedIn() && isShowFirstInstallSearch) {
                val sharedPrefs = it.getSharedPreferences(ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
                if (firstInstallCacheValue.isZero()) return false
                firstInstallCacheValue += FIRST_INSTALL_CACHE_VALUE
                val now = Date()
                val firstInstallTime = Date(firstInstallCacheValue)
                return if (now <= firstInstallTime) {
                    true
                } else {
                    saveFirstInstallTime()
                    false
                }
            } else {
                return false
            }
        }
        return false
    }

    private fun saveFirstInstallTime() {
        context?.let {
            val sharedPrefs = it.getSharedPreferences(ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, Int.ZERO.toLong())?.apply()
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.showPageCoachMark() {
        rvShoppingList.post {
            var tpShoppingList: View? = null
            var tpAddToShoppingList: View? = null
            var tpBuyAgain: View? = null

            adapter.getHeader()?.let {
                rvShoppingList.findViewHolderForAdapterPosition(adapter.findPosition(it))?.apply {
                    tpShoppingList = itemView.findViewById(R.id.vtp_title)
                    tpBuyAgain = itemView.findViewById(R.id.tp_cta)
                }
            }

            adapter.getFirstProductRecommendation()?.let {
                rvShoppingList.findViewHolderForAdapterPosition(adapter.findPosition(it))?.apply {
                    tpAddToShoppingList = itemView.findViewById(R.id.tp_add_wishlist)
                }
            }

            if (!CoachMarkPreference.hasShown(root.context, COACH_MARK_NOW_SHOPPING_LIST_PAGE)) {
                pageCoachMark = CoachMarkModel {
                    CoachMarkPreference.setShown(root.context, COACH_MARK_NOW_SHOPPING_LIST_PAGE, true)
                }.apply {
                    setDataForShoppingListPage(
                        tpShoppingList = tpShoppingList,
                        tpAddToShoppingList = tpAddToShoppingList,
                        tpBuyAgain = tpBuyAgain
                    )
                    show(root.context)
                }
            }
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.showBottomBulkAtcCoachMark() {
        if (!CoachMarkPreference.hasShown(root.context, COACH_MARK_NOW_SHOPPING_LIST_BOTTOM_BULK_ATC)) {
            CoachMarkModel {
                CoachMarkPreference.setShown(root.context, COACH_MARK_NOW_SHOPPING_LIST_BOTTOM_BULK_ATC, true)
            }.apply {
                setDataForBottomBulkAtc(
                    ubAtc = bottomBulkAtcView.ubAtc
                )
                show(root.context)
            }
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupRecyclerView() {
        layoutManager = LinearLayoutManagerWithAccurateOffset(context)
        layoutManager?.recycleChildrenOnDetach = true
        rvShoppingList.apply {
            adapter = this@TokoNowShoppingListFragment.adapter
            layoutManager = this@TokoNowShoppingListFragment.layoutManager
            addItemDecoration(ShoppingListDecoration())
            addOnScrollListener(loadMoreListener)
            setRecycledViewPool(mRecycledViewPool)
            setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
            recycledViewPool.setMaxRecycledViews(ShoppingListHorizontalProductCardItemViewHolder.LAYOUT, MAX_VIEW_POOL)
            itemAnimator = null
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupSwipeToRefreshLayout() {
        strRefreshLayout.setOnRefreshListener {
            refreshLayoutPage()
            strRefreshLayout.isEnabled = true
            strRefreshLayout.isRefreshing = false
            navToolbar.updateNotification()
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupFloatingActionButton() {
        fbuBackToTop.color = COLOR_WHITE
        fbuBackToTop.circleMainMenu.setOnClickListener {
            rvShoppingList.smoothScrollToPosition(Int.ZERO)
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupBottomBulkAtc() {
        bottomBulkAtcView.onAtcClickListener {
            viewModel.addMultiProductsToCart(
                onTrackAddToCartMulti = {
                    analytic.getShoppingListBottomBulkAtcAnalytic().trackClickAtcMiniCartShoppingList(it)
                }
            )
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupMiniCart() {
        val shopIds = listOf(viewModel.getShopId().toString())
        val pageName = MiniCartAnalytics.Page.HOME_PAGE
        val source = MiniCartSource.TokonowShoppingList
        miniCartWidget.initialize(
            shopIds = shopIds,
            fragment = this@TokoNowShoppingListFragment,
            listener = this@TokoNowShoppingListFragment,
            pageName = pageName,
            source = source
        )
    }

    private fun FragmentTokopedianowShoppingListBinding.setupNavigationToolbar() {
        navToolbar.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setupToolbarWithStatusBar(activity = requireActivity())
            setIcon(
                IconBuilder()
                    .addCart()
                    .addNavGlobal()
            )
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupOnScrollListener() {
        val callback = createNavRecyclerViewOnScrollCallback(this)
        rvShoppingList.addOnScrollListener(callback)
    }

    private fun FragmentTokopedianowShoppingListBinding.adjustRecyclerViewBottomPadding() {
        context?.apply {
            rvShoppingList.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, getBottomSpace())
            fbuBackToTop.setMargin(Int.ZERO, Int.ZERO, Int.ZERO, getBottomSpace())
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.hideMiniCart() {
        miniCartWidget.apply {
            hideCoachMark()
            hide()
        }
        adjustRecyclerViewBottomPadding()
    }

    private fun FragmentTokopedianowShoppingListBinding.showMiniCart(
        data: MiniCartSimplifiedData
    ) {
        if(data.isShowMiniCartWidget && !bottomBulkAtcView.isVisible) {
            miniCartWidget.show()
            miniCartWidget.updateData(data)
            adjustRecyclerViewBottomPadding()
        } else {
            hideMiniCart()
        }
    }

    private fun NavToolbar.setHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            setupSearchbar(
                hints = listOf(
                    HintData(
                        data.placeholder.orEmpty(),
                        data.keyword.orEmpty()
                    )
                ),
                searchbarClickCallback = {
                    RouteManager.route(
                        context,
                        ApplinkConstInternalDiscovery.AUTOCOMPLETE + ConstantKey.PARAM_APPLINK_AUTOCOMPLETE + "&" + "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}",
                        TokoNowHomeFragment.SOURCE,
                        context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty(),
                        isFirstInstall().toString()
                    )
                },
                searchbarImpressionCallback = {},
                shouldShowTransition = false
            )
        }
    }

    private fun IconBuilder.addNavGlobal(): IconBuilder = addIcon(
        iconId = IconList.ID_NAV_GLOBAL,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) { /* nothing to do */ }

    private fun IconBuilder.addCart(): IconBuilder = addIcon(
        iconId = IconList.ID_CART,
        disableRouteManager = false,
        disableDefaultGtmTracker = true
    ) { /* nothing to do */ }

    /**
     * -- callback function section --
     */

    private fun createHeaderCallback() = object : AbstractThematicHeaderListener() {
        override fun onClickCtaHeader() {
            analytic.trackClickBuyMoreOnTopNav()
            RouteManager.route(context, ApplinkConstInternalTokopediaNow.REPURCHASE)
        }

        override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView) {
            binding?.strRefreshLayout?.setContentChildViewPullRefresh(view)
        }
    }

    private fun createNavRecyclerViewOnScrollCallback(
        binding: FragmentTokopedianowShoppingListBinding
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = binding.navToolbar,
            startTransitionPixel = NavToolbarExt.getFullToolbarHeight(binding.navToolbar.context) - transitionRange,
            toolbarTransitionRangePixel = transitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) {
                    binding.apply {
                        if (isStickyTopCheckAllScrollingBehaviorEnabled && offsetAlpha > Int.ZERO && offsetAlpha <= TOP_CHECK_ALL_MAX_ALPHA) {
                            stickyTopCheckAllLayout.show()
                            stickyTopCheckAllLayout.background.alpha = offsetAlpha.toInt()
                            stickyTopCheckAll.tpTopCheckAll.alpha = offsetAlpha
                            stickyTopCheckAll.cbTopCheckAll.alpha = offsetAlpha
                        } else {
                            stickyTopCheckAllLayout.hide()
                        }
                    }
                }

                override fun onSwitchToLightToolbar() {
                    binding.apply {
                        if (isNavToolbarScrollingBehaviourEnabled && isLightToolbarSwitched) {
                            fbuBackToTop.show()
                            navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, (if (binding.navToolbar.context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                            navToolbar.setToolbarTitle(getString(R.string.tokopedianow_shopping_list_page_title))
                            switchToLightStatusBar()
                            pageCoachMark?.hide()
                            pageCoachMark = null
                            isLightToolbarSwitched = false
                        } else if (!isNavToolbarScrollingBehaviourEnabled) {
                            navToolbar.switchToDarkToolbar()
                        }
                    }
                }

                override fun onSwitchToDarkToolbar() {
                    binding.apply {
                        if (isNavToolbarScrollingBehaviourEnabled) {
                            fbuBackToTop.hide()
                            if (root.context.isDarkMode()) switchToLightStatusBar() else switchToDarkStatusBar()
                            navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, unifyprinciplesR.color.Unify_Static_White))
                            navToolbar.setToolbarTitle(String.EMPTY)
                            navToolbar.hideShadow()
                            isLightToolbarSwitched = true
                        } else {
                            navToolbar.switchToLightToolbar(force = true)
                        }
                    }
                }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }
            }
        )
    }

    private fun createHorizontalProductCardItemCallback() = object: AbstractShoppingListHorizontalProductCardItemListener() {
        override fun onSelectCheckbox(
            product: ShoppingListHorizontalProductCardItemUiModel,
            isSelected: Boolean
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackClickCheckboxOnProduct(product)
            viewModel.selectAvailableProduct(product.id, isSelected)
        }

        override fun onClickAnotherOption(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackClickAnotherOptionOnProduct(product)
            val bottomSheet = TokoNowShoppingListAnotherOptionBottomSheet.newInstance(product.id)
            bottomSheet.show(
                fm = childFragmentManager,
                availableProducts = viewModel.getAvailableProducts(),
                onClickCloseListener = {
                    refreshLayoutPage()
                }
            )
        }

        override fun onClickDeleteIcon(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackClickDeleteButtonOnProduct(product)
            viewModel.deleteFromWishlist(product)
        }

        override fun onClickAddToShoppingList(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackClickAddToShoppingListOnProduct(product)
            viewModel.addToWishlist(product)
        }

        override fun onClickProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackClickProduct(product)
        }

        override fun onImpressProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.getShoppingListHorizontalProductCardAnalytic().trackImpressProduct(product)
        }
    }

    private fun createRetryCallback() = object : ShoppingListRetryViewHolder.ShoppingListRetryListener {
        override fun onClickRetry() {
            viewModel.switchRetryToLoadMore()
            viewModel.loadMoreProductRecommendation(true)
        }
    }

    private fun createLoadMoreCallback() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisiblePosition = layoutManager?.findLastVisibleItemPosition()
            if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                val lastVisibleViewHolder = recyclerView.findViewHolderForAdapterPosition(lastVisiblePosition.orZero())
                viewModel.loadMoreProductRecommendation(
                    lastVisibleViewHolder is ShoppingListLoadingMoreViewHolder
                )
            }
        }
    }

    private fun createErrorCallback() = object : TokoNowErrorViewHolder.TokoNowErrorListener {
        override fun onClickRefresh() {
            refreshLayoutPage()
        }
    }

    private fun createExpandCollapseCallback() = object : ShoppingListExpandCollapseViewHolder.ShoppingListExpandCollapseListener {
        override fun onClickWidget(
            productState: ShoppingListProductState,
            productLayoutType: ShoppingListProductLayoutType
        ) {
            viewModel.expandCollapseShoppingList(
                productState = productState,
                productLayoutType = productLayoutType
            )
        }
    }

    private fun createTopCheckAllCallback() = object : ShoppingListTopCheckAllViewHolder.ShoppingListTopCheckAllListener {
        override fun onSelectCheckbox(
            productState: ShoppingListProductState,
            isSelected: Boolean
        ) {
            analytic.trackClickCheckboxTopCheckAll()
            viewModel.selectAllAvailableProducts(productState, isSelected)
        }
    }

    private fun createTopCheckAllCheckedChangeCallback() = CompoundButton.OnCheckedChangeListener { _, isSelected ->
        analytic.trackClickCheckboxTopCheckAll()
        viewModel.selectAllAvailableProducts(isSelected)
    }

    private fun createCartProductCallback() = object : ShoppingListCartProductViewHolder.ShoppingListCartProductListener {
        override fun onClickSeeDetail() {
            analytic.getShoppingListCartProductAnalytic().trackClickSeeDetail()
            binding?.miniCartWidget?.showMiniCartListBottomSheet(this@TokoNowShoppingListFragment)
        }

        override fun onImpressWidget() {
            analytic.getShoppingListCartProductAnalytic().trackImpressCartProductWidget()
        }
    }

    private fun createEmptyStateOocCallback() = object: AbstractEmptyStateOocListener() {
        override fun onRefreshLayoutPage() = refreshLayoutPage()

        override fun onGetFragmentManager(): FragmentManager = parentFragmentManager
    }

    private fun createProductRecommendationCallback() = object : AbstractProductRecommendationListener() {
        override fun onClickRecommendationCarouselItem(
            model: TokoNowProductRecommendationOocUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
        ) {
            openAppLink(recomItem.appUrl)
        }

        override fun onSeeMoreClick(
            data: RecommendationCarouselData,
            applink: String
        ) {
            openAppLink(applink)
        }

        override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
            lifecycle.addObserver(observer)
        }

        private fun openAppLink(appLink: String) {
            if(appLink.isNotEmpty()) RouteManager.route(context, appLink)
        }
    }

    private fun createLocalLoadCallback() = object : TokoNowLocalLoadViewHolder.TokoNowLocalLoadListener {
        override fun onClickRetry() {
            binding?.apply {
                rvShoppingList.removeOnScrollListener(loadMoreListener)
                rvShoppingList.addOnScrollListener(loadMoreListener)
                viewModel.retryFirstPageProductRecommendation()
            }
        }
    }

    /**
     * -- internal function section --
     */

    internal fun switchToDarkStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToDarkToolbar()

    internal fun switchToLightStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToLightToolbar()
}
