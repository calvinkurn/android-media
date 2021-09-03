package com.tokopedia.tokopedianow.common.model

import androidx.annotation.StringRes
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

data class TokoNowCategoryGridUiModel(
    val id: String,
    val title: String,
    @StringRes val titleRes: Int = -1,
    val categoryList: List<TokoNowCategoryItemUiModel>?,
    @TokoNowLayoutState val state: Int
): TokoNowLayoutUiModel(id) {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}