package com.tokopedia.discovery.categoryrevamp.view.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.manager.FilterSortManager
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.newdynamicfilter.helper.SortHelper
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import java.util.*
import kotlin.collections.HashMap

private const val LANDSCAPE_COLUMN_MAIN = 3
private const val PORTRAIT_COLUMN_MAIN = 2

abstract class BaseCategorySectionFragment : BaseDaggerFragment() {

    private lateinit var categoryNavigationListener: CategoryNavigationListener
    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var spanCount: Int = 0

    protected val filterController: FilterController? = FilterController()
    protected var searchParameter: SearchParameter = SearchParameter()

    private var sort: ArrayList<Sort> = ArrayList()
    private var filters: ArrayList<Filter> = ArrayList()

    private var selectedSort: HashMap<String, String> = HashMap()

    private var filterTrackingData: FilterTrackingData? = null

    private var totalCount = ""
    var totalCountInt = 0


    private var bottomSheetListener: BottomSheetListener? = null
    private var sortAppliedListener: SortAppliedListener? = null

    companion object {
        const val DEFAULT_SORT = 23
    }

    protected open fun onSwipeToRefresh() {
    }

    protected fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    fun hideRefreshLayout() {
        refreshLayout?.isRefreshing = false
    }

    protected abstract fun getAdapter(): BaseCategoryAdapter?

