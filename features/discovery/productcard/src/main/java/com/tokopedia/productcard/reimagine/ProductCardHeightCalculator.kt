package com.tokopedia.productcard.reimagine

import android.content.Context
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.tokopedia.productcard.R as productcardR

suspend fun List<ProductCardModel>?.getMaxHeightForGridCarouselView(
    context: Context?,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    productImageWidth: Int = context.getPixel(productcardR.dimen.product_card_reimagine_grid_carousel_width),
): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val maxHeight = maxOfOrNull { productCardModel ->
            productCardGridCarouselHeight(context, productCardModel, productImageWidth)
        }?.toInt() ?: 0

        Timber.d("Product Card Grid Max Height: %s", maxHeight.toString())

        maxHeight
    }
}

internal fun productCardGridCarouselHeight(
    context: Context?,
    productCardModel: ProductCardModel,
    productImageWidth: Int = context.getPixel(productcardR.dimen.product_card_reimagine_grid_carousel_width),
): Int {
    val productCardComponentHeightList = listOf(
        productImageWidth,
        gridCarouselCardPaddingBottom(context),
        stockInfoHeight(context, productCardModel),
        gridCarouselNameHeight(context, productCardModel),
        priceSectionHeight(context, productCardModel),
        discountSectionBelowPriceHeight(context, productCardModel),
        benefitSectionHeight(context, productCardModel),
        credibilitySectionHeight(context, productCardModel),
        shopSectionHeight(context, productCardModel),
        addToCartHeight(context, productCardModel),
    )

    val productCardHeight = productCardComponentHeightList.sum()

    Timber.d(
        "Product Card Components Height List: %s; Total: %s",
        productCardComponentHeightList.joinToString(separator = ", "),
        productCardHeight.toString(),
    )

    return productCardHeight
}

private fun gridCarouselCardPaddingBottom(context: Context?) =
    context.getPixel(productcardR.dimen.product_card_reimagine_carousel_padding_bottom)

private fun gridCarouselNameHeight(context: Context?, productCardModel: ProductCardModel): Int {
    val nameLineHeight = productcardR.dimen.product_card_reimagine_name_1_line_height

    return context.getPixel(productcardR.dimen.product_card_reimagine_name_image_margin_top)
        .plus(context.getPixel(nameLineHeight))
        .plus(labelAssignedValueAdditionalHeight(context, productCardModel))
}

suspend fun List<ProductCardModel>?.getMaxHeightForListCarouselView(
    context: Context?,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val maxHeight = maxOfOrNull { productCardModel ->
            productCardListCarouselHeight(context, productCardModel)
        }?.toInt() ?: 0

        Timber.d("Product Card List Max Height: %s", maxHeight.toString())

        maxHeight
    }
}

internal fun productCardListCarouselHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int {
    val productCardImageHeight =
        context.getPixel(productcardR.dimen.product_card_reimagine_list_carousel_image_size)
    val productCardStockInfoHeight = stockInfoHeight(context, productCardModel)

    val productCardComponentHeightList = listOf(
        listCarouselNameHeight(context, productCardModel),
        priceSectionHeight(context, productCardModel),
        discountSectionBelowPriceHeight(context, productCardModel),
        benefitSectionHeight(context, productCardModel),
        credibilitySectionHeight(context, productCardModel),
        shopSectionHeight(context, productCardModel),
        addToCartHeight(context, productCardModel),
    )

    val productCardComponentHeight = productCardComponentHeightList.sum()
    val productCardHeight = maxOf(
        productCardImageHeight + productCardStockInfoHeight,
        productCardComponentHeight
    )

    Timber.d(
        "Product Card Components Height List: %s; Total Component Height: %s; Final Height: %s",
        productCardComponentHeightList.joinToString(separator = ", "),
        productCardComponentHeight.toString(),
        productCardHeight
    )

    return productCardHeight
}

private fun listCarouselNameHeight(context: Context?, productCardModel: ProductCardModel) =
    context.getPixel(productcardR.dimen.product_card_reimagine_name_2_line_height)
        .plus(labelAssignedValueAdditionalHeight(context, productCardModel))

private fun labelAssignedValueAdditionalHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.labelAssignedValue() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_label_assigned_value_additional_height)
    else 0

private fun stockInfoHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.stockInfo() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_stock_bar_background_height)
    else 0

private fun priceSectionHeight(context: Context?, productCardModel: ProductCardModel): Int {
    val priceHeight =
        context.getPixel(productcardR.dimen.product_card_reimagine_price_margin_top)
            .plus(priceHeight(context, productCardModel))
            .plus(nettPriceHeight(context, productCardModel))
            .plus(1.toPx()) // Unknown missing 1px

    val discountSectionHeight =
        if (productCardModel.hasRibbon())
            discountSectionHeight(context, productCardModel)
        else 0

    return maxOf(priceHeight, discountSectionHeight)
}

private fun priceHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.showPrice())
        context.getPixel(productcardR.dimen.product_card_reimagine_price_height)
    else 0

private fun nettPriceHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.labelNettPrice() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_nett_price_height)
    else 0

private fun discountSectionHeight(context: Context?, productCardModel: ProductCardModel): Int =
    maxOf(
        slashedPriceHeight(context, productCardModel),
        discountPercentageHeight(
            context,
            productCardModel,
        ),
    )

private fun slashedPriceHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.slashedPrice.isNotEmpty())
        context.getPixel(productcardR.dimen.product_card_reimagine_slashed_price_height)
    else 0

private fun discountPercentageHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.discountPercentage != 0)
        context.getPixel(productcardR.dimen.product_card_reimagine_discount_percentage_height)
    else 0

private fun discountSectionBelowPriceHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int {
    val hasDiscountSection =
        productCardModel.slashedPrice.isNotEmpty() || productCardModel.discountPercentage != 0
    val hasDiscountSectionBelowPrice = !productCardModel.hasRibbon() && hasDiscountSection

    return if (hasDiscountSectionBelowPrice)
        context.getPixel(productcardR.dimen.product_card_reimagine_slashed_price_margin_top)
            .plus(discountSectionHeight(context, productCardModel))
    else 0
}

private fun benefitSectionHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.labelBenefit() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_label_benefit_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_label_benefit_height))
    else if (productCardModel.labelProductOffer() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_label_offer_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_label_offer_height))
    else 0

private fun credibilitySectionHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int {
    val hasRating = productCardModel.rating.isNotEmpty()
    val labelCredibility = productCardModel.labelCredibility()
    val hasLabelCredibility = labelCredibility != null
    val hasCredibilitySection = hasRating || hasLabelCredibility

    return if (hasCredibilitySection)
        context.getPixel(productcardR.dimen.product_card_reimagine_credibility_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_credibility_height))
    else 0
}

private fun shopSectionHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.shopBadge.hasTitle())
        context.getPixel(productcardR.dimen.product_card_reimagine_shop_section_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_shop_section_height))
    else 0

private fun addToCartHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.hasAddToCart)
        context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_height))
    else 0
