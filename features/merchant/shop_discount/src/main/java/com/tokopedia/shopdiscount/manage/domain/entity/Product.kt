package com.tokopedia.shopdiscount.manage.domain.entity

import com.tokopedia.shopdiscount.common.entity.ProductType
import java.util.*

data class Product(
    val name: String,
    val originalMinPrice: Long,
    val originalMaxPrice: Long,
    val discountedMinPrice: Long,
    val discountedMaxPrice: Long,
    val discountMinPercentage: Int,
    val discountMaxPercentage: Int,
    val imageUrl: String,
    val totalStock: String,
    val locationCount: Int,
    val variantCount: Int,
    val discountStartDate: Date,
    val discountEndDate: Date,
    val productType: ProductType
)
