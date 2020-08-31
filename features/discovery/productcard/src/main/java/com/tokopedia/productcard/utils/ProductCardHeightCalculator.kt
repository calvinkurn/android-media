@file:JvmName("ProductCardHeightCalculator")
package com.tokopedia.productcard.utils

import android.content.Context
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.max

suspend fun List<ProductCardModel>?.getMaxHeightForGridView(context: Context?, coroutineDispatcher: CoroutineDispatcher, productImageWidth: Int): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val productCardHeightList = mutableListOf<Int>()

        forEach { productCardModel ->
            val imageHeight = productImageWidth
            val cardPaddingBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_bottom)
            val contentMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)
            val contentHeight = productCardModel.getContentHeight(context)
            val buttonAddToCartSectionHeight = productCardModel.getButtonAddToCartSectionHeight(context)

            productCardHeightList.add(imageHeight + cardPaddingBottom + contentMarginTop + contentHeight + buttonAddToCartSectionHeight)
        }

        productCardHeightList.max()?.toInt() ?: 0
    }
}

suspend fun List<ProductCardModel>?.getMaxHeightForListView(context: Context?, coroutineDispatcher: CoroutineDispatcher): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val productCardHeightList = mutableListOf<Int>()
        forEach { productCardModel ->
            val cardPaddingTop = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_top)
            val cardPaddingBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_bottom)
            val imageSize = context.resources.getDimensionPixelSize(R.dimen.product_card_list_image_size)
            val contentHeight = productCardModel.getContentHeight(context)
            val buttonDeleteProductSectionHeight = productCardModel.getButtonDeleteProductSectionHeight(context)
            val buttonAddToCartSectionHeight = productCardModel.getButtonAddToCartSectionHeight(context)

            val totalHeight = cardPaddingTop + cardPaddingBottom + max(imageSize, contentHeight) + buttonDeleteProductSectionHeight + buttonAddToCartSectionHeight

            productCardHeightList.add(totalHeight)
        }

        productCardHeightList.max()?.toInt() ?: 0
    }
}

private fun ProductCardModel.getContentHeight(context: Context): Int {
    val gimmickSectionHeight = getGimmickSectionHeight(context)
    val pdpViewCountHeight = getPdpViewCountSectionHeight(context)
    val productNameSectionHeight = getProductNameSectionHeight(context)
    val promoSectionHeight = getPromoSectionHeight(context)
    val priceSectionHeight = getPriceSectionHeight(context)
    val shopInfoSectionHeight = getShopInfoSectionHeight(context)
    val credibilitySectionHeight = getCredibilitySectionHeight(context)
    val shippingInfoSectionHeight = getShippingInfoSectionHeight(context)
    val stockBarHeight = getStockBarAndLabelSectionHeight(context)

    return gimmickSectionHeight +
            pdpViewCountHeight+
            productNameSectionHeight +
            promoSectionHeight +
            priceSectionHeight +
            shopInfoSectionHeight +
            credibilitySectionHeight +
            shippingInfoSectionHeight +
            stockBarHeight
}

private fun ProductCardModel.getGimmickSectionHeight(context: Context): Int {
    val labelGimmick = getLabelGimmick()

    return if (labelGimmick != null && labelGimmick.title.isNotEmpty()) {
        context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_gimmick_height)
    } else 0
}

private fun ProductCardModel.getProductNameSectionHeight(context: Context): Int {
    return if (productName.isNotEmpty()) {
        val productNameMarginTop = getProductNameMarginTop(context)
        val productNameHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_height)

        return productNameMarginTop + productNameHeight
    }
    else 0
}

private fun ProductCardModel.getProductNameMarginTop(context: Context): Int {
    val labelGimmick = getLabelGimmick()

    return if (labelGimmick != null && labelGimmick.title.isNotEmpty()) {
        context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_margin_top)
    } else {
        context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_product_name_gone_margin_top)
    }
}

private fun ProductCardModel.getPromoSectionHeight(context: Context): Int {
    val labelPrice = getLabelPrice()

    var labelDiscountMarginTop = 0
    var labelDiscountHeight = 0
    var labelPriceMarginTop = 0
    var labelPriceHeight = 0

    if (discountPercentage.isNotEmpty() || slashedPrice.isNotEmpty()) {
        labelDiscountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_margin_top)
        labelDiscountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_height)
    }

    if (labelPrice != null && labelPrice.title.isNotEmpty()) {
        labelPriceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_margin_top)
        labelPriceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_height)
    }

    return max(labelDiscountMarginTop + labelDiscountHeight, labelPriceMarginTop + labelPriceHeight)
}

private fun ProductCardModel.getPriceSectionHeight(context: Context): Int {
    return if (priceRange.isNotEmpty() || formattedPrice.isNotEmpty()) {
        val priceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_margin_top)
        val priceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_height)

        return priceMarginTop + priceHeight
    }
    else 0
}

