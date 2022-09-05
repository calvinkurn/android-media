package com.tokopedia.tokofood.common.presentation.uimodel

class UpdateParam(
    val productList: List<UpdateProductParam> = listOf(),
    var shopId: String = ""
)

class UpdateProductParam(
    val productId: String,
    val cartId: String,
    val notes: String,
    val quantity: Int,
    val variants: List<UpdateProductVariantParam> = listOf()
)

class UpdateProductVariantParam(
    val variantId: String,
    val optionId: String
)