package com.tokopedia.search.result.product.inspirationwidget

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardOptionDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeOptionDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeListener
import com.tokopedia.search.utils.addFilterOrigin
import com.tokopedia.track.TrackApp
import java.lang.ref.WeakReference

class InspirationWidgetListenerDelegate(
    context: Context?,
    private val filterController: FilterController,
    private val parameterListener: ProductListParameterListener,
): InspirationCardListener, InspirationSizeListener {

    private val weakContext: WeakReference<Context?> = WeakReference(context)

    override fun onInspirationCardOptionClicked(optionData: InspirationCardOptionDataView) {
        val context = weakContext.get() ?: return

        trackEventClickInspirationCardOption(optionData)

        RouteManager.route(context, optionData.applink)
    }

    private fun trackEventClickInspirationCardOption(option: InspirationCardOptionDataView) {
        val label = "${option.inspirationCardType} - ${parameterListener.queryKey} - ${option.text}"

        InspirationWidgetTracking.trackEventClickInspirationCardOption(label)
    }

    override fun onInspirationSizeOptionClicked(sizeOptionDataView: InspirationSizeOptionDataView) {
        val option = sizeOptionDataView.option
        val isFilterSelectedReversed = !isFilterSelected(option)

        trackInspirationSizeOptionClick(isFilterSelectedReversed, sizeOptionDataView)

        applyInspirationSizeFilter(option, isFilterSelectedReversed)
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

    private fun applyInspirationSizeFilter(option: Option, isFilterSelected: Boolean) {
        filterController.setFilter(option, isFilterSelected)

        val queryParams = filterController.getParameter().addFilterOrigin()
        parameterListener.refreshSearchParameter(queryParams)
        parameterListener.reloadData()
    }
}