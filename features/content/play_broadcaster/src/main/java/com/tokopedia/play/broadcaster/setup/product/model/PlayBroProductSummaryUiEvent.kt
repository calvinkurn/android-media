package com.tokopedia.play.broadcaster.setup.product.model

/**
 * Created By : Jonathan Darwin on February 09, 2022
 */
sealed class PlayBroProductSummaryUiEvent {

    data class GetDataError(val throwable: Throwable, val action: (()->Unit)? = null): PlayBroProductSummaryUiEvent()
    data class DeleteProductError(val throwable: Throwable, val action: (()->Unit)? = null): PlayBroProductSummaryUiEvent()
}