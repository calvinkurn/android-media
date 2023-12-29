package com.tokopedia.product.manage.common.feature.category.model

data class CategorySelectedModel (
        var categoryId: Long = 0L,
        var child : List<Category> = listOf()
)
