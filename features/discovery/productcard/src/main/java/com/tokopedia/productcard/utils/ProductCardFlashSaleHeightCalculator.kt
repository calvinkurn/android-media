package com.tokopedia.productcard.utils

import android.content.Context
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.max

/**
 * @author by yoasfs on 2020-03-11
 */
suspend fun List<ProductCardFlashSaleModel>?.getMaxHeightForGridView(context: Context?, coroutineDispatcher: CoroutineDispatcher, productImageWidth: Int): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val productCardHeightList = mutableListOf<Int>()

        forEach { productCardModel ->
            val imageHeight = productImageWidth
            val cardPaddingBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_bottom)
            val contentMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)
            val contentHeight = productCardModel.getContentHeight(context)

            productCardHeightList.add(imageHeight + cardPaddingBottom + contentMarginTop + contentHeight)
        }

        productCardHeightList.max()?.toInt() ?: 0
    }
}

private fun ProductCardFlashSaleModel.getContentHeight(context: Context): Int {
    val pdpViewCountHeight = getPdpViewCountSectionHeight(context)
    val productNameSectionHeight = getProductNameSectionHeight(context)
    val promoSectionHeight = getPromoSectionHeight(context)
    val priceSectionHeight = getPriceSectionHeight(context)
    val stockBarHeight = getStockBarSectionHeight(context)
    val stockLabelHeight = getStockLabelHeight(context)

    return pdpViewCountHeight +
            productNameSectionHeight +
            promoSectionHeight +
            priceSectionHeight +
            stockBarHeight +
            stockLabelHeight
}

private fun ProductCardFlashSaleModel.getPdpViewCountSectionHeight(context: Context): Int {
    return if (pdpViewCount.isNotEmpty()) {
        val pdpViewCountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_height)
        val pdpViewCountMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_marginbottom)

        return pdpViewCountMarginBottom + pdpViewCountHeight
    }
    else 0
}


private fun ProductCardFlashSaleModel.getProductNameSectionHeight(context: Context): Int {
    return if (productName.isNotEmpty()) {
        val productNameMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_text_view_product_name_margin_bottom)
        val productNameHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_height)

        return productNameMarginBottom + productNameHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getPromoSectionHeight(context: Context): Int {
    val labelPrice = getLabelPrice()

    var labelDiscountMarginBottom = 0
    var labelDiscountHeight = 0
    var labelPriceMarginBottom = 0
    var labelPriceHeight = 0

    if (discountPercentage.isNotEmpty() || slashedPrice.isNotEmpty()) {
        labelDiscountMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_discount_margin_bottom)
        labelDiscountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_height)
    }

    if (labelPrice != null && labelPrice.title.isNotEmpty()) {
        labelPriceMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_text_view_slashed_price_margin_bottom)
        labelPriceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_height)
    }

    return max(labelDiscountMarginBottom + labelDiscountHeight, labelPriceMarginBottom + labelPriceHeight)
}

private fun ProductCardFlashSaleModel.getPriceSectionHeight(context: Context): Int {
    return if (priceRange.isNotEmpty() || formattedPrice.isNotEmpty()) {
        var priceMarginBottom = 0
        if (stockBarLabel.isNotEmpty()) {
            priceMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_text_view_price_margin_bottom)
        }
        val priceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_height)

        return priceMarginBottom + priceHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getStockBarSectionHeight(context: Context): Int {
    return if (stockBarLabel.isNotEmpty()) {
        val stockBarMarginBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_marginbottom)
        val stockBarHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_height)

        return stockBarMarginBottom + stockBarHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getStockLabelHeight(context: Context): Int {
    return if (stockBarLabel.isNotEmpty()) {
        val labelHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_height)

        return labelHeight
    }
    else 0
}