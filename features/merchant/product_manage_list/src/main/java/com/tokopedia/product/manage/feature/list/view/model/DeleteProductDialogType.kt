package com.tokopedia.product.manage.feature.list.view.model

sealed class DeleteProductDialogType {

    data class SingleProduct(
        val productId: String,
        val productName: String,
        val isMultiLocationShop: Boolean
    ): DeleteProductDialogType()

    data class MultipleProduct(val isMultiLocationShop: Boolean): DeleteProductDialogType()
}