package com.tokopedia.product.addedit.category.presentation.model


class CategoryUiModel(
        var categoryId: String,
        var categoryName: String,
        var child: List<CategoryUiModel>,
        var isSelected: Boolean = false
)