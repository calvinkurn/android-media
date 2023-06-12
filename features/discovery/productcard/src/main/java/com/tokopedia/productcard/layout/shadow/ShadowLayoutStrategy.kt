package com.tokopedia.productcard.layout.shadow

import android.view.ViewGroup
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2

internal interface ShadowLayoutStrategy {
    fun renderProductCardShadow(
        productCardModel: ProductCardModel,
        containerView: ViewGroup,
        cardView: CardUnify2?,
        isProductCardGrid: Boolean,
    )
}
