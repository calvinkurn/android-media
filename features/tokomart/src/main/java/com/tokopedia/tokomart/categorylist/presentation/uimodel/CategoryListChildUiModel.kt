package com.tokopedia.tokomart.categorylist.presentation.uimodel

import androidx.annotation.ColorRes
import com.tokopedia.unifyprinciples.Typography

data class CategoryListChildUiModel(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val appLink: String? = null,
    val textWeight: Int = Typography.REGULAR,
    @ColorRes val textColorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_N700,
    val type: CategoryType = CategoryType.CHILD_CATEGORY_ITEM
) {
    enum class CategoryType {
        CHILD_CATEGORY_ITEM,
        ALL_CATEGORY_TEXT
    }
}