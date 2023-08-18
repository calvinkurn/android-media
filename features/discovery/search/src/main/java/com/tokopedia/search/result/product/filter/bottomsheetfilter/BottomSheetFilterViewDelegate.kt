package com.tokopedia.search.result.product.filter.bottomsheetfilter

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.*
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.SortListener
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.filter.analytics.SearchSortFilterTracking
import com.tokopedia.search.result.product.lastfilter.LastFilterListener
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.componentIdMap
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.manualFilterToggleMap
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class BottomSheetFilterViewDelegate @Inject constructor(
    @SearchContext
    context: Context,
    fragmentProvider: FragmentProvider,
    searchParameterProvider: SearchParameterProvider,
    private val filterController: FilterController,
    private val parameterListener: ProductListParameterListener,
    private val lastFilterListener: LastFilterListener,
    private val screenNameProvider: ScreenNameProvider,
    private val userSessionInterface: UserSessionInterface,
) : BottomSheetFilterView,
    ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider,
    SearchParameterProvider by searchParameterProvider,
    ScreenNameProvider by screenNameProvider,
    Callback,
    LifecycleObserver {

    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private var callback: BottomSheetFilterCallback? = null

    private var sortBottomSheet: FilterGeneralDetailBottomSheet? = null

    private val pageSource: String by lazy {
        Dimension90Utils.getDimension90(getSearchParameter()?.getSearchParameterMap().orEmpty())
    }

    override fun sendTrackingOpenFilterPage() {
        SearchSortFilterTracking.eventOpenFilterPage()
    }

    override fun openBottomSheetFilter(
        dynamicFilterModel: DynamicFilterModel?,
        callback: BottomSheetFilterCallback,
    ) {
        if (!getFragment().isAdded) return
        this.callback = callback

        sortFilterBottomSheet = SortFilterBottomSheet().also {
            it.show(
                fragmentManager = getFragment().parentFragmentManager,
                mapParameter = getSearchParameter()?.getSearchParameterHashMap(),
                dynamicFilterModel = dynamicFilterModel,
                callback = this,
                isReimagine = true,
            )
        }
        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
            this.callback?.onBottomSheetFilterDismissed()
            this.callback = null
        }
    }

    override fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel) {
        val searchParameterMap = getSearchParameter()?.getSearchParameterHashMap() ?: mapOf()

        filterController.appendFilterList(searchParameterMap, dynamicFilterModel.data.filter)

        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
    }

    override fun openBottomSheetSort(
        dynamicFilterModel: DynamicFilterModel?,
        callback: BottomSheetFilterCallback,
    ) {
        if (!getFragment().isAdded) return
        val context = context ?: return
        this.callback = callback
        val listSort = dynamicFilterModel?.data?.sort
        val paramsQuery = getSearchParameter()?.getSearchParameterHashMap() ?: hashMapOf()
        val selectedSort = dynamicFilterModel?.findSelectedSort(paramsQuery)

        sortBottomSheet = FilterGeneralDetailBottomSheet().also {
            it.show(
                fragmentManager= getFragment().parentFragmentManager,
                sortOptions = listSort,
                selectedOption= selectedSort,
                callback= createSortListener(),
                titleBottomSheet= context.resources.getString(com.tokopedia.filter.R.string.title_sort_but),
                buttonApplyFilterDetailText= context.resources.getString(com.tokopedia.filter.R.string.dynamic_filter_finish_button_text),
                isNeedShowLoader = listSort.isNullOrEmpty()
            )
        }

        sortBottomSheet?.setOnDismissListener {
            sortBottomSheet = null
            this.callback?.onBottomSheetFilterDismissed()
            this.callback = null
        }
    }

    private fun createSortListener() = object : SortListener {
        override fun onSortOptionClick(sort: Sort) {
            callback?.getProductCount(createMapParameterSortFilter(sort))
        }

        override fun onApplySort(sort: Sort) {
            val mapParameter = createMapParameterSortFilter(sort)
            val applySortFilterModel = createApplySortFilterModel(sort)

            sortBottomSheet = null
            callback?.onApplySortFilter(mapParameter)
            onApplySortFilter(applySortFilterModel)
        }
    }

    private fun createMapParameterSortFilter(sort: Sort): HashMap<String, String>{
        val mapParameter = getSearchParameter()?.getSearchParameterHashMap() ?: hashMapOf()
        mapParameter[sort.key] = sort.value
        return mapParameter
    }
    private fun createApplySortFilterModel(sort: Sort): ApplySortFilterModel {
        val mapParameter = getSearchParameter()?.getSearchParameterHashMap() ?: hashMapOf()
        mapParameter[sort.key] = sort.value
        val selectedFilterMap = filterController.getActiveFilterMap()
        val selectedSortMap = mapSelectedSort(sort)
        val sortAutoFilterMap = getSortAutoFilterMap(sort)
        return ApplySortFilterModel(
            mapParameter,
            selectedFilterMap,
            selectedSortMap,
            sort.name,
            sortAutoFilterMap
        )
    }

    private fun mapSelectedSort(option: Sort): MutableMap<String, String> {
        val mapSort = mutableMapOf<String, String>()
        mapSort[option.key] = option.value
        return mapSort
    }

    private fun getSortAutoFilterMap(option: Sort): Map<String, String> {
        val activeFilterMap = filterController.getActiveFilterMap()
        val autoSort = option.applyFilter.toMapParam()
        val hasNoDuplicateFilter = autoSort.none { activeFilterMap.containsKey(it.key) }
        return if (hasNoDuplicateFilter) autoSort else emptyMap()
    }

    override fun setDynamicSort(dynamicFilterModel: DynamicFilterModel) {
        val paramsQuery = getSearchParameter()?.getSearchParameterHashMap() ?: hashMapOf()

        filterController.appendFilterList(paramsQuery, dynamicFilterModel.data.filter)

        sortBottomSheet?.setDynamicSortItem(dynamicFilterModel.data.sort, dynamicFilterModel.findSelectedSort(paramsQuery))
    }

    override fun onApplySortFilter(
        applySortFilterModel: ApplySortFilterModel,
    ) {
        callback?.onApplySortFilter(applySortFilterModel.mapParameter)

        sortFilterBottomSheet = null

        applySortAndFilter(applySortFilterModel)

        val requestParams = applySortFilterModel.mapParameter +
            manualFilterToggleMap() +
            componentIdMap(SearchSortFilterTracking.FILTER_COMPONENT_ID)

        parameterListener.refreshSearchParameter(requestParams)

        lastFilterListener.updateLastFilter()

        parameterListener.reloadData()
    }

    private fun applySortAndFilter(applySortFilterModel: ApplySortFilterModel) {
        SearchSortFilterTracking.eventApplyFilter(
            keyword = getSearchParameter()?.getSearchQuery().orEmpty(),
            pageSource = pageSource,
            selectedSort = applySortFilterModel.selectedSortMapParameter,
            selectedFilter = applySortFilterModel.selectedFilterMapParameter,
            sortApplyFilter = applySortFilterModel.sortAutoFilterMapParameter,
        )
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        callback?.getProductCount(mapParameter)
    }

    override fun setProductCount(productCountText: String?) {
        sortFilterBottomSheet?.setResultCountText(getFilterCountText(productCountText))
        sortBottomSheet?.setResultCountText(getFilterCountText(productCountText))
    }

    private fun getFilterCountText(productCountText: String?): String {
        val context = context ?: return ""
        return if (productCountText.isNullOrBlank()) {
            context.getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_no_count)
        } else {
            String.format(
                context.getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                productCountText
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        sortFilterBottomSheet = null
        sortBottomSheet = null
        callback = null
    }

}
