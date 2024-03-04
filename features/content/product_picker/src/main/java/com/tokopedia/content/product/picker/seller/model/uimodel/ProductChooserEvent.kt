package com.tokopedia.content.product.picker.seller.model.uimodel

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
sealed interface ProductChooserEvent {

    object SaveProductSuccess : ProductChooserEvent
    data class ShowError(val error: Throwable) : ProductChooserEvent

    data class GetDataError(
        val throwable: Throwable,
        val action: (() -> Unit)? = null
    ) : ProductChooserEvent

    data class DeleteProductSuccess(
        val deletedProductCount: Int
    ) : ProductChooserEvent

    data class DeleteProductError(
        val throwable: Throwable,
        val action: (() -> Unit)? = null
    ) : ProductChooserEvent

    data class FailPinUnPinProduct(
        val throwable: Throwable,
        val isPinned: Boolean
    ) : ProductChooserEvent
}
