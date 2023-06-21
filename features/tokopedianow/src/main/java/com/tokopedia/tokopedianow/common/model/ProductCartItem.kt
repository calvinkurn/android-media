package com.tokopedia.tokopedianow.common.model

data class ProductCartItem(
    val id: String,
    val shopId: String,
    val quantity: Int,
    val stock: Int,
    val isVariant: Boolean
)
