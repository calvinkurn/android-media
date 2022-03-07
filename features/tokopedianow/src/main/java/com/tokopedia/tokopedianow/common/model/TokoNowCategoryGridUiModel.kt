package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowCategoryGridUiModel(
    val id: String,
    val title: String,
    val categoryList: List<TokoNowCategoryItemUiModel>?,
    @TokoNowLayoutState val state: Int
): Visitable<TokoNowCategoryGridTypeFactory> {
    override fun type(typeFactory: TokoNowCategoryGridTypeFactory): Int {
        return typeFactory.type(this)
    }
}