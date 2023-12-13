package com.tokopedia.tokopedianow.annotation.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory.BrandWidgetTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel

data class BrandWidgetUiModel(
    val id: String = "",
    val header: TokoNowDynamicHeaderUiModel = TokoNowDynamicHeaderUiModel(),
    val brandList: List<BrandWidgetItemUiModel> = emptyList(),
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
): Visitable<BrandWidgetTypeFactory> {

    override fun type(typeFactory: BrandWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
