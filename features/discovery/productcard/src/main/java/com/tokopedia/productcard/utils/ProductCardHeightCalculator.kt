@file:JvmName("ProductCardHeightCalculator")
package com.tokopedia.productcard.utils

import android.content.Context
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.max

suspend fun List<ProductCardModel>?.getMaxHeightForGridView(context: Context?, coroutineDispatcher: CoroutineDispatcher, productImageWidth: Int): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val productCardHeightList = mutableListOf<Int>()

        forEach { productCardModel ->
            val imageHeight = productCardModel.layoutStrategy.getImageHeight(productImageWidth)
            val cardPaddingBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_bottom)

            val bestSellerLabelHeight =
                productCardModel.layoutStrategy.getLabelBestSellerHeight(context, productCardModel)

            val categoryBottomLabelHeight =
                productCardModel.layoutStrategy.getTextCategoryBottomHeight(
                    context,
                    productCardModel,
                )

            val hasLabelCampaign = productCardModel.isShowLabelCampaign()
            val campaignLabelHeight = getLabelCampaignHeight(context, hasLabelCampaign)

            val contentMarginTop =
                productCardModel.layoutStrategy.getGridViewContentMarginTop(context, productCardModel)
            val contentHeight = productCardModel.getContentHeightGrid(context)
            val buttonAddToCartSectionHeight = productCardModel.getButtonAddToCartSectionHeight(context)
            val buttonQuantityEditorSectionHeight = productCardModel.getQuantityEditorSectionHeight(context)
            val buttonVariantSectionHeight = productCardModel.getVariantSectionHeight(context)
            val buttonNotifyMeSectionHeight = productCardModel.getButtonNotifyMeSectionHeight(context)
            val buttonSimilarProductHeight = productCardModel.getButtonSimilarProductHeight(context)
            val buttonPrimaryWishlistHeight = productCardModel.getButtonPrimaryWishlistHeight(context)

            productCardHeightList.add(
                            imageHeight +
                            cardPaddingBottom +
                            bestSellerLabelHeight +
                            categoryBottomLabelHeight +
                            campaignLabelHeight +
                            contentMarginTop +
                            contentHeight +
                            buttonAddToCartSectionHeight +
                            buttonQuantityEditorSectionHeight +
                            buttonVariantSectionHeight +
                            buttonNotifyMeSectionHeight +
                            buttonSimilarProductHeight +
                            buttonPrimaryWishlistHeight
            )
        }

        productCardHeightList.maxOrNull()?.toInt() ?: 0
    }
}

private fun getLabelCampaignHeight(context: Context, hasLabelCampaign: Boolean): Int {
    return if (hasLabelCampaign)
        context.resources.getDimensionPixelSize(R.dimen.product_card_label_campaign_height)
    else 0
}

private fun getLabelBestSellerHeight(context: Context, hasLabelBestSeller: Boolean): Int {
    return if (hasLabelBestSeller)
        context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_height) +
                context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_margintop)
    else 0
}

private fun getTextCategoryBottomHeight(context: Context, hasLabelBestSeller: Boolean): Int {
    return if (hasLabelBestSeller)
        context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_category_bottom_height)
    else 0
}

