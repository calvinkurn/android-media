package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
sealed class PlayBroProductSummaryAction {
    data class DeleteProduct(val product: ProductUiModel): PlayBroProductSummaryAction()
    object LoadProductSummary: PlayBroProductSummaryAction()
}