    abstract fun reloadData()
    abstract fun getDepartMentId(): String
    abstract fun onShareButtonClicked()
    protected abstract fun getFilterRequestCode(): Int


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpan()
        initLayoutManager()
        initSwipeToRefresh()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CategoryNavigationListener) {
            categoryNavigationListener = context
        }
        if (context != null) {
            setSortListener(context)
        }
        if (context is BottomSheetListener) {
            this.bottomSheetListener = context
        }
    }

    private fun initSwipeToRefresh() {
        refreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        refreshLayout?.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }

    protected fun getGridLayoutManager(): GridLayoutManager? {
        return gridLayoutManager
    }

    protected fun getLinearLayoutManager(): LinearLayoutManager? {
        return linearLayoutManager
    }

    protected fun getStaggeredGridLayoutManager(): StaggeredGridLayoutManager? {
        return staggeredGridLayoutManager
    }

    protected abstract fun getSortRequestCode(): Int

    private fun initLayoutManager() {
        linearLayoutManager = LinearLayoutManager(activity)
        gridLayoutManager = GridLayoutManager(activity, spanCount)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initSpan() {
        spanCount = calcColumnSize(resources.configuration.orientation)
    }


    private fun calcColumnSize(orientation: Int): Int {
        var defaultColumnNumber = 1
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> defaultColumnNumber = PORTRAIT_COLUMN_MAIN
            Configuration.ORIENTATION_LANDSCAPE -> defaultColumnNumber = LANDSCAPE_COLUMN_MAIN
        }
        return defaultColumnNumber
    }

    protected fun setUpNavigation() {
        categoryNavigationListener.setupSearchNavigation(object : CategoryNavigationListener.ClickListener {

            override fun onChangeGridClick() {
                switchLayoutType()
            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) {
            setUpVisibleFragmentListener()
        }
    }

    protected fun setUpVisibleFragmentListener() {
        setTotalSearchResultCount(totalCount)
        categoryNavigationListener.setUpVisibleFragmentListener(object : CategoryNavigationListener.VisibleClickListener {
            override fun onShareButtonClick() {
                onShareButtonClicked()
            }

            override fun onFilterClick() {
                openFilterActivity()
            }

            override fun onSortClick() {
                openSortActivity()
            }
        })
    }

    private fun openFilterActivity() {
        if (!isFilterDataAvailable() && activity != null) {
            NetworkErrorHelper.showSnackbar(activity, activity!!.getString(R.string.error_filter_data_not_ready))
            return
        }

        if (bottomSheetListener != null) {
            openBottomSheetFilter()
        } else {
            openFilterPage()
        }
    }

    protected fun openBottomSheetFilter() {
        if (searchParameter == null || getFilters() == null) return

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        bottomSheetListener?.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap())
        bottomSheetListener?.launchFilterBottomSheet()
    }

    protected fun openFilterPage() {
        if (searchParameter == null) return
        FilterSortManager.openFilterPage(getFilterTrackingData(), this, screenName, searchParameter.getSearchParameterHashMap())
    }

    private fun isFilterDataAvailable(): Boolean {
        return filters != null && !filters.isEmpty()
    }

    private fun openSortActivity() {
        if (activity == null) return
        if (!FilterSortManager.openSortActivity(this, sort, selectedSort)) {
            NetworkErrorHelper.showSnackbar(activity, activity!!.getString(R.string.error_sort_data_not_ready))
        }
    }

    protected fun renderDynamicFilter(data: DataValue) {
        clearDataFilterSort()
        setFilterData(data.filter)
        setSortData(data.sort)
        if (filterController == null || searchParameter == null
                || getFilters() == null || sort == null)
            return

        val initializedFilterList = FilterHelper.initializeFilterList(getFilters())
        filterController?.initFilterController(searchParameter?.getSearchParameterHashMap(), initializedFilterList)
        initSelectedSort()
        updateDimension()
    }

    private fun showSortTickIfSelected(): Boolean {
        var toShow = false
        if (selectedSort.size == 1 && DEFAULT_SORT != selectedSort["ob"]?.toInt())
            for (items in sort) {
                if (items.key == selectedSort.keys.elementAt(0) && items.value == selectedSort.getValue(items.key)) {
                    toShow = true
                    break
                }
            }
        return toShow
    }

    private fun setSortData(sorts: List<Sort>) {
        this.sort = ArrayList()
        if (sorts == null) {
            return
        }
        this.sort.addAll(sorts)
    }

    private fun setFilterData(filters: List<Filter>) {
        this.filters = ArrayList()
        if (filters == null) {
            return
        }
        this.filters.addAll(filters)
    }

    protected fun getFilters(): ArrayList<Filter>? {
        return filters
    }

    private fun switchLayoutType() {
        switchCatalogcategory()
    }

    fun applyFilterToSearchParameter(filterParameter: Map<String, String>) {
        if (searchParameter == null) return

        this.searchParameter.getSearchParameterHashMap().clear()
        this.searchParameter.getSearchParameterHashMap().putAll(filterParameter)
    }

    fun setSelectedFilter(selectedFilter: HashMap<String, String>) {
        if (filterController == null || getFilters() == null) return
        val initializedFilterList = FilterHelper.initializeFilterList(getFilters())
        filterController.initFilterController(selectedFilter, initializedFilterList)
        updateDimension()

    }

    fun updateDimension() {
        var param = ""
        val filterParam = createParametersForQuery(getSelectedFilter())
        val sortParam = createParametersForQuery(getSelectedSort())

        if (filterParam.isNotEmpty()) {
            param = filterParam
        }

        if (sortParam.isNotEmpty()) {
            if (param.isNotEmpty()) {
                param = "$param&$sortParam"
            } else {
                param = sortParam
            }
        }
        getAdapter()?.setDimension(param)
    }

    protected fun addQuickFilter(option: Option, state: Boolean) {
        filterController?.setFilter(option, state)
    }

    fun getSelectedFilter(): HashMap<String, String> {
        return if (filterController == null) HashMap() else HashMap(filterController.getActiveFilterMap())

    }

    private fun initSelectedSort() {
        if (sort == null) return

        val selectedSort = HashMap(
                SortHelper.getSelectedSortFromSearchParameter(searchParameter.getSearchParameterHashMap(), sort)
        )
        addDefaultSelectedSort(selectedSort)
        setSelectedSort(selectedSort)
    }

    private fun setSelectedSort(selected_Sort: HashMap<String, String>) {
        selectedSort = selected_Sort
    }

    protected fun getSelectedSort(): HashMap<String, String> {
        return selectedSort
    }


    private fun addDefaultSelectedSort(selectedSort: HashMap<String, String>) {
        if (selectedSort.isEmpty()) {
            selectedSort[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        }
    }


    private fun switchCatalogcategory() {
        when (getAdapter()?.getCurrentLayoutType()) {
            CategoryNavConstants.RecyclerView.GridType.GRID_1 -> {
                spanCount = 1
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeSingleGridView()
            }
            CategoryNavConstants.RecyclerView.GridType.GRID_2 -> {
                spanCount = 1
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeListView()
            }
            CategoryNavConstants.RecyclerView.GridType.GRID_3 -> {
                spanCount = 2
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeDoubleGridView()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FilterSortManager.handleOnActivityResult(requestCode, resultCode, data, object : FilterSortManager.Callback {
            override fun onFilterResult(queryParams: Map<String, String>?, selectedFilters: Map<String, String>?, selectedOptions: List<Option>?) {

            }

            override fun onSortResult(selectedSort: Map<String, String>?, selectedSortName: String?, autoApplyFilter: String?) {
                setSelectedSort(HashMap(selectedSort?.toMutableMap() ?: mutableMapOf()))
                selectedSort?.let {
                    searchParameter.getSearchParameterHashMap().putAll(it)
                }
                reloadData()
                sortAppliedListener?.onSortApplied(DEFAULT_SORT != selectedSort?.get("ob")?.toInt())
                onSortAppliedEvent(selectedSortName ?: "",
                        selectedSort?.get("ob")?.toInt() ?: 0)
            }
        })
    }

    abstract fun onSortAppliedEvent(selectedSortName: String, sortValue: Int)

    fun clearDataFilterSort() {
        if (filters != null) {
            this.filters.clear()
        }
        if (sort != null) {
            this.sort.clear()
        }
    }

    private fun getMapFromIntent(data: Intent?, extraName: String): Map<String, String> {
        val serializableExtra = data?.getSerializableExtra(extraName) ?: return HashMap()

        val filterParameterMapIntent = data?.getSerializableExtra(extraName) as Map<*, *>
        val filterParameter = HashMap<String, String>(filterParameterMapIntent.size)

        for ((key, value) in filterParameterMapIntent) {
            filterParameter[key.toString()] = value.toString()
        }

        return filterParameter
    }

    fun setTotalSearchResultCount(formattedResultCount: String) {
        totalCount = formattedResultCount
        if (bottomSheetListener != null) {
            bottomSheetListener?.setFilterResultCount(formattedResultCount)
        }
    }

    fun setTotalSearchResultCountInteger(count: Int?) {
        if (count != null)
            totalCountInt = count
    }

    fun onBottomSheetHide() {
        FilterTracking.eventApplyFilter(getFilterTrackingData(), screenName, getSelectedFilter())
    }

    private fun getFilterTrackingData(): FilterTrackingData {
        if (filterTrackingData == null) {
            filterTrackingData = FilterTrackingData(FilterEventTracking.Event.CLICK_CATEGORY,
                    FilterEventTracking.Category.FILTER_CATEGORY,
                    getDepartMentId(),
                    FilterEventTracking.Category.PREFIX_CATEGORY_PAGE
            )
        }
        return filterTrackingData!!
    }


    fun setSortListener(context: Context?) {
        this.sortAppliedListener = context as SortAppliedListener
    }

    interface SortAppliedListener {
        fun onSortApplied(showTick: Boolean)
    }

    fun resetSortTick() {
        sortAppliedListener?.onSortApplied(showSortTickIfSelected())
    }

    protected fun removeSelectedFilter(uniqueId: String) {
        if (filterController == null) return

        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)

        removeFilterFromFilterController(option)
        refreshSearchParameter(filterController.getParameter())
        refreshFilterController(HashMap(filterController.getParameter()))
        reloadData()
    }

    fun removeFilterFromFilterController(option: Option) {
        if (filterController == null) return

        val optionKey = option.key

        if (Option.KEY_CATEGORY == optionKey) {
            filterController.setFilter(option, false, true)
        } else if (Option.KEY_PRICE_MIN == optionKey || Option.KEY_PRICE_MAX == optionKey) {
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MIN), false, true)
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MAX), false, true)
        } else {
            filterController.setFilter(option, false)
        }
    }

    private fun generatePriceOption(priceOptionKey: String): Option {
        val option = Option()
        option.key = priceOptionKey
        return option
    }

    fun refreshSearchParameter(queryParams: Map<String, String>) {
        if (searchParameter == null) return

        this.searchParameter.getSearchParameterHashMap().clear()
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams)
        this.searchParameter.getSearchParameterHashMap()[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
    }

    fun refreshFilterController(queryParams: java.util.HashMap<String, String>) {
        if (filterController == null || getFilters() == null) return

        val params = java.util.HashMap(queryParams)
        params[SearchApiConst.ORIGIN_FILTER] = SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE

        val initializedFilterList = FilterHelper.initializeFilterList(getFilters())
        filterController.initFilterController(params, initializedFilterList)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }
}