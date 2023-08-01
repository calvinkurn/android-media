package com.tokopedia.productcard.layout.height

import android.view.View
import com.tokopedia.unifycomponents.CardUnify2

internal interface HeightLayoutStrategy {
    fun renderCardHeight(view: View, productCard: CardUnify2?)
}
