package com.tokopedia.catalog.ui.model

import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.sortfilter.compose.SortFilter

sealed class CatalogFilterProductListState(val data:MutableList<SortFilter>?=null) {

    object Loading :CatalogFilterProductListState()

    class Success(data:MutableList<SortFilter>) : CatalogFilterProductListState(data)
}
