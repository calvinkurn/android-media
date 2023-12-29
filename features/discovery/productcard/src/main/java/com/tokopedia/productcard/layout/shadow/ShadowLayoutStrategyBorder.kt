package com.tokopedia.productcard.layout.shadow

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.CARD_MARGIN
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ShadowLayoutStrategyBorder : ShadowLayoutStrategy {

    override fun renderProductCardShadow(
        productCardModel: ProductCardModel,
        containerView: ViewGroup,
        cardView: CardUnify2?,
        isProductCardGrid: Boolean,
    ) {
        cardView ?: return
        cardView.cardType = CardUnify2.TYPE_BORDER
        setCardBackground(cardView, containerView.context)
        cardView.useCompatPadding = false

        val margin = CARD_MARGIN.toPx()
        containerView.setMargin(margin, margin, margin, margin)


        if (isProductCardGrid) {
            cardView.setMargin(cardViewLeftMargin(productCardModel), 0, 0, 0)
        }
    }

    private fun cardViewLeftMargin(productCardModel: ProductCardModel) =
        (if (productCardModel.showRibbon) 3 else 0).toPx()

    private fun setCardBackground(cardView: CardUnify2?, context: Context) {
        val backgroundColor = if(context.isDarkMode()) {
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN50)
        } else ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
        cardView?.setCardUnifyBackgroundColor(backgroundColor)
    }
}
