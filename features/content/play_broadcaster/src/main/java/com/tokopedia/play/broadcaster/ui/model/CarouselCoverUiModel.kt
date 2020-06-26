package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 24/06/20
 */
sealed class CarouselCoverUiModel {

    object Camera : CarouselCoverUiModel()
    data class Product(val model: ProductContentUiModel) : CarouselCoverUiModel()
}