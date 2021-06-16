package com.tokopedia.tokomart.categorylist.presentation.uimodel

import androidx.annotation.ColorRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListTypeFactory
import com.tokopedia.unifyprinciples.Typography

data class CategoryListChildUiModel(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val appLink: String? = null,
    val textWeight: Int = Typography.REGULAR,
    @ColorRes val textColorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_N700,
    val type: CategoryType = CategoryType.CHILD_CATEGORY_ITEM
) : Visitable<TokoMartCategoryListTypeFactory> {
    enum class CategoryType {
        CHILD_CATEGORY_ITEM,
        ALL_CATEGORY_TEXT
    }

    override fun type(typeFactory: TokoMartCategoryListTypeFactory): Int {
        return typeFactory.type(this)
    }
}