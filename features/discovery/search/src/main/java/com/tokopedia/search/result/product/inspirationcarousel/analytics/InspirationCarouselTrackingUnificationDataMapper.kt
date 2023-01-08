package com.tokopedia.search.result.product.inspirationcarousel.analytics

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

object InspirationCarouselTrackingUnificationDataMapper {
    private val SearchParameter?.queryKey: String
        get() {
            val searchParameter = this ?: return ""
            return searchParameter.getSearchQuery()
        }

    fun createCarouselTrackingUnificationData(
        product: InspirationCarouselDataView.Option.Product,
        searchParameter: SearchParameter?,
        cartId: String = "",
        quantity: Int = 0,
    ): InspirationCarouselTracking.Data {
        return InspirationCarouselTracking.Data(
            searchParameter.queryKey,
            product,
            getSortFilterParamStringFromSearchParameter(searchParameter),
            cartId,
            quantity,
        )
    }

    private fun getSortFilterParamStringFromSearchParameter(
        searchParameter: SearchParameter?
    ): String {
        val parameter = searchParameter ?: return ""
        @Suppress("UNCHECKED_CAST")
        return getSortFilterParamsString(parameter.getSearchParameterMap() as Map<String?, String?>)
    }
}
