package com.tokopedia.productcard.reimagine

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.MIN_QUANTITY_NON_VARIANT
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup as LabelGroupReimagine

data class ProductCardModel(
    val imageUrl: String = "",
    val isAds: Boolean = false,
    val name: String = "",
    val price: String = "",
    val slashedPrice: String = "",
    val discountPercentage: Int = 0,
    val labelGroupList: List<LabelGroupReimagine> = listOf(),
    val rating: String = "",
    val shopBadge: ShopBadge = ShopBadge(),
    val hasAddToCart: Boolean = false,
    val videoUrl: String = "",
    val hasThreeDots: Boolean = false,
    val stockInfo: StockInfo = StockInfo(),
    val isSafeProduct: Boolean = false,
    val isInBackground: Boolean = false,
    val nonVariant: NonVariant? = null,
    val colorMode: ProductCardColor? = null 
) {

    fun labelBenefit(): LabelGroupReimagine? =
        labelGroup(LABEL_REIMAGINE_BENEFIT)?.takeIf(LabelGroupReimagine::hasTitle)

    fun labelCredibility(): LabelGroupReimagine? =
        labelGroup(LABEL_REIMAGINE_CREDIBILITY)?.takeIf(LabelGroupReimagine::hasTitle)

    fun labelAssignedValue(): LabelGroupReimagine? =
        labelGroup(LABEL_REIMAGINE_ASSIGNED_VALUE)?.takeIf(LabelGroupReimagine::hasImage)

    fun labelProductOffer(): LabelGroupReimagine? =
        labelGroup(LABEL_REIMAGINE_PRODUCT_OFFER)?.takeIf(LabelGroupReimagine::hasTitle)

    fun labelNettPrice(): LabelGroupReimagine? =
        labelGroup(LABEL_NETT_PRICE)?.takeIf(LabelGroupReimagine::hasTitle)

    fun labelPreventiveOverlay(): LabelGroupReimagine? =
        labelGroup(LABEL_PREVENTIVE_OVERLAY)?.takeIf {
            it.hasTitle()
                && labelPreventiveBlock() == null
        }

    fun labelPreventiveBlock(): LabelGroupReimagine? =
        labelGroup(LABEL_PREVENTIVE_BLOCK)?.takeIf(LabelGroupReimagine::hasTitle)

    fun labelOverlay1(): LabelGroupReimagine? =
        labelGroup(LABEL_OVERLAY_1)?.takeIf(LabelGroupReimagine::hasImage)

    fun labelOverlay2(): LabelGroupReimagine? =
        labelGroup(LABEL_OVERLAY_2)?.takeIf(LabelGroupReimagine::hasImage)

    fun labelOverlay3(): LabelGroupReimagine? =
        labelGroup(LABEL_OVERLAY_3)?.takeIf(LabelGroupReimagine::hasImage)

    fun ribbon(): LabelGroupReimagine? =
        labelGroup(LABEL_REIMAGINE_RIBBON)?.takeIf(LabelGroupReimagine::hasTitle)

    fun hasRibbon() = ribbon() != null

    fun showVideoIdentifier() = videoUrl.isNotBlank() && !isSafeProduct

    fun labelGroupOverlayList() = labelGroupList
        .filter { it.position.startsWith(LABEL_OVERLAY_) && it.hasImage() }
        .sortedBy { it.position }

    fun stockInfo() : StockInfo? = stockInfo.takeIf { it.hasTitle() }

    fun showPrice() = price.isNotBlank() && labelNettPrice() == null

    fun useQuantityEditor() = nonVariant != null

    fun showAddToCartNonVariant(): Boolean = nonVariant?.quantity == 0

    fun showQuantityEditor(): Boolean = (nonVariant?.quantity ?: 0) > 0

    fun showAddToCartButton(): Boolean = hasAddToCart || showAddToCartNonVariant()

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = "",
        val styles: List<Style> = listOf(),
    ) {
        private val style = styles.associate { it.key to it.value }

        fun hasTitle() = title.isNotBlank()
        fun hasImage() = imageUrl.isNotBlank()
        fun backgroundColor() = style[LabelGroupStyle.BACKGROUND_COLOR]
        fun backgroundOpacity() = style[LabelGroupStyle.BACKGROUND_OPACITY]?.toFloatOrNull()
        fun textColor() = style[LabelGroupStyle.TEXT_COLOR]
        fun outlineColor(): String? = style[LabelGroupStyle.OUTLINE_COLOR]
        fun width(): Int = style[LabelGroupStyle.WIDTH]?.toIntOrNull() ?: 0

        data class Style(val key: String = "", val value: String = "") {

            companion object {
                internal fun from(style: ProductCardModel.LabelGroup.Style): Style =
                    Style(style.key, style.value)
            }
        }

        companion object {
            internal fun from(labelGroup: ProductCardModel.LabelGroup): LabelGroupReimagine =
                LabelGroup(
                    position = labelGroup.position,
                    title = labelGroup.title,
                    type = labelGroup.type,
                    imageUrl = labelGroup.imageUrl,
                    styles = labelGroup.styleList.map(Style::from)
                )
        }
    }

    data class ShopBadge(
        val imageUrl: String = "",
        val title: String = "",
    ) {

        fun hasImage() = imageUrl.isNotEmpty()

        fun hasTitle() = title.isNotEmpty()

        companion object {
            internal fun from(shopBadge: ProductCardModel.ShopBadge?): ShopBadge =
                ShopBadge(
                    imageUrl = shopBadge?.imageUrl ?: "",
                    title = shopBadge?.title ?: "",
                )
        }
    }

    data class StockInfo(
        val percentage: Int = 0,
        val label: String = "",
        val labelColor: String = ""
    ) {
        fun hasTitle() = label.isNotEmpty()

        companion object {
            internal fun from(productCardModel: ProductCardModel): StockInfo =
                StockInfo(
                    percentage = productCardModel.stockBarPercentage,
                    label = productCardModel.stockBarLabel,
                    labelColor = productCardModel.stockBarLabelColor,
                )
        }
    }

    data class NonVariant(
        val quantity: Int = 0,
        internal val minQuantity: Int = 0,
        internal val maxQuantity: Int = 0,
    ) {
        val minQuantityFinal = maxOf(minQuantity, MIN_QUANTITY_NON_VARIANT)
        val maxQuantityFinal = maxOf(maxQuantity, minQuantityFinal)

        val quantityRange: IntRange
            get() = minQuantityFinal..maxQuantityFinal

        companion object {
            fun from(nonVariant: ProductCardModel.NonVariant?): NonVariant? {
                nonVariant ?: return null

                return NonVariant(
                    quantity = nonVariant.quantity,
                    minQuantity = nonVariant.minQuantity,
                    maxQuantity = nonVariant.maxQuantity,
                )
            }
        }
    }
    
    companion object {
        fun from(productCardModel: ProductCardModel): ProductCardModelReimagine =
            ProductCardModelReimagine(
                imageUrl = productCardModel.productImageUrl,
                isAds = productCardModel.isTopAds,
                name = productCardModel.productName,
                price = productCardModel.formattedPrice,
                slashedPrice = productCardModel.slashedPrice,
                discountPercentage = productCardModel.discountPercentageInt,
                labelGroupList = productCardModel.labelGroupList.map(LabelGroupReimagine::from),
                rating = productCardModel.countSoldRating,
                shopBadge = shopBadge(productCardModel),
                hasAddToCart = productCardModel.hasAddToCartButton,
                videoUrl = productCardModel.customVideoURL,
                hasThreeDots = productCardModel.hasThreeDots,
                stockInfo = StockInfo.from(productCardModel),
                isInBackground = productCardModel.isInBackground,
                nonVariant = NonVariant.from(productCardModel.nonVariant),
                colorMode = productCardModel.colorMode,
            )

        private fun shopBadge(productCardModel: ProductCardModel) =
            ShopBadge.from(productCardModel.shopBadgeList.find { it.isShown })
    }
}
