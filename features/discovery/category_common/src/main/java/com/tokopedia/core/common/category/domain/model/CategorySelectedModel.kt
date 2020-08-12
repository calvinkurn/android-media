package com.tokopedia.core.common.category.domain.model

data class CategorySelectedModel (
        var categoryId: Long = 0L,
        var child : List<Category> = listOf()
)