suspend fun List<ProductCardModel>?.getMaxHeightForListView(context: Context?, coroutineDispatcher: CoroutineDispatcher): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val productCardHeightList = mutableListOf<Int>()
        forEach { productCardModel ->
            val cardPaddingTop = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_top)
            val cardPaddingBottom = context.resources.getDimensionPixelSize(R.dimen.product_card_padding_bottom)

            val hasLabelBestSeller = productCardModel.isShowLabelBestSeller()
            val bestSellerLabelHeight = getLabelBestSellerHeight(context, hasLabelBestSeller)

            val hasLabelCategoryBottom = productCardModel.isShowLabelCategoryBottom()
            val categoryBottomLabelHeight = getTextCategoryBottomHeight(context, hasLabelCategoryBottom)

            val hasLabelCampaign = productCardModel.isShowLabelCampaign()
            val campaignLabelHeight = getLabelCampaignHeight(context, hasLabelCampaign)

            val hasLabelCampaignOrBestSeller = hasLabelCampaign || hasLabelBestSeller
            val contentMarginTop = getListViewContentMarginTop(context, hasLabelCampaignOrBestSeller)
            val imageSize = context.resources.getDimensionPixelSize(R.dimen.product_card_list_image_size)
            val contentHeight = productCardModel.getContentHeightList(context)
            val buttonDeleteProductSectionHeight = productCardModel.getButtonDeleteProductSectionHeight(context)
            val buttonAddToCartSectionHeight = productCardModel.getButtonAddToCartSectionHeight(context)
            val buttonQuantityEditorSectionHeight = productCardModel.getQuantityEditorSectionHeight(context)
            val buttonVariantSectionHeight = productCardModel.getVariantSectionHeight(context)
            val buttonNotifyMeSectionHeight = productCardModel.getButtonNotifyMeSectionHeight(context)

            val totalHeight = cardPaddingTop +
                    bestSellerLabelHeight +
                    categoryBottomLabelHeight +
                    campaignLabelHeight +
                    contentMarginTop +
                    cardPaddingBottom +
                    max(imageSize, contentHeight) +
                    buttonDeleteProductSectionHeight +
                    buttonAddToCartSectionHeight +
                    buttonQuantityEditorSectionHeight +
                    buttonVariantSectionHeight +
                    buttonNotifyMeSectionHeight

            productCardHeightList.add(totalHeight)
        }

        productCardHeightList.maxOrNull()?.toInt() ?: 0
    }
}

private fun getListViewContentMarginTop(context: Context, hasLabelCampaignOrBestSeller: Boolean): Int {
    return if (hasLabelCampaignOrBestSeller)
        context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin_top)
    else 0
}

private fun ProductCardModel.getContentHeightGrid(context: Context): Int {
    val gimmickSectionHeight = layoutStrategy.getGimmickSectionHeight(context, this)
    val pdpViewCountHeight = getPdpViewCountSectionHeight(context)
    val productNameSectionHeight = getProductNameSectionHeight(context)
    val categoryCostPerUnitHeight = getCategoryCostPerUnitHeight(context)
    val priceSectionHeight = getPriceSectionHeight(context)
    val promoSectionHeight = getPromoSectionHeight(context)
    val shopInfoSectionHeight = getShopInfoSectionHeight(context)
    val credibilitySectionHeight = getCredibilitySectionHeight(context)
    val shopRatingSectionHeight = getShopRatingSectionHeight(context)
    val shippingInfoSectionHeight = getShippingInfoSectionHeight(context)
    val etaHeight = getLabelETA(context)
    val stockBarHeight = getStockBarAndLabelSectionHeight(context)

    return gimmickSectionHeight +
            pdpViewCountHeight+
            productNameSectionHeight +
            categoryCostPerUnitHeight +
            priceSectionHeight +
            promoSectionHeight +
            shopInfoSectionHeight +
            credibilitySectionHeight +
            shopRatingSectionHeight +
            shippingInfoSectionHeight +
            etaHeight +
            stockBarHeight
}

private fun ProductCardModel.getContentHeightList(context: Context): Int {
    val gimmickSectionHeight = getGimmickSectionHeight(context)
    val pdpViewCountHeight = getPdpViewCountSectionHeight(context)
    val productNameSectionHeight = getProductNameSectionHeight(context)
    val categoryCostPerUnitHeight = getCategoryCostPerUnitHeight(context)
    val priceSectionHeight = getPriceSectionHeight(context)
    val promoSectionHeight = getPromoSectionHeight(context)
    val shopInfoSectionHeight = getShopInfoSectionHeight(context)
    val credibilitySectionHeight = getCredibilitySectionHeight(context)
    val shopRatingSectionHeight = getShopRatingSectionHeight(context)
    val shippingInfoSectionHeight = getShippingInfoSectionHeight(context)
    val etaHeight = getLabelETA(context)
    val stockBarHeight = getStockBarAndLabelSectionHeight(context)

    return gimmickSectionHeight +
            pdpViewCountHeight+
            productNameSectionHeight +
            categoryCostPerUnitHeight +
            max(priceSectionHeight, promoSectionHeight) +
            shopInfoSectionHeight +
            credibilitySectionHeight +
            shopRatingSectionHeight +
            max(shippingInfoSectionHeight, etaHeight) +
            stockBarHeight +
            6.toPx() // small hack, unknown height missing from calculation
}

