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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.productcard.compact.similarproduct.presentation.bottomsheet.ProductCardCompactSimilarProductBottomSheet
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
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
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.activity.TokoNowShoppingListActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapterTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet.TokoNowShoppingListAnotherOptionBottomSheet
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListExpandCollapseViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListRetryViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListTopCheckAllViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.searchbar.R as searchbarR

class TokoNowShoppingListFragment :
    Fragment(),
    TokoNowView,
    TokoNowChooseAddressWidgetListener
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
                topCheckAllListener = createTopCheckAllCallback()
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
            setupNavigationToolbar()
            setupOnScrollListener()

            loadLayout()
        }
    }

    override fun getFragmentPage(): Fragment = this@TokoNowShoppingListFragment

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() {  }

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { }

    override fun onChooseAddressWidgetRemoved() { }

    override fun onClickChooseAddressWidgetTracker() { }

    /**
     * -- private suspend function section --
     */

    private suspend fun collectUiState(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.uiState.collect { uiState ->
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

    private suspend fun collectScrollState(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isOnScrollNotNeeded.collect { isNotNeededToScroll ->
            if (isNotNeededToScroll) binding.rvShoppingList.removeOnScrollListener(loadMoreListener)
        }
    }

    private suspend fun collectStickyTopCheckAllStatus(
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

    private suspend fun collectStickyTopCheckAllScrollingBehaviour() {
        viewModel.isStickyTopCheckAllScrollingBehaviourEnabled.collect { isEnabled ->
            isStickyTopCheckAllScrollingBehaviorEnabled = isEnabled
        }
    }

    private suspend fun collectErrorNavToolbar(
        binding: FragmentTokopedianowShoppingListBinding
    ) {
        viewModel.isNavToolbarScrollingBehaviourEnabled.collect { isEnabled ->
            binding.navToolbar.apply {
                isNavToolbarScrollingBehaviourEnabled = isEnabled

                if (isEnabled) {
                    setToolbarContentType(TOOLBAR_TYPE_TITLE)
                    setShowShadowEnabled(false)
                    switchToDarkIcon()
                    switchToDarkStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White))
                    hideShadow()
                } else {
                    setToolbarContentType(TOOLBAR_TYPE_SEARCH)
                    setShowShadowEnabled(true)
                    switchToLightIcon()
                    switchToLightStatusBar()
                    setCustomBackButton(color = ContextCompat.getColor(context, (if (context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                    showShadow()
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

    private fun setHeaderModel(context: Context) {
        viewModel.headerModel = HeaderModel(
            pageTitle = getString(R.string.tokopedianow_shopping_list_page_title),
            pageTitleColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            ctaText = getString(R.string.tokopedianow_shopping_list_repurchase),
            ctaTextColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            ctaChevronIsShown = true,
            ctaChevronColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            backgroundGradientColor = TokoNowThematicHeaderUiModel.GradientColor(
                startColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN500),
                endColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN400)
            ),
            emptyStockTitle = getString(R.string.tokopedianow_shopping_list_empty_stock)
        )
    }

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
                launch { collectUiState(this@collectStateFlow) }
                launch { collectScrollState(this@collectStateFlow) }
                launch { collectErrorNavToolbar(this@collectStateFlow) }
                launch { collectStickyTopCheckAllStatus(this@collectStateFlow) }
                launch { collectStickyTopCheckAllScrollingBehaviour() }
            }
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.loadLayout() {
        setHeaderModel(navToolbar.context)
        viewModel.loadLayout()
    }

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
                    if (isNavToolbarScrollingBehaviourEnabled) {
                        binding.navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, (if (binding.navToolbar.context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)))
                        switchToLightStatusBar()
                        binding.navToolbar.setToolbarTitle(getString(R.string.tokopedianow_shopping_list_page_title))
                    }
                }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }

                override fun onSwitchToDarkToolbar() {
                    if (isNavToolbarScrollingBehaviourEnabled) {
                        if (binding.navToolbar.context.isDarkMode()) {
                            switchToLightStatusBar()
                        } else {
                            switchToDarkStatusBar()
                        }
                        binding.navToolbar.setCustomBackButton(color = ContextCompat.getColor(binding.root.context, unifyprinciplesR.color.Unify_Static_White))
                        binding.navToolbar.setToolbarTitle(String.EMPTY)
                        binding.navToolbar.hideShadow()
                    }
                }
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
            bottomSheet.show(childFragmentManager, ProductCardCompactSimilarProductBottomSheet::class.java.simpleName)
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
            val lastVisiblePosition = layoutManager?.findLastVisibleItemPosition()
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

    /**
     * -- internal function section --
     */

    internal fun switchToDarkStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToDarkToolbar()

    internal fun switchToLightStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToLightToolbar()
}
