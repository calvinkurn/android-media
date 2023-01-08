package com.tokopedia.play.broadcaster.setup.product.model

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
sealed interface PlayBroProductChooserEvent {

    object SaveProductSuccess : PlayBroProductChooserEvent
    data class ShowError(val error: Throwable) : PlayBroProductChooserEvent

    data class GetDataError(
        val throwable: Throwable,
        val action: (() -> Unit)? = null,
    ) : PlayBroProductChooserEvent

    data class DeleteProductSuccess(
        val deletedProductCount: Int
    ) : PlayBroProductChooserEvent

    data class DeleteProductError(
        val throwable: Throwable,
        val action: (() -> Unit)? = null,
    ) : PlayBroProductChooserEvent

    data class FailPinUnPinProduct(
        val throwable: Throwable,
        val isPinned: Boolean,
    ) : PlayBroProductChooserEvent
}