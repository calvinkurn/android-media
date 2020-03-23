package com.tokopedia.productcard

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.shouldShowWithAction
import kotlinx.android.synthetic.main.product_card_flashsale_content_layout.view.*

internal fun ProductCardFlashSaleView.renderProductCardFlashSaleContent(productCardModel: ProductCardFlashSaleModel) {
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderTextPrice(productCardModel)
    renderStockLabel(productCardModel)
    renderStockPercentage(productCardModel)
}

private fun ProductCardFlashSaleView.renderPdpCountView(productCardModel: ProductCardFlashSaleModel) {
    tvPdpView?.shouldShowWithAction(productCardModel.pdpViewCount.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.pdpViewCount)
        ivPdpView.show()
    }
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

private fun ProductCardFlashSaleView.renderLabelPrice(productCardModel: ProductCardFlashSaleModel) {
    labelPrice?.initLabelGroup(productCardModel.getLabelPrice())
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


private fun ProductCardFlashSaleModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}
