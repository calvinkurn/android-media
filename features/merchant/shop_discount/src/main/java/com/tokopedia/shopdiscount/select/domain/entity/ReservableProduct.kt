package com.tokopedia.shopdiscount.select.domain.entity

data class ReservableProduct(
    val id: String,
    val name: String,
    val picture: String,
    val minPriceFormatted: String,
    val maxPriceFormatted: String,
    val sku: String,
    val stock: String,
    val url: String,
    val countVariant: Int,
    val disabled: Boolean,
    val disabledReason: String,
    val hasSameOriginalPrice : Boolean,
    val isCheckboxTicked : Boolean = false,
    val disableClick: Boolean = false,
)