package com.tokopedia.product.manage.common.feature.category.model

data class CategoryDomainModel(
    var id: Long = 0,
    var name: String = "",
    var identifier: String = "",
    var isHasChild:Boolean = false

)
