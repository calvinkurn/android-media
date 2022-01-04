package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryItemTypeFactory

data class TokoNowCategoryItemUiModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val appLink: String = "",
    val warehouseId: String = ""
): Visitable<TokoNowCategoryItemTypeFactory> {
    override fun type(typeFactoryCategory: TokoNowCategoryItemTypeFactory): Int {
        return typeFactoryCategory.type(this)
    }
}