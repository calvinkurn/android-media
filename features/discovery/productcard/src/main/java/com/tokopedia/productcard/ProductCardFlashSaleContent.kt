package com.tokopedia.productcard

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.shouldShowWithAction
import kotlinx.android.synthetic.main.product_card_flashsale_content_layout.view.*

internal fun View.renderProductCardFlashSaleContent(productCardModel: ProductCardFlashSaleModel) {
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderTextPrice(productCardModel)
    renderStockLabel(productCardModel)
    renderStockPercentage(productCardModel)
}

private fun View.renderPdpCountView(productCardModel: ProductCardFlashSaleModel) {
    tvPdpView?.shouldShowWithAction(productCardModel.pdpViewCount.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.pdpViewCount)
        ivPdpView.show()
    }
}

private fun View.renderTextProductName(productCardModel: ProductCardFlashSaleModel) {
    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.productName)
    }
}

private fun View.renderDiscount(productCardModel: ProductCardFlashSaleModel) {
    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        it.text = productCardModel.discountPercentage
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        it.text = productCardModel.slashedPrice
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun View.renderLabelPrice(productCardModel: ProductCardFlashSaleModel) {
    labelPrice?.initLabelGroup(productCardModel.getLabelPrice())
}

private fun View.renderTextPrice(productCardModel: ProductCardFlashSaleModel) {
    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        it.text = priceToRender
    }
}

private fun View.renderStockPercentage(productCardModel: ProductCardFlashSaleModel) {
    progressBar?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.progress = productCardModel.stockBarPercentage
    }
}

private fun View.renderStockLabel(productCardModel: ProductCardFlashSaleModel) {
    tvLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
        it.text = productCardModel.stockBarLabel
    }
}


private fun ProductCardFlashSaleModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}
