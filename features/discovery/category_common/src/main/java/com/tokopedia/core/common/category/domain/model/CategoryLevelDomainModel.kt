package com.tokopedia.core.common.category.domain.model

class CategoryLevelDomainModel {
    var selectedCategoryId: Long = 0
    var parentCategoryId: Long = 0
    var categoryModels: List<CategoryDomainModel>? = listOf()
}