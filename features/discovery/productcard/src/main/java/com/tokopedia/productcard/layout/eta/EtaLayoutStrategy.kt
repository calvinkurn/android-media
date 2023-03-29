package com.tokopedia.productcard.layout.eta

import android.view.View
import com.tokopedia.productcard.ProductCardModel

interface EtaLayoutStrategy {
    fun renderTextEta(view: View, productCardModel: ProductCardModel)
}
