package com.tokopedia.productcard.reimagine

import android.content.Context
import androidx.annotation.DimenRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.tokopedia.productcard.R as productcardR

suspend fun List<ProductCardModel>?.getMaxHeightForGridCarouselView(
    context: Context?,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    productImageWidth: Int = context.getPixel(productcardR.dimen.product_card_reimagine_carousel_width),
): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val maxHeight = maxOfOrNull { productCardModel ->
            val productCardComponentHeightList = listOf(
                productImageWidth,
                gridCarouselCardPaddingBottom(context),
                gridCarouselStockInfoHeight(context, productCardModel),
                gridCarouselNameHeight(context, productCardModel),
                gridCarouselPriceHeight(context, productCardModel),
                gridCarouselDiscountBelowPriceHeight(context, productCardModel),
                gridCarouselLabelBenefitHeight(context, productCardModel),
                gridCarouselCredibilitySectionHeight(context, productCardModel),
                gridCarouselShopSectionHeight(context, productCardModel),
                gridCarouselFreeShippingHeight(context, productCardModel),
            )

            val productCardHeight = productCardComponentHeightList.sum()

            Timber.d(
                "Product Card Components Height List: %s; Total: %s",
                productCardComponentHeightList.joinToString(separator = ", "),
                productCardHeight.toString(),
            )

            productCardHeight
        }?.toInt() ?: 0

        Timber.d("Product Card Max Height: %s", maxHeight.toString())

        maxHeight
    }
}

private fun gridCarouselCardPaddingBottom(context: Context?) =
    context.getPixel(productcardR.dimen.product_card_reimagine_carousel_padding_bottom)

private fun gridCarouselStockInfoHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.stockInfo() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_stock_bar_background_height)
    else 0

private fun gridCarouselNameHeight(context: Context?, productCardModel: ProductCardModel): Int {
    val nameLineHeight =
        if (productCardModel.hasMultilineName) productcardR.dimen.product_card_reimagine_name_2_line_height
        else productcardR.dimen.product_card_reimagine_name_1_line_height

    return context.getPixel(productcardR.dimen.product_card_reimagine_name_image_margin_top)
        .plus(context.getPixel(nameLineHeight))
}

private fun gridCarouselPriceHeight(context: Context?, productCardModel: ProductCardModel): Int {
    val priceHeight =
        if (productCardModel.price.isNotEmpty())
            context.getPixel(productcardR.dimen.product_card_reimagine_price_margin_top)
                .plus(context.getPixel(productcardR.dimen.product_card_reimagine_price_height))
        else 0

    val discountSectionHeight =
        if (productCardModel.hasRibbon())
            gridCarouselDiscountHeight(context, productCardModel)
        else 0

    return maxOf(priceHeight, discountSectionHeight)
}

private fun gridCarouselDiscountHeight(context: Context?, productCardModel: ProductCardModel): Int =
    maxOf(
        gridCarouselSlashedPriceHeight(context, productCardModel),
        gridCarouselDiscountPercentageHeight(
            context,
            productCardModel,
        ),
    )

private fun gridCarouselSlashedPriceHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.slashedPrice.isNotEmpty())
        context.getPixel(productcardR.dimen.product_card_reimagine_slashed_price_height)
    else 0

private fun gridCarouselDiscountPercentageHeight(context: Context?, productCardModel: ProductCardModel) =
    if (productCardModel.discountPercentage != 0)
        context.getPixel(productcardR.dimen.product_card_reimagine_discount_percentage_height)
    else 0

private fun gridCarouselDiscountBelowPriceHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int {
    val hasDiscountSection =
        productCardModel.slashedPrice.isNotEmpty() || productCardModel.discountPercentage != 0
    val hasDiscountSectionBelowPrice = !productCardModel.hasRibbon() && hasDiscountSection

    return if (hasDiscountSectionBelowPrice)
        context.getPixel(productcardR.dimen.product_card_reimagine_slashed_price_margin_top)
            .plus(gridCarouselDiscountHeight(context, productCardModel))
    else 0
}

private fun gridCarouselLabelBenefitHeight(context: Context?, productCardModel: ProductCardModel): Int =
    if (productCardModel.labelBenefit() != null)
        context.getPixel(productcardR.dimen.product_card_reimagine_label_benefit_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_label_benefit_height))
    else 0

private fun gridCarouselCredibilitySectionHeight(
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

private fun gridCarouselShopSectionHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int =
    if (productCardModel.shopBadge.hasTitle())
        context.getPixel(productcardR.dimen.product_card_reimagine_shop_section_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_shop_section_height))
    else 0

private fun gridCarouselFreeShippingHeight(
    context: Context?,
    productCardModel: ProductCardModel
): Int {
    return if (productCardModel.freeShipping.imageUrl.isNotEmpty())
        context.getPixel(productcardR.dimen.product_card_reimagine_bebas_ongkir_margin_top)
            .plus(context.getPixel(productcardR.dimen.product_card_reimagine_bebas_ongkir_height))
    else 0
}

private fun Context?.getPixel(@DimenRes id: Int): Int =
    this?.resources?.getDimensionPixelSize(id) ?: 0
