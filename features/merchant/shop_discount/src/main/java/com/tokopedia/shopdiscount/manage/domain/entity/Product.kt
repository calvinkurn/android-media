package com.tokopedia.shopdiscount.manage.domain.entity

import com.tokopedia.shopdiscount.common.entity.ProductType

data class Product(
    val id : String,
    val name: String,
    val formattedOriginalMinPrice: String,
    val formattedOriginalMaxPrice: String,
    val formattedDiscountMinPrice: String,
    val formattedDiscountMaxPrice: String,
    val formattedDiscountMinPercentage: String,
    val formattedDiscountMaxPercentage: String,
    val imageUrl: String,
    val totalStock: String,
    val locationCount: Int,
    val hasVariant : Boolean,
    val discountStartDate: String,
    val discountEndDate: String,
    val productType: ProductType,
    val hasSameDiscountPercentageAmount: Boolean,
    val hasSameDiscountedPriceAmount: Boolean,
    val hasSameOriginalPrice: Boolean,
    val isCheckboxTicked : Boolean = false,
    val shouldDisplayCheckbox : Boolean = false,
    val disableClick: Boolean = false,
    val sku: String
)
