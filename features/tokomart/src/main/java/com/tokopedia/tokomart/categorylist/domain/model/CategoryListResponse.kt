package com.tokopedia.tokomart.categorylist.domain.model

data class CategoryListResponse(
    val id: String,
    val title: String,
    val iconUrl: String? = null,
    val childList: List<CategoryListResponse>? = null
)