package com.tokopedia.search.result.product.productfilterindicator

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.helper.isSortHasDefaultValue
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.result.product.SearchParameterProvider
import javax.inject.Inject

class ProductFilterIndicatorDelegate  @Inject constructor(
    private val filterController: FilterController,
    private val searchParameterProvider: SearchParameterProvider,
) : ProductFilterIndicator {
    private val searchParameter: SearchParameter?
        get() = searchParameterProvider.getSearchParameter()

    override val isAnyFilterActive: Boolean
        get() = filterController.isFilterActive()
    override val isAnySortActive: Boolean
        get() {
            val mapParameter = searchParameter?.getSearchParameterMap() ?: mapOf()
            return !isSortHasDefaultValue(mapParameter)
        }
    override val isAnyFilterOrSortActive: Boolean
        get() = isAnyFilterActive || isAnySortActive
}
