package com.tokopedia.productcard

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.utils.GOLD
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

internal fun View.renderProductCardRibbon(
    productCardModel: ProductCardModel,
    isProductCardGrid: Boolean,
) {
    val archView = findViewById<ImageView?>(R.id.imageRibbonArch)
    val contentView = findViewById<ImageView?>(R.id.imageRibbonContent)
    val textView = findViewById<Typography?>(R.id.textRibbon)

    if (productCardModel.showRibbon) {
        if (isProductCardGrid) setArchMargin(productCardModel, archView)

        archView?.show()
        contentView?.show()
        textView?.show()

        val labelRibbon = productCardModel.getLabelRibbon() ?: LabelGroup()

        val ribbonBackgroundPair = getRibbonBackground(labelRibbon.type)
        archView.setImageResource(ribbonBackgroundPair.first)
        contentView.setImageResource(ribbonBackgroundPair.second)

        textView?.text = labelRibbon.title
        contentView?.requestLayout()
    }
    else {
        archView?.gone()
        contentView?.gone()
        textView?.gone()
    }
}

private fun setArchMargin(
    productCardModel: ProductCardModel,
    archView: ImageView
) {
    val archMarginTop =
        if (productCardModel.cardType == CardUnify2.TYPE_SHADOW) 17.toPx()
        else 11.toPx()

    archView.setMargin(0, archMarginTop, 0, 0)
}

private fun getRibbonBackground(type: String): Pair<Int, Int> {
    return when(type) {
        GOLD -> Pair(
            R.drawable.product_card_ribbon_arch_gold,
            R.drawable.product_card_ribbon_content_gold
        )
        else -> Pair(
            R.drawable.product_card_ribbon_arch_red,
            R.drawable.product_card_ribbon_content_red,
        )
    }
}
