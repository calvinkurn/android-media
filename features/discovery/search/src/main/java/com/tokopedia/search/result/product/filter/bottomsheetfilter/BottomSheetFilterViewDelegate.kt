package com.tokopedia.search.result.product.filter.bottomsheetfilter

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.common.data.SortModel
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
import com.tokopedia.filter.R as filterR

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
    SortFilterBottomSheet.Callback,
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

    override fun openBottomSheetSort(
        dynamicFilterModel: DynamicFilterModel?,
        callback: BottomSheetFilterCallback
    ) {
        if (!getFragment().isAdded) return
        this.callback = callback

        sortBottomSheet = FilterGeneralDetailBottomSheet().also {
            it.show(
                fragmentManager = getFragment().parentFragmentManager,
                filter = createSortModel(dynamicFilterModel),
                optionCallback = createSortListener(),
                enableResetButton = false,
            )
        }

        sortBottomSheet?.setOnDismissListener {
            sortBottomSheet = null
            this.callback?.onBottomSheetFilterDismissed()
            this.callback = null
        }
    }

    private fun createSortModel(dynamicFilterModel: DynamicFilterModel?): SortModel? {
        dynamicFilterModel ?: return null

        val title = context?.resources?.getString(filterR.string.title_sort_but) ?: ""

        return SortModel(dynamicFilterModel.data.sort, title)
    }

    private fun createSortListener() = object : FilterGeneralDetailBottomSheet.OptionCallback {
        override fun onApplyButtonClicked(optionList: List<IOption>?) {
            val selectedSort = getSelectedOption(optionList) ?: return

            sortBottomSheet = null

            val parameter = filterController.getParameter() +
                selectedSort.toMapParam() +
                manualFilterToggleMap() +
                componentIdMap(SearchSortFilterTracking.FILTER_COMPONENT_ID)

            applyParameter(parameter)
        }
    }

    private fun getSelectedOption(optionList: List<IOption>?): Sort? =
        optionList?.firstOrNull { it.inputState.toBoolean() } as? Sort

    private fun applyParameter(parameter: Map<String, String>) {
        parameterListener.refreshSearchParameter(parameter)

        lastFilterListener.updateLastFilter()

        parameterListener.reloadData()
    }

    override fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel) {
        val searchParameterMap = getSearchParameter()?.getSearchParameterHashMap() ?: mapOf()

        filterController.appendFilterList(searchParameterMap, dynamicFilterModel.data.filter)

        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)

        applyActiveSort(dynamicFilterModel)
        sortBottomSheet?.setOptionHolder(createSortModel(dynamicFilterModel))
    }

    private fun applyActiveSort(dynamicFilterModel: DynamicFilterModel) {
        dynamicFilterModel.data.sort.forEach {
            it.inputState = (getSearchParameter()?.get(it.key) == it.value).toString()
        }
    }

    override fun onApplySortFilter(
        applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel,
    ) {
        callback?.onApplySortFilter(applySortFilterModel.mapParameter)

        sortFilterBottomSheet = null

        trackApplyFilterAndSort(applySortFilterModel)

        val parameter = applySortFilterModel.mapParameter +
            manualFilterToggleMap() +
            componentIdMap(SearchSortFilterTracking.FILTER_COMPONENT_ID)

        applyParameter(parameter)
    }

    private fun trackApplyFilterAndSort(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
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
    }

    private fun getFilterCountText(productCountText: String?): String {
        val context = context ?: return ""
        return if (productCountText.isNullOrBlank()) {
            context.getString(filterR.string.bottom_sheet_filter_finish_button_no_count)
        } else {
            String.format(
                context.getString(filterR.string.bottom_sheet_filter_finish_button_template_text),
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
