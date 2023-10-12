package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalogcommon.uimodel.BlankUiModel

interface CatalogComparisonDetailAdapterFactory {
    fun type(uiModel: BlankUiModel): Int
}
