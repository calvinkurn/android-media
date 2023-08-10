package com.tokopedia.productcard.layout.shadow

import android.graphics.Color
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.CARD_MARGIN
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx

internal class ShadowLayoutStrategyClear: ShadowLayoutStrategy {

    override fun renderProductCardShadow(
        productCardModel: ProductCardModel,
        containerView: ViewGroup,
        cardView: CardUnify2?,
        isProductCardGrid: Boolean,
    ) {
        cardView ?: return
        cardView.cardType = CardUnify2.TYPE_CLEAR
        cardView.setCardUnifyBackgroundColor(Color.TRANSPARENT)
        cardView.useCompatPadding = false

        val margin = CARD_MARGIN.toPx()
        containerView.setMargin(margin, margin, margin, margin)

        if (isProductCardGrid) {
            cardView.setMargin(cardViewLeftMargin(productCardModel), 0, 0, 0)
        }
    }

    private fun cardViewLeftMargin(productCardModel: ProductCardModel) =
        (if (productCardModel.showRibbon) 4 else 0).toPx()
}
