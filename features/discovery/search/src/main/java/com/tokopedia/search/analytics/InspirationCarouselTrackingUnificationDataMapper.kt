package com.tokopedia.search.analytics

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView

object InspirationCarouselTrackingUnificationDataMapper {
    private val SearchParameter?.queryKey: String
        get() {
            val searchParameter = this ?: return ""
            return searchParameter.getSearchQuery()
        }

    fun createCarouselTrackingUnificationData(
        product: InspirationCarouselDataView.Option.Product,
        searchParameter: SearchParameter?
    ): InspirationCarouselTrackingUnification.Data {
        return InspirationCarouselTrackingUnification.Data(
            searchParameter.queryKey,
            product,
            getSortFilterParamStringFromSearchParameter(searchParameter),
        )
    }

    fun getSortFilterParamStringFromSearchParameter(
        searchParameter: SearchParameter?
    ): String {
        val parameter = searchParameter ?: return ""
        @Suppress("UNCHECKED_CAST")
        return getSortFilterParamsString(parameter.getSearchParameterMap() as Map<String?, String?>)
    }
}