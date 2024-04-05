package com.tokopedia.carouselproductcard.reimagine.grid

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.reimagine.ProductCardModel

data class CarouselProductCardGridModel(
    val productCardModel: ProductCardModel,
    val impressHolder: (Int) -> ImpressHolder? = { null },
    val onImpressed: () -> Unit = { },
    val onClick: () -> Unit = { },
    val onAddToCart: () -> Unit = { },
    val onAreaClicked: () -> Unit = { },
    val onProductImageClicked: () -> Unit = { },
    val onSellerInfoClicked: () -> Unit = { },
    val onViewAttachedToWindow: () -> Unit = { },
    val onViewDetachedFromWindow: (visiblePercentage: Int) -> Unit = { },
): Visitable<CarouselProductCardTypeFactory> {

    override fun type(typeFactory: CarouselProductCardTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
