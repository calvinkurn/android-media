package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.catalog.model.raw.ProductListResponse

class CatalogProductListMapper {

    fun transform(searchResponse: CatalogSearchProductResponse)
            : ProductListResponse {
        return ProductListResponse(searchResponse.searchProduct)
    }

    private fun getReviewCount(s: String): Int {
        return try {
            val reviewCount = s.replace(".", "").replace(",", "")
            Integer.parseInt(reviewCount)
        } catch (e: NumberFormatException) {
            0
        }
    }
}
