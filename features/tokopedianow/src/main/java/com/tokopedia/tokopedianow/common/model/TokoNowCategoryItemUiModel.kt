package com.tokopedia.tokopedianow.common.model

import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowItemTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowLayoutItemUiModel

data class TokoNowCategoryItemUiModel(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val appLink: String
): TokoNowLayoutItemUiModel(id) {
    override fun type(typeFactory: TokoNowItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}