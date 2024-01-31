package com.tokopedia.productcard.reimagine

import android.content.Context
import com.tokopedia.productcard.R
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
    useCompatPadding: Boolean = false,
): Int {
    if (this == null || context == null || this.isEmpty()) return 0

    return withContext(coroutineDispatcher) {
        val compatPaddingTopBottomMargin = compatPaddingTopBottomMargin(useCompatPadding, context)
        val maxHeight = maxOf { productCardGridCarouselHeight(context, it, productImageWidth) }.toInt()
            .plus(compatPaddingTopBottomMargin)

        Timber.d(
            "Product Card Grid Max Height: %s; useCompatPadding: %s",
            maxHeight.toString(),
            useCompatPadding.toString(),
        )

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
        gridCarouselNameHeight(context),
        priceSectionHeight(context, productCardModel),
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
    context.getPixel(productcardR.dimen.product_card_reimagine_padding_bottom)

private fun gridCarouselNameHeight(context: Context?): Int =
    context.getPixel(productcardR.dimen.product_card_reimagine_name_image_margin_top)
        .plus(context.getPixel(productcardR.dimen.product_card_reimagine_name_1_line_height))

private fun compatPaddingTopBottomMargin(useCompatPadding: Boolean, context: Context?) =
    if (useCompatPadding)
        2 * context.getPixel(productcardR.dimen.product_card_reimagine_use_compat_padding_size)
    else 0

suspend fun List<ProductCardModel>?.getMaxHeightForListCarouselView(
    context: Context?,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    useCompatPadding: Boolean = false,
): Int {
    if (this == null || context == null || this.isEmpty()) return 0

    return withContext(coroutineDispatcher) {
        val compatPaddingTopBottomMargin = compatPaddingTopBottomMargin(useCompatPadding, context)

        val maxHeight =
            maxOf { productCardListCarouselHeight(context, it) }.toInt()
                .plus(compatPaddingTopBottomMargin)

        Timber.d(
            "Product Card List Max Height: %s; useCompatPadding: %s",
            maxHeight.toString(),
            useCompatPadding.toString(),
        )

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
        listCarouselNameHeight(context),
        priceSectionHeight(context, productCardModel),
        benefitSectionHeight(context, productCardModel),
        credibilitySectionHeight(context, productCardModel),
        shopSectionHeight(context, productCardModel),
    )

    val productCardComponentHeight = productCardComponentHeightList.sum()
    val productCardImageSectionHeight = productCardImageHeight + productCardStockInfoHeight

    val productCardHeight =
        listOf(
            maxOf(productCardImageSectionHeight, productCardComponentHeight),
            listCardPaddingInBackground(productCardModel, context),
            addToCartHeight(context, productCardModel),
        ).sum()

    Timber.d(
        "Product Card Components Height List: %s; Total Component Height: %s; Final Height: %s",
        productCardComponentHeightList.joinToString(separator = ", "),
        productCardComponentHeight.toString(),
        productCardHeight
    )

    return productCardHeight
}

private fun listCardPaddingInBackground(productCardModel: ProductCardModel, context: Context?) =
    if (productCardModel.isInBackground)
        context.getPixel(R.dimen.product_card_reimagine_content_guideline_padding_in_background) * 2
    else 0

private fun listCarouselNameHeight(context: Context?) =
    context.getPixel(productcardR.dimen.product_card_reimagine_name_1_line_height)

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
    if (productCardModel.showAddToCartButton())
        context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_height))
    else 0
