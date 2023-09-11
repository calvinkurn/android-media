package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel

interface ProductListAdapterListener {
    fun onAtcButtonClicked(atcModel: CatalogProductAtcUiModel)
}
