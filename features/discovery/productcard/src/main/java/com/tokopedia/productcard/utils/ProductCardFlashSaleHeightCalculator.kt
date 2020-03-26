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
        val pdpViewCountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_margintop)
        val pdpViewCountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_height)

        return pdpViewCountMarginTop + pdpViewCountHeight
    }
    else 0
}


private fun ProductCardFlashSaleModel.getProductNameSectionHeight(context: Context): Int {
    return if (productName.isNotEmpty()) {
        val productNameMarginTop = getProductNameMarginTop(context)
        val productNameHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_height)

        return productNameMarginTop + productNameHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getProductNameMarginTop(context: Context): Int {
    val labelGimmick = getLabelGimmick()

    return if (labelGimmick != null && labelGimmick.title.isNotEmpty()) {
        context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_margin_top)
    } else {
        context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_gone_margin_top)
    }
}

private fun ProductCardFlashSaleModel.getPromoSectionHeight(context: Context): Int {
    val labelPrice = getLabelPrice()

    var labelDiscountMarginTop = 0
    var labelDiscountHeight = 0
    var labelPriceMarginTop = 0
    var labelPriceHeight = 0

    if (discountPercentage.isNotEmpty()) {
        labelDiscountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_margin_top)
        labelDiscountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_height)
    }

    if (labelPrice != null && labelPrice.title.isNotEmpty()) {
        labelPriceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_margin_top)
        labelPriceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_height)
    }

    return max(labelDiscountMarginTop + labelDiscountHeight, labelPriceMarginTop + labelPriceHeight)
}

private fun ProductCardFlashSaleModel.getPriceSectionHeight(context: Context): Int {
    return if (priceRange.isNotEmpty() || formattedPrice.isNotEmpty()) {
        val priceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_margin_top)
        val priceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_height)

        return priceMarginTop + priceHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getStockBarSectionHeight(context: Context): Int {
    return if (stockBarPercentage != 0) {
        val stockBarMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_margintop)
        val stockBarHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_height)

        return stockBarMarginTop + stockBarHeight
    }
    else 0
}

private fun ProductCardFlashSaleModel.getStockLabelHeight(context: Context): Int {
    return if (stockBarPercentage != 0) {
        val labelMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_margintop)
        val labelHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_height)

        return labelMarginTop + labelHeight
    }
    else 0
}