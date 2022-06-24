package com.tokopedia.productcard

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.fashion.FashionStrategy
import com.tokopedia.productcard.fashion.FashionStrategyControl
import com.tokopedia.productcard.fashion.FashionStrategyLongImage
import com.tokopedia.productcard.fashion.FashionStrategyNoCampaign
import com.tokopedia.productcard.fashion.FashionStrategyReposition
import com.tokopedia.productcard.utils.EXTRA_CHAR_SPACE
import com.tokopedia.productcard.utils.LABEL_BEST_SELLER
import com.tokopedia.productcard.utils.LABEL_CAMPAIGN
import com.tokopedia.productcard.utils.LABEL_CATEGORY
import com.tokopedia.productcard.utils.LABEL_CATEGORY_BOTTOM
import com.tokopedia.productcard.utils.LABEL_CATEGORY_SIDE
import com.tokopedia.productcard.utils.LABEL_COST_PER_UNIT
import com.tokopedia.productcard.utils.LABEL_ETA
import com.tokopedia.productcard.utils.LABEL_FULFILLMENT
import com.tokopedia.productcard.utils.LABEL_GIMMICK
import com.tokopedia.productcard.utils.LABEL_INTEGRITY
import com.tokopedia.productcard.utils.LABEL_PRICE
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.LABEL_SHIPPING
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT
import com.tokopedia.productcard.utils.MAX_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_QUANTITY_NON_VARIANT
import com.tokopedia.productcard.utils.TYPE_VARIANT_COLOR
import com.tokopedia.productcard.utils.TYPE_VARIANT_CUSTOM
import com.tokopedia.productcard.utils.TYPE_VARIANT_SIZE
import com.tokopedia.unifycomponents.UnifyButton

