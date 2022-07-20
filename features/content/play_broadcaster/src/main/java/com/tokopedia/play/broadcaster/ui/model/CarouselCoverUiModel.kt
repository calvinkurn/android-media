package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created by jegul on 24/06/20
 */
sealed class CarouselCoverUiModel {

    object Camera : CarouselCoverUiModel()
    data class Product(val model: ProductUiModel) : CarouselCoverUiModel()
}