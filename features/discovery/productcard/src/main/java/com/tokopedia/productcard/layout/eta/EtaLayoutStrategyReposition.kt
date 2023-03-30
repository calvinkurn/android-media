package com.tokopedia.productcard.layout.eta

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.unifyprinciples.Typography

class EtaLayoutStrategyReposition : EtaLayoutStrategy {
    override fun renderTextEta(view: View, productCardModel: ProductCardModel) {
        val textViewETA = view.findViewById<Typography?>(R.id.textViewInlineETA)
        val labelGroupETA = productCardModel.getLabelETA()
        textViewETA?.initLabelGroup(labelGroupETA)
    }
}
