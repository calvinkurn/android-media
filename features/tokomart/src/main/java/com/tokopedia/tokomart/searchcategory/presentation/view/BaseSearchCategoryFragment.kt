package com.tokopedia.tokomart.searchcategory.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_SHARE
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.presentation.adapter.SearchCategoryAdapter
import com.tokopedia.tokomart.searchcategory.presentation.itemdecoration.ProductItemDecoration
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener

abstract class BaseSearchCategoryFragment:
        BaseDaggerFragment(),
        ChooseAddressListener {

    companion object {
        protected const val DEFAULT_SPAN_COUNT = 2
    }

    private var searchCategoryAdapter: SearchCategoryAdapter? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

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
    }

    abstract fun getViewModel(): BaseSearchCategoryViewModel

    protected open fun submitList(visitableList: List<Visitable<*>>) {
        if (visitableList.isNotEmpty())
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
}