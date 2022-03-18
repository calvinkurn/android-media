package com.tokopedia.tokopedianow.category.domain.model

data class CategorySharingModel(
    val categoryIdLvl2: String,
    val categoryIdLvl3: String,
    val name: String,
    val url: String
)