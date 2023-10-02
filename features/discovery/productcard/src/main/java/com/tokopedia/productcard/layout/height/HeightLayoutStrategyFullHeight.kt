package com.tokopedia.productcard.layout.height

import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.CardUnify2

internal class HeightLayoutStrategyFullHeight : HeightLayoutStrategy {
    override fun renderCardHeight(view: View, cardView: CardUnify2?) {
        setCardHeightMatchParent(cardView)
    }

    private fun setCardHeightMatchParent(cardView: CardUnify2?) {
        val layoutParams = cardView?.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        cardView?.layoutParams = layoutParams
    }
}
