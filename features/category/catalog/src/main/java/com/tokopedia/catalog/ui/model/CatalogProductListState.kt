package com.tokopedia.catalog.ui.model

import com.tokopedia.catalog.domain.model.CatalogProductListResponse

sealed class CatalogProductListState(val data:MutableList<CatalogProductListResponse.CatalogGetProductList.CatalogProduct>?=null) {

    object Loading :CatalogProductListState()

    class Success(data:MutableList<CatalogProductListResponse.CatalogGetProductList.CatalogProduct>) : CatalogProductListState(data)
}
