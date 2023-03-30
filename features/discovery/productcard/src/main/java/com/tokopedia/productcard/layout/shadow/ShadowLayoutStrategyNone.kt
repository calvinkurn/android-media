package com.tokopedia.productcard.layout.shadow

import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx

class ShadowLayoutStrategyNone : ShadowLayoutStrategy {
    override fun renderProductCardShadow(productCardLayout: CardUnify2?) {
        val cardLayout = productCardLayout?: return
        cardLayout.cardType = CardUnify2.TYPE_BORDER
        cardLayout.useCompatPadding = false
        val margin = CARD_MARGIN.toPx()
        cardLayout.setMargin(margin, margin, margin, margin)
    }

    companion object {
        const val CARD_MARGIN = 4
    }
}
