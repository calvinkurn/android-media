package com.tokopedia.tokopedianow.shoppinglist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.customview.pullrefresh.ParentIconSwipeRefreshLayout
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.compact.similarproduct.presentation.bottomsheet.ProductCardCompactSimilarProductBottomSheet
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowShoppingListBinding
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.activity.TokoNowShoppingListActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListAdapterTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet.TokoNowShoppingListAnotherOptionBottomSheet
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.searchbar.R as searchbarR

class TokoNowShoppingListFragment :
    Fragment(),
    TokoNowView,
    TokoNowChooseAddressWidgetListener
{
    companion object {
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
                shoppingListHorizontalProductCardItemListener = createHorizontalProductCardItemCallback()
            )
        )
    }

    private val loadMoreListener: RecyclerView.OnScrollListener
        by lazy { createLoadMoreListener() }

    private var binding: FragmentTokopedianowShoppingListBinding?
        by autoClearedNullable()

    private var layoutManager: LinearLayoutManager?
        by autoClearedNullable()

    /**
     * -- internal variable section --
     */

    internal val strRefreshLayout: ParentIconSwipeRefreshLayout?
        get() = binding?.strRefreshLayout

    internal val navToolbar: NavToolbar?
        get() = binding?.navToolbar

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
            observeLiveData()

            setupRecyclerView()
            setupNavigationToolbar()
            setupOnScrollListener()

            loadFirstPage()
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

    private fun FragmentTokopedianowShoppingListBinding.loadFirstPage() {
        navToolbar.post {
            setHeaderSpace(navToolbar)
            setHeaderModel(navToolbar.context)
            viewModel.loadLayout()
        }
    }

    private fun setHeaderSpace(
        navToolbar: NavToolbar
    ) {
        val navToolbarHeight = if (navToolbar.height.isZero()) context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero() else navToolbar.height
        viewModel.headerSpace = navToolbarHeight
    }

    private fun setHeaderModel(context: Context) {
        viewModel.headerModel = HeaderModel(
            pageTitle = getString(R.string.tokopedianow_shopping_list_page_title),
            pageTitleColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            ctaText = getString(R.string.tokopedianow_shopping_list_repurchase),
            ctaTextColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            ctaChevronIsShown = true,
            ctaChevronColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White),
            backgroundGradientColor = TokoNowHeaderUiModel.GradientColor(
                startColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN500),
                endColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN400)
            )
        )
    }

    private fun FragmentTokopedianowShoppingListBinding.setupRecyclerView() {
        layoutManager = LinearLayoutManager(context)
        rvShoppingList.apply {
            adapter = this@TokoNowShoppingListFragment.adapter
            layoutManager = this@TokoNowShoppingListFragment.layoutManager
            addItemDecoration(ShoppingListDecoration())
            addOnScrollListener(loadMoreListener)
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
            bringToFront()
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.observeLiveData() {
        viewModel.layout.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isOnScrollNotNeeded.observe(viewLifecycleOwner) {
            rvShoppingList.removeOnScrollListener(loadMoreListener)
        }
    }

    private fun FragmentTokopedianowShoppingListBinding.setupOnScrollListener() {
        val callback = createNavRecyclerViewOnScrollCallback(navToolbar)
        rvShoppingList.addOnScrollListener(callback)
    }

    private fun getNavToolbarHeight(navToolbar: NavToolbar): Int {
        val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
        return if (navToolbar.height.isZero()) defaultHeight else navToolbar.height
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
     * -- internal function section --
     */

    internal fun switchToDarkStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToDarkToolbar()

    internal fun switchToLightStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToLightToolbar()

    internal fun NavToolbar.setBackButtonColor(color: Int) = setCustomBackButton(color = ContextCompat.getColor(context, color))

    /**
     * -- callback function section --
     */
    private fun createHeaderCallback() = object : TokoNowHeaderViewHolder.TokoNowHeaderListener {
        override fun onClickCtaHeader() {
            RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.REPURCHASE
            )
        }

        override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView) {
            strRefreshLayout?.setContentChildViewPullRefresh(view)
        }
    }

    private fun createNavRecyclerViewOnScrollCallback(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = getNavToolbarHeight(navToolbar) - transitionRange - transitionRange,
            toolbarTransitionRangePixel = transitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }

                override fun onSwitchToLightToolbar() {
                    navToolbar.setBackButtonColor(if (navToolbar.context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)
                    switchToLightStatusBar()
                    navToolbar.setToolbarTitle(getString(R.string.tokopedianow_shopping_list_page_title))
                }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }

                override fun onSwitchToDarkToolbar() {
                    if (navToolbar.context.isDarkMode()) {
                        navToolbar.setBackButtonColor(unifyprinciplesR.color.Unify_Static_White)
                        switchToLightStatusBar()
                    } else {
                        navToolbar.setBackButtonColor(unifyprinciplesR.color.Unify_Static_White)
                        switchToDarkStatusBar()
                    }
                    navToolbar.setToolbarTitle(String.EMPTY)
                    navToolbar.hideShadow()
                }
            }
        )
    }

    private fun createHorizontalProductCardItemCallback() = object : ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener{
        override fun onClickOtherOptions() {
            val bottomSheet = TokoNowShoppingListAnotherOptionBottomSheet.newInstance("12514021813")
            bottomSheet.show(childFragmentManager, ProductCardCompactSimilarProductBottomSheet::class.java.simpleName)
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
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

    override fun getFragmentPage(): Fragment = this@TokoNowShoppingListFragment

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() {  }

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { }

    override fun onChooseAddressWidgetRemoved() { }

    override fun onClickChooseAddressWidgetTracker() { }
}
