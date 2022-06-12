package com.tokopedia.shop.flashsale.presentation.creation.manage.model

import com.tokopedia.kotlin.extensions.view.ZERO

data class ReserveProductModel(
    val productName: String = "",
    val imageUrl: String = "",
    val sku: String = "",
    val price: Double = Int.ZERO.toDouble(),
    val variantCount: Int = Int.ZERO,
    val stock: Long = Int.ZERO.toLong(),
    var isSelected: Boolean = false,
)
