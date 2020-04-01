package com.tokopedia.productcard

import android.graphics.Paint
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.utils.TRANSPARENT_BLACK
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.product_card_flashsale_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_flashsale_layout.view.*
import kotlinx.android.synthetic.main.product_card_flashsale_layout.view.labelProductStatus

internal fun ProductCardFlashSaleView.renderProductCardFlashSaleContent(productCardModel: ProductCardFlashSaleModel) {
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderTopAds(productCardModel)
    renderDiscount(productCardModel)
    renderTextPrice(productCardModel)
    renderStockPercentage(productCardModel)
    renderStockLabel(productCardModel)
    renderOutOfStockView(productCardModel)
}

private fun ProductCardFlashSaleView.renderPdpCountView(productCardModel: ProductCardFlashSaleModel) {
    ivPdpView.hide()
    tvPdpView?.shouldShowWithAction(productCardModel.pdpViewCount.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.pdpViewCount)
        ivPdpView.show()
    }
}

private fun ProductCardFlashSaleView.renderTopAds(productCardModel: ProductCardFlashSaleModel){
    tv_top_ads.shouldShowWithAction(productCardModel.isTopAds){}
}

private fun ProductCardFlashSaleView.renderTextProductName(productCardModel: ProductCardFlashSaleModel) {
    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.productName)
    }
}

private fun ProductCardFlashSaleView.renderDiscount(productCardModel: ProductCardFlashSaleModel) {
    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        it.text = productCardModel.discountPercentage
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        it.text = productCardModel.slashedPrice
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun ProductCardFlashSaleView.renderTextPrice(productCardModel: ProductCardFlashSaleModel) {
    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        it.text = priceToRender
    }
}

private fun ProductCardFlashSaleView.renderStockPercentage(productCardModel: ProductCardFlashSaleModel) {
    progressBar?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.progress = productCardModel.stockBarPercentage
    }
}

private fun ProductCardFlashSaleView.renderStockLabel(productCardModel: ProductCardFlashSaleModel) {
    tvLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.text = productCardModel.stockBarLabel
    }
}

private fun ProductCardFlashSaleView.renderOutOfStockView(productCardModel: ProductCardFlashSaleModel) {
    if (productCardModel.isOutOfStock) {
        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())
        oosOverlay.visibility = View.VISIBLE
    } else {
        oosOverlay.visibility = View.GONE
    }
}

private fun setOutOfStock(it: Label) {
    it.text = "Stok Habis"
    it.trySetCustomLabelType("transparentBlack")
}

private fun Label.trySetCustomLabelType(labelGroupType: String) {
    unlockFeature = true

    val colorRes = labelGroupType.toUnifyLabelColor()
    val colorHexInt = ContextCompat.getColor(context, colorRes)
    val colorHexString = "#${Integer.toHexString(colorHexInt)}"
    setLabelType(colorHexString)
}

@ColorRes
private fun String?.toUnifyLabelColor(): Int {
    return when(this) {
        TRANSPARENT_BLACK -> com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
        else -> com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
    }
}

private fun ProductCardFlashSaleModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}
