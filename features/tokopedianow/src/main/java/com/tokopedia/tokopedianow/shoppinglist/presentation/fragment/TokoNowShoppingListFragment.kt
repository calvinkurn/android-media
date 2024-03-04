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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.customview.pullrefresh.ParentIconSwipeRefreshLayout
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.productcard.compact.similarproduct.presentation.bottomsheet.ProductCardCompactSimilarProductBottomSheet
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowShoppingListBinding
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.presentation.activity.TokoNowShoppingListActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapterTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet.TokoNowShoppingListAnotherOptionBottomSheet
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
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
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListCartProductViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        private const val TOP_CHECK_ALL_THRESHOLD_ALPHA = 10f
        private const val TOP_CHECK_ALL_MAX_ALPHA = 255f

        fun newInstance(): TokoNowShoppingListFragment = TokoNowShoppingListFragment()
    }

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModel: TokoNowShoppingListViewModel

    /**
     * -- private variable section --
     */

    private val adapter: ShoppingListAdapter by lazy {
        ShoppingListAdapter(
            ShoppingListAdapterTypeFactory(
                tokoNowView = this@TokoNowShoppingListFragment,
                headerListener = createHeaderCallback(),
                chooseAddressListener = this@TokoNowShoppingListFragment,
                productCardItemListener = createHorizontalProductCardItemCallback(),
                retryListener = createRetryCallback(),
                errorListener = createErrorCallback(),
                expandCollapseListener = createExpandCollapseCallback(),
                topCheckAllListener = createTopCheckAllCallback(),
                cartProductListener = createCartProductCallback()
            )
        )
    }

    private val loadMoreListener: RecyclerView.OnScrollListener
        by lazy { createLoadMoreCallback() }

    private var binding: FragmentTokopedianowShoppingListBinding?
        by autoClearedNullable()

    private var layoutManager: LinearLayoutManager?
        by autoClearedNullable()

    private var isNavToolbarScrollingBehaviourEnabled: Boolean = true
    private var isStickyTopCheckAllScrollingBehaviorEnabled: Boolean = false
    private var loader: LoaderDialog? = null

    /**
     * -- override function section --
     */

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

        loadLayout()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun getFragmentPage(): Fragment = this@TokoNowShoppingListFragment

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) = viewModel.setMiniCartData(miniCartSimplifiedData)

    override fun refreshLayoutPage() {  }

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
                        viewModel.updateScrollState()
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
                bottomBulkAtcView.showWithCondition(isAvailable)
                adjustRecyclerViewBottomPadding()
            }
        }
    }

    private suspend fun collectBottomBulkAtc(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.bottomBulkAtcData.collect { model ->
            if (model != null) {
                binding.bottomBulkAtcView.bind(
                    counter = model.counter,
                    priceInt = model.price
                )
            }
        }
    }

    private suspend fun collectErrorNavToolbar(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isNavToolbarScrollingBehaviourEnabled.collect { isEnabled ->
            binding.navToolbar.apply {
                isNavToolbarScrollingBehaviourEnabled = isEnabled
                setShowShadowEnabled(!isEnabled)

                if (isEnabled) {
                    setToolbarContentType(TOOLBAR_TYPE_TITLE)
                    switchToDarkIcon()
                    switchToDarkStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White))
                    hideShadow()
                } else {
                    setToolbarContentType(TOOLBAR_TYPE_SEARCH)
                    switchToLightIcon()
                    switchToLightStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, (if (context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                    showShadow()
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

    private suspend fun collectToasterErrorShown() {
        viewModel.toasterData.collect { data ->
            if (data != null) {
                when(data.event) {
                    ADD_WISHLIST -> {
                        showToaster(data) {
                            viewModel.addToWishlist(data.product)
                        }
                    }
                    DELETE_WISHLIST -> {
                        showToaster(data) {
                            viewModel.deleteFromWishlist(data.product)
                        }
                    }
                }
            }
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
        clickListener: () -> Unit
    ) {
        view?.let { view ->
            if (model.text.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getBottomSpace()
                Toaster.build(
                    view = view,
                    text = model.text,
                    duration = model.duration,
                    type = model.type,
                    actionText = model.actionText,
                    clickListener = {
                        clickListener()
                    }
                ).show()
            }
        }
    }

    private fun getBottomSpace(): Int = if (binding?.miniCartWidget?.isVisible.orFalse() || binding?.bottomBulkAtcView?.isVisible.orFalse()) context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_bottom_view_height).orZero() else Int.ZERO

    /**
     * Create a new coroutine in the [lifecycleScope]. [repeatOnLifecycle] launches the block in a new coroutine
     * every time the lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
     */
    private fun FragmentTokopedianowShoppingListBinding.collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                /**
                 * Because [collect] is a suspend function, need different coroutines to collect multiple flows in parallel.
                 * The suspending function suspends until the Flow terminates.
                 */
                launch { collectLayoutState(this@collectStateFlow) }
                launch { collectMiniCartState(this@collectStateFlow) }
                launch { collectScrollBehavior(this@collectStateFlow) }
                launch { collectErrorNavToolbar(this@collectStateFlow) }
                launch { collectTopCheckAllSelected(this@collectStateFlow) }
                launch { collectProductAvailability(this@collectStateFlow) }
                launch { collectBottomBulkAtc(this@collectStateFlow) }
                launch { collectLoaderDialogShown() }
                launch { collectToasterErrorShown() }
            }
        }
    }

    private fun loadLayout() = viewModel.loadLayout()

    private fun FragmentTokopedianowShoppingListBinding.setupRecyclerView() {
        layoutManager = LinearLayoutManager(context)
        rvShoppingList.apply {
            adapter = this@TokoNowShoppingListFragment.adapter
            layoutManager = this@TokoNowShoppingListFragment.layoutManager
            addItemDecoration(ShoppingListDecoration())
            addOnScrollListener(loadMoreListener)
            itemAnimator = null
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupSwipeToRefreshLayout() {
        strRefreshLayout.setOnRefreshListener {
            viewModel.refreshLayout()
            strRefreshLayout.isEnabled = true
            strRefreshLayout.isRefreshing = false
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupFloatingActionButton() {
        fbuBackToTop.color = COLOR_WHITE
        fbuBackToTop.circleMainMenu.setOnClickListener {
            rvShoppingList.smoothScrollToPosition(Int.ZERO)
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupBottomBulkAtc() {
        bottomBulkAtcView.onAtcClickListener {}
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

    private fun createHeaderCallback() = object : TokoNowThematicHeaderViewHolder.TokoNowHeaderListener {
        override fun onClickCtaHeader() {
            RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.REPURCHASE
            )
        }

        override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView) {
            getRefreshLayout()?.setContentChildViewPullRefresh(view)
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
                        if (isStickyTopCheckAllScrollingBehaviorEnabled) {
                            stickyTopCheckAllLayout.showIfWithBlock(offsetAlpha > TOP_CHECK_ALL_THRESHOLD_ALPHA) {
                                stickyTopCheckAllLayout.background.alpha = offsetAlpha.toInt()
                                stickyTopCheckAll.tpTopCheckAll.visibleWithCondition(offsetAlpha == TOP_CHECK_ALL_MAX_ALPHA)
                                stickyTopCheckAll.cbTopCheckAll.visibleWithCondition(offsetAlpha == TOP_CHECK_ALL_MAX_ALPHA)
                            }
                        } else {
                            stickyTopCheckAllLayout.hide()
                        }
                    }
                }

                override fun onSwitchToLightToolbar() {
                    binding.apply {
                        fbuBackToTop.show()
                        if (isNavToolbarScrollingBehaviourEnabled) {
                            navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, (if (binding.navToolbar.context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                            navToolbar.setToolbarTitle(getString(R.string.tokopedianow_shopping_list_page_title))
                            switchToLightStatusBar()
                        }
                    }
                }

                override fun onSwitchToDarkToolbar() {
                    binding.apply {
                        fbuBackToTop.hide()
                        if (isNavToolbarScrollingBehaviourEnabled) {
                            if (root.context.isDarkMode()) switchToLightStatusBar() else switchToDarkStatusBar()
                            navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, unifyprinciplesR.color.Unify_Static_White))
                            navToolbar.setToolbarTitle(String.EMPTY)
                            navToolbar.hideShadow()
                        }
                    }
                }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }
            }
        )
    }

    private fun createHorizontalProductCardItemCallback() = object : ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener {
        override fun onSelectCheckbox(
            productId: String,
            isSelected: Boolean
        ) {
            viewModel.selectAvailableProduct(productId, isSelected)
        }

        override fun onClickOtherOptions(
            productId: String
        ) {
            val bottomSheet = TokoNowShoppingListAnotherOptionBottomSheet.newInstance(productId)
            bottomSheet.show(
                fm = childFragmentManager,
                availableProducts = viewModel.getAvailableProducts()
            )
        }

        override fun onClickDeleteIcon(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            viewModel.deleteFromWishlist(product)
        }

        override fun onClickAddToShoppingList(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            viewModel.addToWishlist(product)
        }
    }

    private fun createRetryCallback() = object : ShoppingListRetryViewHolder.ShoppingListRetryListener {
        override fun onClickRetry() {
            viewModel.switchRetryToLoadMore()
            viewModel.loadMoreProductRecommendation(true)
        }
    }

    private fun createLoadMoreCallback()  = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisiblePosition = getLayoutManager()?.findLastVisibleItemPosition()
            if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                val lastVisibleViewHolder = recyclerView.findViewHolderForAdapterPosition(lastVisiblePosition.orZero())
                viewModel.loadMoreProductRecommendation(
                    lastVisibleViewHolder is TokoNowLoadingMoreViewHolder
                )
            }
        }
    }

    private fun createErrorCallback() = object : TokoNowErrorViewHolder.TokoNowErrorListener {
        override fun onClickRefresh() {
            viewModel.refreshLayout()
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
            viewModel.selectAllAvailableProducts(productState, isSelected)
        }
    }

    private fun createTopCheckAllCheckedChangeCallback() = CompoundButton.OnCheckedChangeListener { _, isSelected ->
        viewModel.selectAllAvailableProducts(isSelected)
    }

    private fun createCartProductCallback() = object : ShoppingListCartProductViewHolder.ShoppingListCartProductListener {
        override fun onClickSeeDetail() {
            getMiniCart()?.showMiniCartListBottomSheet(this@TokoNowShoppingListFragment)
        }
    }

    /**
     * -- internal function section --
     */

    internal fun switchToDarkStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToDarkToolbar()

    internal fun switchToLightStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToLightToolbar()

    internal fun getRefreshLayout(): ParentIconSwipeRefreshLayout? = binding?.strRefreshLayout

    internal fun getMiniCart(): MiniCartWidget? = binding?.miniCartWidget

    internal fun getLayoutManager(): LinearLayoutManager? = layoutManager
}
