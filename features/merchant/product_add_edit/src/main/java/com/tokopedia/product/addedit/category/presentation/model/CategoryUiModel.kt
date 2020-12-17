package com.tokopedia.product.addedit.category.presentation.model


data class CategoryUiModel(
        var categoryId: String,
        var categoryName: String,
        var child: List<CategoryUiModel>,
        var isSelected: Boolean = false,
        var categoryLevel: Int = 0
)