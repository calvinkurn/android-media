package com.tokopedia.catalogcommon.adapter

import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel

interface CatalogAdapterFactory {
    fun type(uiModel: TopFeaturesUiModel): Int
    fun type(uiModel: HeroBannerUiModel): Int
}