private fun ProductCardModel.getShopInfoSectionHeight(context: Context): Int {
    var shopBadgeMarginTop = 0
    var shopBadgeSize = 0
    var shopLocationMarginTop = 0
    var shopLocationHeight = 0

    if (isShowShopBadge()) {
        shopBadgeMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_margin_top)
        shopBadgeSize = context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_size)
    }

    if (shopLocation.isNotEmpty()) {
        shopLocationMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_margin_top)
        shopLocationHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_height)
    }

    return max(shopBadgeMarginTop + shopBadgeSize, shopLocationMarginTop + shopLocationHeight)
}

private fun ProductCardModel.getCredibilitySectionHeight(context: Context): Int {
    val credibilitySectionHeightList = mutableListOf<Int>().also {
        it.add(getRatingHeight(context))
        it.add(getReviewCountHeight(context))
        it.add(getLabelIntegrityHeight(context))
    }

    return credibilitySectionHeightList.max() ?: 0
}

private fun ProductCardModel.getRatingHeight(context: Context): Int {
    return when {
        !willShowRatingAndReviewCount() -> {
            0
        }
        ratingString.isNotEmpty() -> {
            val textRatingStringMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_rating_string_margin_top)
            val textRatingStringHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_rating_string_height)

            textRatingStringMarginTop + textRatingStringHeight
        }
        ratingCount > 0 -> {
            val imageRatingStarMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_image_rating_star_margin_top)
            val imageRatingStarHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_image_rating_star_height)

            imageRatingStarMarginTop + imageRatingStarHeight
        }
        else -> {
            0
        }
    }
}

private fun ProductCardModel.getReviewCountHeight(context: Context): Int {
    return if (willShowRatingAndReviewCount()) {
        val textReviewCountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_review_count_margin_top)
        val textReviewCountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_review_count_height)

        textReviewCountMarginTop + textReviewCountHeight
    }
    else 0
}

private fun ProductCardModel.getLabelIntegrityHeight(context: Context): Int {
    val labelIntegrity = getLabelIntegrity()

    return if (labelIntegrity != null && labelIntegrity.title.isNotEmpty() && !willShowRatingAndReviewCount()) {
        val labelCredibilityMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_integrity_margin_top)
        val labelCredibilityHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_integrity_height)

        labelCredibilityMarginTop + labelCredibilityHeight
    }
    else 0
}

private fun ProductCardModel.getShippingInfoSectionHeight(context: Context): Int {
    val freeOngkirBadgeHeight = getFreeOngkirBadgeHeight(context)
    val labelShippingHeight = getLabelShippingHeight(context)

    return max(freeOngkirBadgeHeight, labelShippingHeight)
}

private fun ProductCardModel.getFreeOngkirBadgeHeight(context: Context): Int {
    return if (isShowFreeOngkirBadge()) {
        val freeOngkirBadgeMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_free_ongkir_badge_margin_top)
        val freeOngkirBadgeHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_free_ongkir_badge_height)

        freeOngkirBadgeMarginTop + freeOngkirBadgeHeight
    }
    else 0
}

private fun ProductCardModel.getLabelShippingHeight(context: Context): Int {
    val labelShipping = getLabelShipping()

    return if (labelShipping != null && labelShipping.title.isNotEmpty() && !isShowFreeOngkirBadge()) {
        val labelShippingMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_shipping_margin_top)
        val labelShippingHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_shipping_height)

        labelShippingMarginTop + labelShippingHeight
    }
    else 0
}

private fun ProductCardModel.getButtonDeleteProductSectionHeight(context: Context): Int {
    return if(hasDeleteProductButton) {
        val buttonDeleteProductMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_button_delete_product_margin_top)
        val buttonDeleteProductHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_delete_product_height)

        buttonDeleteProductMarginTop + buttonDeleteProductHeight
    }
    else 0
}

private fun ProductCardModel.getButtonAddToCartSectionHeight(context: Context): Int {
    return if (hasAddToCartButton) {
        val buttonAddToCartMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_to_cart_margin_top)
        val buttonAddToCartHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_to_cart_height)

        buttonAddToCartMarginTop + buttonAddToCartHeight
    }
    else 0
}

private fun ProductCardModel.getPdpViewCountSectionHeight(context: Context): Int {
    return if (pdpViewCount.isNotEmpty()) {
        val pdpViewCountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_height)
        val pdpViewCountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_pdpcount_margintop)

        return pdpViewCountMarginTop + pdpViewCountHeight
    }
    else 0
}

private fun ProductCardModel.getStockBarAndLabelSectionHeight(context: Context): Int {
    return if (stockBarLabel.isNotEmpty()) {
        val stockBarMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_margintop)
        val stockBarHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_height)

        val labelHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_height)
        val labelMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_margintop)

        return stockBarMarginTop + stockBarHeight +  labelHeight + labelMarginTop
    }
    else 0
}