package com.tokopedia.productcard.layout.shadow

import com.tokopedia.unifycomponents.CardUnify2

internal class ShadowLayoutStrategyControl : ShadowLayoutStrategy {
    override fun renderProductCardShadow(productCardLayout: CardUnify2?) {
        val cardLayout = productCardLayout ?: return
        cardLayout.cardType = CardUnify2.TYPE_SHADOW
        cardLayout.useCompatPadding = true
    }
}
