package com.tokopedia.mvc.domain.entity

data class ProductResult(
    val total: Int,
    val products: List<Product>
)
