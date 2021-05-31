package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.catalog.model.raw.ProductListResponse

class CatalogProductListMapper {

    fun transform(searchResponse: CatalogSearchProductResponse)
            : ProductListResponse {
        return ProductListResponse(searchResponse.searchProduct)
    }
}
