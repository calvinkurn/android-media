package com.tokopedia.tokomart.searchcategory.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_SHARE
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokomart.searchcategory.presentation.itemdecoration.ProductItemDecoration
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.unifycomponents.Toaster

abstract class BaseSearchCategoryFragment:
        BaseDaggerFragment(),
        ChooseAddressListener,
        BannerComponentListener,
        TitleListener,
        QuickFilterListener, SortFilterBottomSheet.Callback {

    companion object {
        protected const val DEFAULT_SPAN_COUNT = 2
    }

    protected var searchCategoryAdapter: SearchCategoryAdapter? = null
    protected var endlessScrollListener: EndlessRecyclerViewScrollListener? = null
    protected var sortFilterBottomSheet: SortFilterBottomSheet? = null

    protected var navToolbar: NavToolbar? = null
    protected var recyclerView: RecyclerView? = null

    protected abstract val toolbarPageName: String

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
        configureRecyclerView()
        observeViewModel()

        getViewModel().onViewCreated()
    }

    protected open fun findViews(view: View) {
        navToolbar = view.findViewById(R.id.tokonowSearchCategoryNavToolbar)
        recyclerView = view.findViewById(R.id.tokonowSearchCategoryRecyclerView)
    }

    protected open fun configureNavToolbar() {
        val navToolbar = navToolbar ?: return

        navToolbar.bringToFront()
        navToolbar.setToolbarPageName(toolbarPageName)
        navToolbar.setIcon(createNavToolbarIconBuilder())
    }

    protected abstract fun createNavToolbarIconBuilder(): IconBuilder

    protected fun IconBuilder.addShare(): IconBuilder = this
            .addIcon(ID_SHARE, disableRouteManager = false, disableDefaultGtmTracker = false) { }

    protected fun IconBuilder.addCart(): IconBuilder = this
            .addIcon(ID_CART, disableRouteManager = false, disableDefaultGtmTracker = false) { }

    protected fun IconBuilder.addGlobalNav(): IconBuilder = this
            .addIcon(ID_NAV_GLOBAL, disableRouteManager = false, disableDefaultGtmTracker = false) { }

    protected open fun configureRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, VERTICAL)
        endlessScrollListener = createEndlessScrollListener(staggeredGridLayoutManager)
        searchCategoryAdapter = SearchCategoryAdapter(createTypeFactory())

        recyclerView?.adapter = searchCategoryAdapter
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.addProductItemDecoration()

        endlessScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun createEndlessScrollListener(layoutManager: StaggeredGridLayoutManager) =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore()
                }
            }

    abstract fun createTypeFactory(): BaseSearchCategoryTypeFactory

    protected open fun RecyclerView.addProductItemDecoration() {
        try {
            val context = context ?: return
            val spacing = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)

            if (itemDecorationCount >= 1)
                invalidateItemDecorations()

            addItemDecoration(ProductItemDecoration(spacing))
        } catch (throwable: Throwable) {

        }
    }

    protected open fun observeViewModel() {
        getViewModel().visitableListLiveData.observe(viewLifecycleOwner, this::submitList)
        getViewModel().hasNextPageLiveData.observe(viewLifecycleOwner, this::updateEndlessScrollListener)
        getViewModel().isFilterPageOpenLiveData.observe(viewLifecycleOwner, this::openBottomSheetFilter)
        getViewModel().dynamicFilterModelLiveData.observe(
                viewLifecycleOwner, this::onDynamicFilterModelChanged)
    }

    abstract fun getViewModel(): BaseSearchCategoryViewModel

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

    override fun onLocalizingAddressSelected() {

    }

    override fun getFragment() = this

    override fun onSeeAllCategoryClicked() {

    }

    override fun onBannerClick(applink: String) {
        // TODO: Route to applink
        Toaster.build(requireView(),
                "Navigate to Applink",
                Toaster.TYPE_NORMAL,
                Toaster.LENGTH_SHORT)
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

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {

    }

    override fun getResultCount(mapParameter: Map<String, String>) {

    }

    private fun onDynamicFilterModelChanged(dynamicFilterModel: DynamicFilterModel?) {
        dynamicFilterModel?.let { it ->
            sortFilterBottomSheet?.setDynamicFilterModel(it)
        }
    }
}