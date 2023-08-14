package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalog.ui.uimodel.TopFeaturesUiModel


interface CatalogAdapterFactory {
    fun type(uiModel: TopFeaturesUiModel): Int
}
