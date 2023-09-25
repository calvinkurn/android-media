package com.tokopedia.productcard

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.layout.LayoutStrategyFactory
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
import com.tokopedia.productcard.utils.LABEL_OVERLAY
import com.tokopedia.productcard.utils.LABEL_PRICE
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.LABEL_RIBBON
import com.tokopedia.productcard.utils.LABEL_SHIPPING
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_QUANTITY_NON_VARIANT
import com.tokopedia.productcard.utils.SlashPriceCashbackExperiment
import com.tokopedia.productcard.utils.TYPE_VARIANT_COLOR
import com.tokopedia.productcard.utils.TYPE_VARIANT_CUSTOM
import com.tokopedia.productcard.utils.TYPE_VARIANT_SIZE
import com.tokopedia.productcard.utils.rollenceRemoteConfig
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.CardUnify2
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
        @Deprecated("replaced with animateOnPress")
        val cardInteraction: Boolean? = null,
        val productListType: ProductListType = ProductListType.CONTROL,
        val isPortrait: Boolean = false,
        val seeOtherProductText: String = "",
        val isTopStockBar: Boolean = false,
        val cardType: Int = CardUnify2.TYPE_SHADOW,
        val animateOnPress: Int = CardUnify2.ANIMATE_OVERLAY,
        val pageSource: PageSource = PageSource.OTHER,
        val abTestRemoteConfig: Lazy<RemoteConfig?> = rollenceRemoteConfig(),
) {
    @Deprecated("replace with labelGroupList")
    var isProductSoldOut: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductPreOrder: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductWholesale: Boolean = false

    internal val layoutStrategy =
        LayoutStrategyFactory.create(productListType, isTopStockBar, cardType)

    private val slashedPriceCashbackExperiment by lazyThreadSafetyNone {
        SlashPriceCashbackExperiment.get(abTestRemoteConfig)
    }

    fun isOnSlashPriceCashbackExperiment() =
        slashedPriceCashbackExperiment.isUnderExperiment(pageSource)

    val showRibbon: Boolean
        get() = getLabelRibbon()?.title?.isNotEmpty() == true

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

    fun getLabelOverlay(): LabelGroup? {
        return findLabelGroup(LABEL_OVERLAY)
    }

    fun getLabelRibbon(): LabelGroup? {
        return findLabelGroup(LABEL_RIBBON)
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

    fun showDiscountAsText(): Boolean = isOnSlashPriceCashbackExperiment()

    fun isShowLabelPrice(): Boolean =
        !isShowDiscountOrSlashPrice() || isOnSlashPriceCashbackExperiment()

    fun isShowFreeOngkirBadge() = freeOngkir.isActive && freeOngkir.imageUrl.isNotEmpty()

    fun isShowShopBadge() = shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() } != null && shopLocation.isNotEmpty()

    fun isShowShopLocation() = shopLocation.isNotEmpty() && !willShowFulfillment()

    fun isShowShopRating() = shopRating.isNotEmpty()

    fun isShowLabelBestSeller() = getLabelBestSeller()?.title?.isNotEmpty() == true

    fun isShowLabelCategorySide() =
        isShowLabelBestSeller() && getLabelCategorySide()?.title?.isNotEmpty() == true

    fun isShowLabelCategoryBottom() =
        isShowLabelBestSeller() && getLabelCategoryBottom()?.title?.isNotEmpty() == true

    fun isStockBarShown() = stockBarLabel.isNotEmpty()

    fun isShowLabelCampaign(): Boolean {
        val labelCampaign = getLabelCampaign()

        return !isShowLabelBestSeller()
                && labelCampaign != null
                && labelCampaign.title.isNotEmpty()
                && labelCampaign.imageUrl.isNotEmpty()
    }

    fun isShowLabelOverlay(): Boolean {
        val labelOverlay = getLabelOverlay()

        return labelOverlay != null
                && labelOverlay.title.isNotEmpty()
                && (labelOverlay.imageUrl.isNotEmpty() || labelOverlay.type.isNotEmpty())
    }

    fun isShowLabelGimmick() =
            !isShowLabelBestSeller()
                    && !isShowLabelCampaign()

    fun willShowVariant(): Boolean {
        return labelGroupVariantList.isNotEmpty()
    }

    fun hasLabelVariantColor(): Boolean {
        return labelGroupVariantList.any { it.isColor() }
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

    fun willShowBigImage() : Boolean {
        return productListType == ProductListType.LONG_IMAGE
            || (productListType == ProductListType.PORTRAIT && isPortrait)
    }

    fun willShowButtonSeeOtherProduct() = seeOtherProductText.isNotEmpty()

    fun getRenderedLabelGroupVariantList(): List<LabelGroupVariant> {
        val (colorVariant, sizeVariant, customVariant) = getSplittedLabelGroupVariant()

        if (isLabelVariantCountBelowMinimum(colorVariant, sizeVariant))
            return listOf()

        val colorVariantTaken = layoutStrategy.getLabelVariantColorCount(colorVariant)
        val sizeVariantTaken =
            layoutStrategy.getLabelVariantSizeCount(this, colorVariantTaken)

        return colorVariant.take(colorVariantTaken) +
                sizeVariant.take(sizeVariantTaken) +
                customVariant
    }

    private fun isLabelVariantCountBelowMinimum(
            colorVariant: List<LabelGroupVariant>,
            sizeVariant: List<LabelGroupVariant>
    ) = colorVariant.size < MIN_LABEL_VARIANT_COUNT
            && sizeVariant.size < MIN_LABEL_VARIANT_COUNT

    private fun getSplittedLabelGroupVariant():
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
                    val additionalSize = element.title.length + layoutStrategy.extraCharSpace
                    val isWithinCharLimit =
                            (sizeVariantCount + additionalSize) <= layoutStrategy.sizeCharLimit

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

    fun getLabelReposition(): LabelGroup? {
        return when {
            getLabelProductStatus() != null -> null
            getLabelBestSeller() != null -> getLabelBestSeller()
            else -> getLabelGimmick()
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is ProductCardModel
            && this.productImageUrl == other.productImageUrl
            && this.isWishlisted == other.isWishlisted
            && this.isWishlistVisible == other.isWishlistVisible
            && this.labelPromo == other.labelPromo
            && this.shopImageUrl == other.shopImageUrl
            && this.shopName == other.shopName
            && this.productName == other.productName
            && this.discountPercentage == other.discountPercentage
            && this.slashedPrice == other.slashedPrice
            && this.priceRange == other.priceRange
            && this.formattedPrice == other.formattedPrice
            && this.shopBadgeList == other.shopBadgeList
            && this.shopLocation == other.shopLocation
            && this.ratingCount == other.ratingCount
            && this.reviewCount == other.reviewCount
            && this.labelCredibility == other.labelCredibility
            && this.labelOffers == other.labelOffers
            && this.freeOngkir == other.freeOngkir
            && this.isTopAds == other.isTopAds
            && this.ratingString == other.ratingString
            && this.hasThreeDots == other.hasThreeDots
            && this.labelGroupList == other.labelGroupList
            && this.hasDeleteProductButton == other.hasDeleteProductButton
            && this.hasAddToCartButton == other.hasAddToCartButton
            && this.hasRemoveFromWishlistButton == other.hasRemoveFromWishlistButton
            && this.pdpViewCount == other.pdpViewCount
            && this.stockBarLabel == other.stockBarLabel
            && this.stockBarLabelColor == other.stockBarLabelColor
            && this.stockBarPercentage == other.stockBarPercentage
            && this.isOutOfStock == other.isOutOfStock
            && this.addToCardText == other.addToCardText
            && this.shopRating == other.shopRating
            && this.isShopRatingYellow == other.isShopRatingYellow
            && this.countSoldRating == other.countSoldRating
            && this.hasNotifyMeButton == other.hasNotifyMeButton
            && this.labelGroupVariantList == other.labelGroupVariantList
            && this.addToCartButtonType == other.addToCartButtonType
            && this.isWideContent == other.isWideContent
            && this.variant == other.variant
            && this.nonVariant == other.nonVariant
            && this.hasSimilarProductButton == other.hasSimilarProductButton
            && this.hasButtonThreeDotsWishlist == other.hasButtonThreeDotsWishlist
            && this.hasAddToCartWishlist == other.hasAddToCartWishlist
            && this.hasSimilarProductWishlist == other.hasSimilarProductWishlist
            && this.customVideoURL == other.customVideoURL
            && this.cardInteraction == other.cardInteraction
            && this.productListType == other.productListType
            && this.isPortrait == other.isPortrait
            && this.seeOtherProductText == other.seeOtherProductText
            && this.isTopStockBar == other.isTopStockBar
            && this.cardType == other.cardType
            && this.animateOnPress == other.animateOnPress
            && this.pageSource == other.pageSource
    }

    enum class ProductListType {
        CONTROL,
        REPOSITION,
        LONG_IMAGE,
        GIMMICK,
        PORTRAIT,
        ETA,
        BEST_SELLER,
        FIXED_GRID,
        LIST_VIEW,
    }

    enum class PageSource {
        SEARCH,
        OTHER,
    }
}