data class ProductCardModel (
        val productImageUrl: String = "",
        @Deprecated("Cannot wishlist from product card anymore")
        var isWishlisted: Boolean = false,
        @Deprecated("Cannot wishlist from product card anymore")
        val isWishlistVisible: Boolean = false,
        @Deprecated("replace with labelGroupList")
        val labelPromo: Label = Label(),
        @Deprecated("No more shop image")
        val shopImageUrl: String = "",
        @Deprecated("No more shop name")
        val shopName: String = "",
        val productName: String = "",
        val discountPercentage: String = "",
        val slashedPrice: String = "",
        val priceRange: String = "",
        val formattedPrice: String = "",
        val shopBadgeList: List<ShopBadge> = listOf(),
        val shopLocation: String = "",
        val ratingCount: Int = 0,
        val reviewCount: Int = 0,
        @Deprecated("replace with labelGroupList")
        val labelCredibility: Label = Label(),
        @Deprecated("replace with labelGroupList")
        val labelOffers: Label = Label(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val isTopAds: Boolean = false,
        val ratingString: String = "",
        val hasThreeDots: Boolean = false,
        val labelGroupList: List<LabelGroup> = listOf(),
        val hasDeleteProductButton: Boolean = false,
        val hasAddToCartButton: Boolean = false,
        val hasRemoveFromWishlistButton: Boolean = false,
        val pdpViewCount: String = "",
        val stockBarLabel: String = "",
        val stockBarLabelColor: String = "",
        val stockBarPercentage: Int = 0,
        val isOutOfStock: Boolean = false,
        @Deprecated("determined from product card")
        val addToCardText: String = "",
        val shopRating: String = "",
        val isShopRatingYellow: Boolean = false,
        val countSoldRating: String = "",
        val hasNotifyMeButton: Boolean = false,
        val labelGroupVariantList: List<LabelGroupVariant> = listOf(),
        @Deprecated("determined from product card")
        val addToCartButtonType: Int = UnifyButton.Type.TRANSACTION,
        val isWideContent: Boolean = false,
        val variant: Variant? = null,
        val nonVariant: NonVariant? = null,
        val hasSimilarProductButton: Boolean = false,
        val hasButtonThreeDotsWishlist: Boolean = false,
        val hasAddToCartWishlist: Boolean = false,
        val hasSimilarProductWishlist: Boolean = false,
        val customVideoURL : String = "",
        val cardInteraction: Boolean = false,
        val productListType: ProductListType = ProductListType.LONG_IMAGE,
) {
    @Deprecated("replace with labelGroupList")
    var isProductSoldOut: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductPreOrder: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductWholesale: Boolean = false

    internal val fashionStrategy: FashionStrategy = when (productListType) {
        ProductListType.CONTROL -> FashionStrategyControl()
        ProductListType.REPOSITION -> FashionStrategyReposition()
        ProductListType.LONG_IMAGE -> FashionStrategyLongImage()
        ProductListType.NO_CAMPAIGN -> FashionStrategyNoCampaign()
    }

    val hasVideo : Boolean = customVideoURL.isNotBlank()

    @Deprecated("replace with LabelGroup")
    data class Label(
            val position: String = "",
            val title: String = "",
            val type: String = ""
    )

    data class FreeOngkir(
            val isActive: Boolean = false,
            val imageUrl: String = ""
    )

    data class ShopBadge(
            val isShown: Boolean = true,
            val imageUrl: String = ""
    )

    data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val imageUrl: String = ""
    ) {
        fun isGimmick() = position == LABEL_GIMMICK
    }

    data class LabelGroupVariant(
            val typeVariant: String = "",
            var title: String = "",
            val type: String = "",
            val hexColor: String = ""
    ) {
        fun isColor() = typeVariant == TYPE_VARIANT_COLOR
        fun isSize() = typeVariant == TYPE_VARIANT_SIZE
        fun isCustom() = typeVariant == TYPE_VARIANT_CUSTOM
    }

    data class Variant(
            val quantity: Int = 0,
    )

    fun hasVariant(): Boolean {
        return variant != null
    }

    fun hasVariantWithQuantity(): Boolean {
        return variant?.quantity ?: 0 > 0
    }

    data class NonVariant(
            val quantity: Int = 0,
            private val minQuantity: Int = 0,
            private val maxQuantity: Int = 0,
    ) {
        val minQuantityFinal = maxOf(minQuantity, MIN_QUANTITY_NON_VARIANT)
        val maxQuantityFinal = maxOf(maxQuantity, this.minQuantityFinal)

        val quantityRange: IntRange
            get() = minQuantityFinal..maxQuantityFinal
    }

    fun shouldShowAddToCartNonVariantQuantity(): Boolean {
        return nonVariant?.quantity == 0
    }

    fun canShowQuantityEditor() = nonVariant != null

    fun shouldShowCartEditorComponent(): Boolean {
        return nonVariant?.quantity ?: 0 > 0
    }

    fun getLabelProductStatus(): LabelGroup? {
        return findLabelGroup(LABEL_PRODUCT_STATUS)
    }

    private fun findLabelGroup(position: String): LabelGroup? {
        return labelGroupList.find { it.position == position }
    }

    fun getLabelPrice(): LabelGroup? {
        return findLabelGroup(LABEL_PRICE)
    }

    fun getLabelGimmick(): LabelGroup? {
        return findLabelGroup(LABEL_GIMMICK)
    }

    fun getLabelIntegrity(): LabelGroup? {
        return findLabelGroup(LABEL_INTEGRITY)
    }

    fun getLabelShipping(): LabelGroup? {
        return findLabelGroup(LABEL_SHIPPING)
    }

    fun getLabelCampaign(): LabelGroup? {
        return findLabelGroup(LABEL_CAMPAIGN)
    }

    fun getLabelBestSeller(): LabelGroup? {
        return findLabelGroup(LABEL_BEST_SELLER)
    }

    fun getLabelCategorySide(): LabelGroup? {
        return findLabelGroup(LABEL_CATEGORY_SIDE)
    }

    fun getLabelCategoryBottom(): LabelGroup? {
        return findLabelGroup(LABEL_CATEGORY_BOTTOM)
    }

    fun getLabelETA(): LabelGroup? {
        return findLabelGroup(LABEL_ETA)
    }

    fun getLabelFulfillment(): LabelGroup? {
        return findLabelGroup(LABEL_FULFILLMENT)
    }

    fun getLabelCategory(): LabelGroup? {
        return findLabelGroup(LABEL_CATEGORY)
    }

    fun getLabelCostPerUnit(): LabelGroup? {
        return findLabelGroup(LABEL_COST_PER_UNIT)
    }

    fun willShowRatingAndReviewCount(): Boolean {
        return (ratingString.isNotEmpty() || ratingCount > 0) && reviewCount > 0 && !willShowRating()
    }

    fun willShowSalesAndRating(): Boolean{
        return countSoldRating.isNotEmpty() && getLabelIntegrity() != null
    }

    fun willShowRating(): Boolean{
        return countSoldRating.isNotEmpty()
    }

    fun isShowDiscountOrSlashPrice() = discountPercentage.isNotEmpty() || slashedPrice.isNotEmpty()

    fun isShowFreeOngkirBadge() = freeOngkir.isActive && freeOngkir.imageUrl.isNotEmpty()

    fun isShowShopBadge() = shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() } != null && shopLocation.isNotEmpty()

    fun isShowShopRating() = shopRating.isNotEmpty()

    fun isShowLabelBestSeller() = getLabelBestSeller()?.title?.isNotEmpty() == true

    fun isShowLabelCategorySide() =
        isShowLabelBestSeller() && getLabelCategorySide()?.title?.isNotEmpty() == true

    fun isShowLabelCategoryBottom() =
        isShowLabelBestSeller() && getLabelCategoryBottom()?.title?.isNotEmpty() == true

    fun isStockBarShown() = stockBarLabel.isNotEmpty() && !isOutOfStock

    fun isShowLabelCampaign(): Boolean {
        val labelCampaign = getLabelCampaign()

        return !isShowLabelBestSeller()
                && labelCampaign != null
                && labelCampaign.title.isNotEmpty()
                && labelCampaign.imageUrl.isNotEmpty()
    }

    fun isShowLabelGimmick() =
            !isShowLabelBestSeller()
                    && !isShowLabelCampaign()

    fun willShowVariant(): Boolean {
        return labelGroupVariantList.isNotEmpty()
    }

    fun willShowFulfillment(): Boolean{
        val labelFulfillment = getLabelFulfillment()

        return labelFulfillment != null
                && labelFulfillment.title.isNotEmpty()
                && labelFulfillment.imageUrl.isNotEmpty()
    }

    fun isShowLabelCategory() = getLabelCategory()?.title?.isNotEmpty() == true

    fun isShowLabelCostPerUnit() = getLabelCostPerUnit()?.title?.isNotEmpty() == true

    fun isShowCategoryAndCostPerUnit() = isShowLabelCategory() && isShowLabelCostPerUnit()

    fun willShowPrimaryButtonWishlist() = hasAddToCartWishlist || hasSimilarProductWishlist

    fun getRenderedLabelGroupVariantList(
        sizeCharLimit: Int,
        showBoth: Boolean = false,
    ): List<LabelGroupVariant> {
        val (colorVariant, sizeVariant, customVariant) = getSplittedLabelGroupVariant(sizeCharLimit)

        if (isLabelVariantCountBelowMinimum(colorVariant, sizeVariant))
            return listOf()

        val colorVariantTaken = getLabelVariantColorCount(colorVariant)
        val sizeVariantTaken = if (showBoth) MAX_LABEL_VARIANT_COUNT
        else getLabelVariantSizeCount(colorVariantTaken)

        return colorVariant.take(colorVariantTaken) +
                sizeVariant.take(sizeVariantTaken) +
                customVariant
    }

    private fun isLabelVariantCountBelowMinimum(
            colorVariant: List<LabelGroupVariant>,
            sizeVariant: List<LabelGroupVariant>
    ) = colorVariant.size < MIN_LABEL_VARIANT_COUNT
            && sizeVariant.size < MIN_LABEL_VARIANT_COUNT

    private fun getLabelVariantColorCount(colorVariant: List<LabelGroupVariant>) =
            if (colorVariant.size >= MIN_LABEL_VARIANT_COUNT)
                MAX_LABEL_VARIANT_COUNT
            else 0

    private fun getLabelVariantSizeCount(colorVariantTaken: Int): Int {
        val hasLabelVariantColor = colorVariantTaken > 0

        return if (hasLabelVariantColor) 0 else MAX_LABEL_VARIANT_COUNT
    }

    private fun getSplittedLabelGroupVariant(sizeCharLimit: Int):
        Triple<List<LabelGroupVariant>, List<LabelGroupVariant>, List<LabelGroupVariant>> {
        var sizeVariantCount = 0
        var hiddenSizeVariant = 0

        val colorVariant = mutableListOf<LabelGroupVariant>()
        val sizeVariant = mutableListOf<LabelGroupVariant>()
        val customVariant = mutableListOf<LabelGroupVariant>()

        labelGroupVariantList.forEach { element ->
            when {
                element.isColor() -> {
                    colorVariant.add(element)
                }
                element.isSize() -> {
                    val additionalSize = element.title.length + EXTRA_CHAR_SPACE
                    val isWithinCharLimit =
                            (sizeVariantCount + additionalSize) <= sizeCharLimit

                    if (isWithinCharLimit) {
                        sizeVariant.add(element)
                        sizeVariantCount += additionalSize
                    }
                    else {
                        hiddenSizeVariant++
                    }
                }
                else -> {
                    customVariant.add(element)
                }
            }
        }

        processHiddenSizeVariant(hiddenSizeVariant, customVariant)

        return Triple(colorVariant, sizeVariant, customVariant)
    }

    private fun processHiddenSizeVariant(hiddenSizeVariant: Int, customVariant: MutableList<LabelGroupVariant>) {
        if (hiddenSizeVariant <= 0) return

        val labelGroupCustomVariant = customVariant.getOrNull(0)
                ?: LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "0")

        val title = (labelGroupCustomVariant.title.toIntOrZero() + hiddenSizeVariant).toString()

        labelGroupCustomVariant.title = title

        customVariant.clear()
        customVariant.add(labelGroupCustomVariant)
    }

    fun getOverlayImageLabel(): LabelGroup? {
        return when {
            getLabelProductStatus() != null -> null
            getLabelBestSeller() != null -> getLabelBestSeller()
            else -> getLabelGimmick()
        }
    }

    enum class ProductListType {
        CONTROL, REPOSITION, LONG_IMAGE, NO_CAMPAIGN
    }
}