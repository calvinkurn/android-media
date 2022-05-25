package com.tokopedia.core.common.category.domain.model

data class CategoryDomainModel(
    var id: Long = 0,
    var name: String = "",
    var identifier: String = "",
    var isHasChild:Boolean = false

)
