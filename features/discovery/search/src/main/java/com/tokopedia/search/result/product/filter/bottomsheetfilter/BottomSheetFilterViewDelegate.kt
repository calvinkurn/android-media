package com.tokopedia.search.result.product.filter.bottomsheetfilter

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.filter.analytics.SearchSortFilterTracking
import com.tokopedia.search.result.product.lastfilter.LastFilterListener
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.getUserId
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
    SortFilterBottomSheet.Callback,
    LifecycleObserver {

    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private var callback: BottomSheetFilterCallback? = null

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
                getFragment().parentFragmentManager,
                getSearchParameter()?.getSearchParameterHashMap(),
                dynamicFilterModel,
                this
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

    override fun onApplySortFilter(
        applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel,
    ) {
        callback?.onApplySortFilter(applySortFilterModel.mapParameter)

        sortFilterBottomSheet = null

        applySort(applySortFilterModel)
        applyFilter(applySortFilterModel)

        parameterListener.refreshSearchParameter(applySortFilterModel.mapParameter)

        lastFilterListener.updateLastFilter()

        parameterListener.reloadData()
    }

    private fun applySort(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        if (applySortFilterModel.selectedSortName.isEmpty()
            || applySortFilterModel.selectedSortMapParameter.isEmpty()
        ) return

        SearchSortFilterTracking.eventSearchResultSort(
            getScreenName(),
            applySortFilterModel.selectedSortName,
            getUserId(userSessionInterface),
        )
    }

    private fun applyFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        SearchSortFilterTracking.eventApplyFilter(
            getScreenName(),
            applySortFilterModel.selectedFilterMapParameter,
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
        callback = null
    }

}
