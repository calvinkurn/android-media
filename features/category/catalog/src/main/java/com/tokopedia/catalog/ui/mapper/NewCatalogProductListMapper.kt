package com.tokopedia.catalog.ui.mapper

import com.tokopedia.catalog.domain.model.CatalogSearchProductForReimaganeResponse
import com.tokopedia.catalog.domain.model.NewProductListResponse

class NewCatalogProductListMapper {

    fun transform(searchResponse: CatalogSearchProductForReimaganeResponse)
            : NewProductListResponse {
        return NewProductListResponse(searchResponse.searchProduct)
    }
}
