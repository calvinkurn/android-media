package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryChipsTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowCategoryChipsUiModel(
    val id: String,
    val title: String,
    val categoryList: List<TokoNowCategoryItemUiModel>?,
    @TokoNowLayoutState val state: Int
): Visitable<TokoNowCategoryChipsTypeFactory> {
    override fun type(typeFactory: TokoNowCategoryChipsTypeFactory): Int {
        return typeFactory.type(this)
    }
}