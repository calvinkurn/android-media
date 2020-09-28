package com.tokopedia.core.common.category.domain.model

data class CategoryModel (
        var id: Long = 0L,
        var name: String = "",
        var hasChild: Boolean = false
)