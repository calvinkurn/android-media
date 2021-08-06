package com.tokopedia.tokopedianow.common.model

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

data class TokoNowCategoryGridUiModel(
    val id: String,
    val title: String,
    val categoryList: List<TokoNowCategoryItemUiModel>?,
    @TokoNowLayoutState val state: Int
): TokoNowLayoutUiModel(id) {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}