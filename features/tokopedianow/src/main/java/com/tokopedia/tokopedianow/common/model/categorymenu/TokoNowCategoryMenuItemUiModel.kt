package com.tokopedia.tokopedianow.common.model.categorymenu

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuItemTypeFactory

data class TokoNowCategoryMenuItemUiModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val appLink: String = "",
    val warehouseId: String = "",
    val headerName: String = "",
    val color: String = ""
): Visitable<TokoNowCategoryMenuItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactoryCategory: TokoNowCategoryMenuItemTypeFactory): Int {
        return typeFactoryCategory.type(this)
    }
}
