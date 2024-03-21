package com.tokopedia.catalog.ui.model

import com.tokopedia.catalog.domain.model.CatalogProductListResponse

sealed class CatalogProductListState(val data:MutableList<CatalogProductListUiModel.CatalogProductUiModel>?=null) {

    object Loading :CatalogProductListState()

    class Success(data:MutableList<CatalogProductListUiModel.CatalogProductUiModel>) : CatalogProductListState(data)
}
