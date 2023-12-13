package com.tokopedia.tokopedianow.annotation.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory.BrandWidgetItemTypeFactory

data class BrandWidgetItemUiModel(
    val id: String = "",
    val imageUrl: String = "",
    val appLink: String = ""
): Visitable<BrandWidgetItemTypeFactory> {

    override fun type(typeFactory: BrandWidgetItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}
