package com.tokopedia.productcard

import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.utils.GOLD
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

internal fun View.renderProductCardRibbon(
    productCardModel: ProductCardModel,
    isProductCardGrid: Boolean,
) {
    val archView = findViewById<ImageView?>(R.id.imageRibbonArch)
    val contentView = findViewById<ImageView?>(R.id.imageRibbonContent)
    val textView = findViewById<Typography?>(R.id.textRibbon)

    if (productCardModel.showRibbon) {
        if (isProductCardGrid) {
            setArchMargin(productCardModel, archView)
            setArchElevation(productCardModel, archView)
        }

        archView?.show()
        contentView?.show()
        textView?.show()

        val labelRibbon = productCardModel.getLabelRibbon() ?: LabelGroup()

        val ribbonBackgroundPair = getRibbonBackground(labelRibbon.type)
        archView.setImageResource(ribbonBackgroundPair.first)
        contentView.setImageResource(ribbonBackgroundPair.second)

        val previousText = textView?.text
        textView?.text = labelRibbon.title

        if (previousText?.length != labelRibbon.title.length)
            contentView.requestLayout()
    } else {
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
        when(productCardModel.cardType) {
            CardUnify2.TYPE_SHADOW -> 12.7f.toPx().toInt()
            CardUnify2.TYPE_CLEAR -> 6.15f.toPx().toInt()
            else -> 6.8f.toPx().toInt()
        }

    archView.setMargin(0, archMarginTop, 0, 0)
}

private fun setArchElevation(
    productCardModel: ProductCardModel,
    archView: ImageView
) {
    val archElevation =
        when(productCardModel.cardType) {
            CardUnify2.TYPE_CLEAR -> 0f
            else -> 4f
        }

    archView.elevation = archElevation.toPx()
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
