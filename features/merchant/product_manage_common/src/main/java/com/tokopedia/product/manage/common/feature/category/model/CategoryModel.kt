package com.tokopedia.product.manage.common.feature.category.model

data class CategoryModel (
        var id: Long = 0L,
        var name: String = "",
        var hasChild: Boolean = false
)
