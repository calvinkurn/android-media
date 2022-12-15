package com.tokopedia.tkpd.flashsale.domain.entity

data class CriteriaSelection (
    val criteriaId: Long = 0,
    var selectionCount: Int = 0,
    val categoryTitle: String = "",
    val categoryTitleComplete: String = "",
    val selectionCountMax: Int = 0,
    val hasMoreData: Boolean = false,
    val categoryList: List<Category> = emptyList()
)