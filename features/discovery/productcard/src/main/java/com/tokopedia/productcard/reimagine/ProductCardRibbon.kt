package com.tokopedia.productcard.reimagine

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

internal class ProductCardRibbon(view: View) {

    private val cardContainer by view.lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val ribbonText by view.lazyView<Typography?>(R.id.productCardRibbonText)
    private val ribbonArch by view.lazyView<ImageView?>(R.id.productCardRibbonArch)
    private val ribbonBackground by view.lazyView<ImageView?>(R.id.productCardRibbonBackground)

    fun render(ribbon: ProductCardModel.LabelGroup?) {
        val hasRibbon = ribbon != null

        ribbonText?.shouldShowWithAction(hasRibbon) {
            it.text = ribbon?.title ?: ""
        }
        ribbonArch?.showWithCondition(hasRibbon)
        ribbonBackground?.showWithCondition(hasRibbon)


        cardContainer?.layoutParams = cardContainer?.layoutParams?.apply {
            val marginLayoutParams = this as? ViewGroup.MarginLayoutParams
            marginLayoutParams?.marginStart = if (hasRibbon) 4.toPx() else 0
        }
    }
}
