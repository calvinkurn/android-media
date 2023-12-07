package com.tokopedia.oldcatalog.model.util

import com.tokopedia.oldcatalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.oldcatalog.model.raw.ProductListResponse

class CatalogProductListMapper {

    fun transform(searchResponse: CatalogSearchProductResponse)
            : ProductListResponse {
        return ProductListResponse(searchResponse.searchProduct)
    }
}