private fun ProductCardModel.getGimmickSectionHeight(context: Context): Int {
    return if (isShowLabelGimmick()) {
        val labelGimmick = getLabelGimmick()

        if (labelGimmick != null && labelGimmick.title.isNotEmpty()) context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_gimmick_height)
        else 0
    }
    else 0
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

private fun ProductCardModel.getCategoryCostPerUnitHeight(context: Context): Int {
    val labelCategory = getLabelCategorySectionHeight(context)
    val labelCostPerUnit = getLabelCostPerUnitSectionHeight(context)

    return max(labelCategory, labelCostPerUnit)
}

private fun ProductCardModel.getLabelCategorySectionHeight(context: Context): Int {
    return if (getLabelCategory()?.title?.isNotEmpty() == true) {
        val labelCategoryMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_category_margin_top)
        val labelCategoryHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_category_height)

        labelCategoryMarginTop + labelCategoryHeight
    } else 0
}

private fun ProductCardModel.getLabelCostPerUnitSectionHeight(context: Context): Int {
    return if (getLabelCostPerUnit()?.title?.isNotEmpty() == true) {
        val labelCategoryMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_category_margin_top)
        val labelCategoryHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_category_height)

        labelCategoryMarginTop + labelCategoryHeight
    } else 0
}

private fun ProductCardModel.getPriceSectionHeight(context: Context): Int {
    return if (priceRange.isNotEmpty() || formattedPrice.isNotEmpty()) {
        val priceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_margin_top)
        val priceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_price_height)

        return (priceMarginTop * 2) + // small hack
                priceHeight
    }
    else 0
}

private fun ProductCardModel.getPromoSectionHeight(context: Context): Int {
    val labelPrice = getLabelPrice()

    var labelDiscountMarginTop = 0
    var labelDiscountHeight = 0
    var labelPriceMarginTop = 0
    var labelPriceHeight = 0

    if (isShowDiscountOrSlashPrice()) {
        labelDiscountMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_margin_top)
        labelDiscountHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_discount_height)
    }

    if (labelPrice != null && labelPrice.title.isNotEmpty() && !isShowDiscountOrSlashPrice()) {
        labelPriceMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_margin_top)
        labelPriceHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_label_price_height)
    }

    return max(labelDiscountMarginTop + labelDiscountHeight, labelPriceMarginTop + labelPriceHeight)
}

private fun ProductCardModel.getShopInfoSectionHeight(context: Context): Int {
    val labelFulfillment = getLabelFulfillment()
    var shopBadgeMarginTop = 0
    var shopBadgeSize = 0
    var shopLocationMarginTop = 0
    var shopLocationHeight = 0
    var labelFulfillmentMarginTop = 0
    var labelFulfillmentHeight = 0

    if (isShowShopBadge()) {
        shopBadgeMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_margin_top)
        shopBadgeSize = context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_size)
    }

    if (shopLocation.isNotEmpty() && labelFulfillment == null) {
        shopLocationMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_margin_top)
        shopLocationHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_height)
        return max(shopBadgeMarginTop + shopBadgeSize, shopLocationMarginTop + shopLocationHeight)
    }

    if (labelFulfillment != null) {
        labelFulfillmentMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_fulfillment_margin_top)
        labelFulfillmentHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_fulfillment_height)
    }

    return max(shopBadgeMarginTop + shopBadgeSize, labelFulfillmentMarginTop + labelFulfillmentHeight)
}

