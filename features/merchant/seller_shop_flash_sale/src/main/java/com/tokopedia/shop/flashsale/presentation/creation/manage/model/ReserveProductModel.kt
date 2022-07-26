package com.tokopedia.shop.flashsale.presentation.creation.manage.model

import com.tokopedia.kotlin.extensions.view.ZERO

data class ReserveProductModel(
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val sku: String = "",
    val price: Double = Int.ZERO.toDouble(),
    val stock: Long = Int.ZERO.toLong(),
    var isSelected: Boolean = false,
    var disabled: Boolean = false,
    var disabledReason: String = "",
    val variant: List<String>
)
