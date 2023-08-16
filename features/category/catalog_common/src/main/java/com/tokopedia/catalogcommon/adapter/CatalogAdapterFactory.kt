package com.tokopedia.catalogcommon.adapter

import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel

interface CatalogAdapterFactory {
    fun type(uiModel: TopFeaturesUiModel): Int
    fun type(uiModel: HeroBannerUiModel): Int
    fun type(uiModel: DummyUiModel): Int
    fun type(uiModel: SliderImageTextUiModel): Int

    fun type(uiModel: PanelImageUiModel): Int
}
