package com.tokopedia.mvc.domain.entity

data class ProductMetadata(
    val sortOptions: List<ProductSortOptions>,
    val categoryOptions: List<ProductCategoryOption>
)
