package com.tokopedia.productcard.layout.eta

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.unifyprinciples.Typography

internal class EtaLayoutStrategyReposition : EtaLayoutStrategy {
    override fun renderTextEta(view: View, productCardModel: ProductCardModel) {
        val textViewInlineETA = view.findViewById<Typography?>(R.id.textViewInlineETA)
        textViewInlineETA?.initLabelGroup(productCardModel.getLabelETA())

        val textViewETA = view.findViewById<Typography?>(R.id.textViewETA)
        textViewETA?.gone()
    }
}
