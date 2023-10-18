package com.tokopedia.search.result.product.inspirationwidget

import android.content.Context
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardOptionDataView
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterDataView
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterListener
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterOptionDataView
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
) : InspirationCardListener,
    InspirationFilterListener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onInspirationCardOptionClicked(optionData: InspirationCardOptionDataView) {
        trackEventClickInspirationCardOption(optionData)

        openApplink(context, optionData.applink)
    }

    private fun trackEventClickInspirationCardOption(option: InspirationCardOptionDataView) {
        val label = "${option.inspirationCardType} - $queryKey - ${option.text}"

        InspirationWidgetTracking.trackEventClickInspirationCardOption(label)
    }

    override fun onInspirationFilterOptionClicked(
        filterOptionDataView: InspirationFilterOptionDataView,
        filterDataView: InspirationFilterDataView,
    ) {
        val optionList = filterOptionDataView.optionList
        val isFilterSelectedReversed = !isFilterSelected(optionList)

        val nonSelectedFilterList = filterDataView.optionFilterData - filterOptionDataView

        trackInspirationFilterOptionClick(isFilterSelectedReversed, filterOptionDataView)

        applyInspirationFilter(
            filterDataView,
            optionList,
            nonSelectedFilterList,
            isFilterSelectedReversed,
            filterOptionDataView.componentId
        )
    }

    private fun isFilterSelected(option: Option): Boolean {
        return if (option.isPriceRange) {
            filterController.isPriceRangeFilterSelected(option)
        } else {
            filterController.getFilterViewState(option)
        }
    }

    override fun isFilterSelected(optionList: List<Option>): Boolean {
        return if (optionList.isEmpty()) {
            false
        } else {
            optionList.all { option -> isFilterSelected(option) }
        }
    }

    private fun FilterController.isPriceRangeFilterSelected(option: Option) =
        option.valMin == getMinPrice()
            && option.valMax == getMaxPrice()

    private fun FilterController.getMinPrice(): String =
        getParameter()[SearchApiConst.PMIN] ?: ""

    private fun FilterController.getMaxPrice(): String =
        getParameter()[SearchApiConst.PMAX] ?: ""

    private fun trackInspirationFilterOptionClick(
        isFilterSelected: Boolean,
        filterOptionDataView: InspirationFilterOptionDataView,
    ) {
        if (isFilterSelected) filterOptionDataView.click(TrackApp.getInstance().gtm)
    }

    private fun applyInspirationFilter(
        filterDataView: InspirationFilterDataView,
        optionList: List<Option>,
        nonSelectedFilterList: List<InspirationFilterOptionDataView>,
        isFilterSelected: Boolean,
        componentId: String,
    ) {
        if (filterDataView.isTypeRadio) {
            val previousOptionList = nonSelectedFilterList
                .filter { isFilterSelected(it.optionList) }
                .flatMap { it.optionList }
            removePreviousOptionFromFilterController(previousOptionList)
        }

        optionList.forEach { option ->
            applyFilterToFilterController(option, isFilterSelected)
        }

        val queryParams = filterController.getParameter() +
            originFilterMap() +
            componentIdMap(componentId) +
            manualFilterToggleMap()

        parameterListener.refreshSearchParameter(queryParams)
        parameterListener.reloadData()
    }

    private fun removePreviousOptionFromFilterController(previousOptionList: List<Option>) {
        previousOptionList.forEach { option ->
            applyFilterToFilterController(option, false)
        }
    }

    private fun applyFilterToFilterController(option: Option, isFilterSelected: Boolean) {
        when {
            option.isPriceRange -> applyPriceRangeFilter(option, isFilterSelected)
            option.isMinOrMaxPriceOption -> applyPriceFilter(option, isFilterSelected)
            option.isCategoryOption -> applyCategoryFilter(option, isFilterSelected)
            else -> applyRegularFilter(option, isFilterSelected)
        }
    }

    private fun applyPriceRangeFilter(option: Option, isFilterSelected: Boolean) {
        val valMin = if (isFilterSelected) option.valMin else ""
        val valMax = if (isFilterSelected) option.valMax else ""

        filterController.setFilter(
            Option(name = "", key = Option.KEY_PRICE_MIN, value = valMin),
            isFilterApplied = valMin != "",
            isCleanUpExistingFilterWithSameKey = true
        )

        filterController.setFilter(
            Option(name = "", key = Option.KEY_PRICE_MAX, value = valMax),
            isFilterApplied = valMax != "",
            isCleanUpExistingFilterWithSameKey = true
        )
    }

    private fun applyPriceFilter(option: Option, isFilterSelected: Boolean) {
        val priceValue = if (isFilterSelected) option.value else ""
        filterController.setFilter(
            Option(name = "", key = option.key, value = priceValue),
            isFilterApplied = priceValue != "",
            isCleanUpExistingFilterWithSameKey = true,
        )
    }

    private fun applyCategoryFilter(option: Option, isFilterSelected: Boolean) {
        filterController.setFilter(option, isFilterSelected, true)
    }

    private fun applyRegularFilter(option: Option, isFilterSelected: Boolean) {
        filterController.setFilter(option, isFilterSelected)
    }
}
