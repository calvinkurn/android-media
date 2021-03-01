package com.tokopedia.productcard

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.utils.*
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.parcel.Parcelize

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
        val addToCartButtonType: Int = UnifyButton.Type.TRANSACTION
) {
    @Deprecated("replace with labelGroupList")
    var isProductSoldOut: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductPreOrder: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductWholesale: Boolean = false

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

    @Parcelize
    data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val imageUrl: String = ""
    ):Parcelable {

        fun isShowLabelCampaign(): Boolean {
            return imageUrl.isNotEmpty() && title.isNotEmpty()
        }
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

    fun getLabelETA(): LabelGroup? {
        return findLabelGroup(LABEL_ETA)
    }

    fun getLabelFulfillment(): LabelGroup? {
        return findLabelGroup(LABEL_FULFILLMENT)
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

    fun getRenderedLabelGroupVariantList(): List<LabelGroupVariant> {
        val (colorVariant, sizeVariant, customVariant) = getSplittedLabelGroupVariant()

        if (colorVariant.size < 2 && sizeVariant.size < 2) return listOf()

        val colorVariantTaken = if (colorVariant.size >= 2) 5 else 0
        val sizeVariantTaken = if (colorVariantTaken > 0) 0 else 5

        return colorVariant.take(colorVariantTaken) + sizeVariant.take(sizeVariantTaken) + customVariant
    }

    private fun getSplittedLabelGroupVariant(): Triple<List<LabelGroupVariant>, List<LabelGroupVariant>, List<LabelGroupVariant>> {
        val sizeVariantLimit = 18
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
                    val additionalSize = element.title.length + 2

                    if ((sizeVariantCount + additionalSize) <= sizeVariantLimit) {
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

    companion object {
        const val WORDING_SEGERA_HABIS = "Segera Habis"
        val FIRE_WIDTH = R.dimen.dp_12
        val FIRE_HEIGHT = R.dimen.dp_13
    }
}