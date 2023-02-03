package com.tokopedia.search.result.product.inspirationwidget

import android.content.Context
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardOptionDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeListener
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeOptionDataView
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.componentIdMap
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.manualFilterToggleMap
import com.tokopedia.search.utils.originFilterMap
import com.tokopedia.track.TrackApp

class InspirationWidgetListenerDelegate(
    context: Context?,
    queryKeyProvider: QueryKeyProvider,
    private val filterController: FilterController,
    private val parameterListener: ProductListParameterListener,
): InspirationCardListener,
    InspirationSizeListener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onInspirationCardOptionClicked(optionData: InspirationCardOptionDataView) {
        trackEventClickInspirationCardOption(optionData)

        openApplink(context, optionData.applink)
    }

    private fun trackEventClickInspirationCardOption(option: InspirationCardOptionDataView) {
        val label = "${option.inspirationCardType} - ${queryKey} - ${option.text}"

        InspirationWidgetTracking.trackEventClickInspirationCardOption(label)
    }

    override fun onInspirationSizeOptionClicked(sizeOptionDataView: InspirationSizeOptionDataView) {
        val option = sizeOptionDataView.option
        val isFilterSelectedReversed = !isFilterSelected(option)

        trackInspirationSizeOptionClick(isFilterSelectedReversed, sizeOptionDataView)

        applyInspirationSizeFilter(
            option,
            isFilterSelectedReversed,
            sizeOptionDataView.componentId
        )
    }

    override fun isFilterSelected(option: Option?): Boolean {
        option ?: return false

        return filterController.getFilterViewState(option)
    }

    private fun trackInspirationSizeOptionClick(
        isFilterSelected: Boolean,
        sizeOptionDataView: InspirationSizeOptionDataView,
    ) {
        if (isFilterSelected)
            sizeOptionDataView.click(TrackApp.getInstance().gtm)
    }

    private fun applyInspirationSizeFilter(
        option: Option,
        isFilterSelected: Boolean,
        componentId: String,
    ) {
        filterController.setFilter(option, isFilterSelected)

        val queryParams = filterController.getParameter() +
            originFilterMap() +
            componentIdMap(componentId) +
            manualFilterToggleMap()

        parameterListener.refreshSearchParameter(queryParams)
        parameterListener.reloadData()
    }
}