private fun ProductCardModel.getCredibilitySectionHeight(context: Context): Int {
    val credibilitySectionHeightList = mutableListOf<Int>().also {
        it.add(getRatingHeight(context))
        it.add(getReviewCountHeight(context))
        it.add(getLabelIntegrityHeight(context))
        it.add(getSalesRatingFloatHeight(context))
    }

    return credibilitySectionHeightList.maxOrNull() ?: 0
}

private fun ProductCardModel.getRatingHeight(context: Context): Int {
    return when {
        !willShowRatingAndReviewCount() -> {
            0
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

private fun ProductCardModel.getSalesRatingFloatHeight(context: Context): Int {
    return if (willShowRating()) {
        val labelCredibilityMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_integrity_margin_top)
        val labelCredibilityHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_integrity_height)

        labelCredibilityMarginTop + labelCredibilityHeight
    }
    else 0
}

private fun ProductCardModel.getShopRatingSectionHeight(context: Context): Int {
    return if (isShowShopRating()) {
        val textShopRatingMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_rating_margin_top)
        val textShopRatingHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_rating_height)

        textShopRatingMarginTop + textShopRatingHeight
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

private fun ProductCardModel.getLabelETA(context: Context): Int {
    val labelETA = getLabelETA()

    return if (labelETA != null && labelETA.title.isNotEmpty()) {
        val labelETAMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_eta_margin_top)
        val labelETAHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_eta_height)

        labelETAMarginTop + labelETAHeight
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
    return if (hasAddToCartButton || shouldShowAddToCartNonVariantQuantity()) {
        val buttonAddToCartMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_to_cart_margin_top)
        val buttonAddToCartHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_to_cart_height)

        buttonAddToCartMarginTop + buttonAddToCartHeight
    }
    else 0
}

private fun ProductCardModel.getQuantityEditorSectionHeight(context: Context): Int {
    return if (shouldShowCartEditorComponent()) {
        val quantityEditorMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_quantity_editor_margin_top)
        val quantityEditorHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_quantity_editor_height)

        quantityEditorMarginTop + quantityEditorHeight
    }
    else 0
}

private fun ProductCardModel.getVariantSectionHeight(context: Context): Int {
    return if (hasVariant()) {
        val addVariantButtonMarginTop =
            context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_variant_margin_top)

        val addVariantButtonHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_add_variant_height)

        addVariantButtonMarginTop + addVariantButtonHeight
    }
    else 0
}

private fun ProductCardModel.getButtonNotifyMeSectionHeight(context: Context): Int {
    return if (hasNotifyMeButton) {
        val buttonNotifyMeMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_button_notify_me_margin_top)
        val buttonNotifyMeHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_notify_me_height)

        buttonNotifyMeMarginTop + buttonNotifyMeHeight
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
    return if (isStockBarShown()) {
        val stockBarMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_margintop)
        val stockBarHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_progressbar_height)

        val labelHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_height)
        val labelMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_flashsale_label_margintop)

        return stockBarMarginTop + stockBarHeight +  labelHeight + labelMarginTop
    }
    else 0
}

private fun ProductCardModel.getButtonSimilarProductHeight(context: Context): Int {
    return if (hasNotifyMeButton) {
        val buttonSimilarProductMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_button_similar_product_margin_top)
        val buttonSimilarProductHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_similar_product_height)

        buttonSimilarProductMarginTop + buttonSimilarProductHeight
    }
    else 0
}

private fun ProductCardModel.getButtonPrimaryWishlistHeight(context: Context): Int {
    return if (willShowPrimaryButtonWishlist()) {
        val buttonPrimaryWishlistMarginTop = context.resources.getDimensionPixelOffset(R.dimen.product_card_button_primary_margin)
        val buttonPrimaryWishlistHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_button_primary_height)

        buttonPrimaryWishlistMarginTop + buttonPrimaryWishlistHeight
    }
    else 0
}
