package com.tokopedia.productcard.compact.productcard.presentation.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toLongOrZero

data class ProductCardCompactUiModel(
    val productId: String = "",
    val warehouseId: String = "",
    val imageUrl: String = "",
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val availableStock: Int = 0,
    val orderQuantity: Int = 0,
    val price: String = "",
    val discount: String = "",
    val discountInt: Int = 0,
    val slashPrice: String = "",
    val name: String = "",
    val rating: String = "",
    val sold: String = "",
    val progressBarLabel: String = "",
    val progressBarPercentage: Int = 0,
    val hasBeenWishlist: Boolean = false,
    val isSimilarProductShown: Boolean = false,
    val isWishlistShown: Boolean = false,
    val isVariant: Boolean = false,
    val needToShowQuantityEditor: Boolean = false,
    val labelGroupList: List<LabelGroup> = listOf(),
    val needToChangeMaxLinesName: Boolean = false,
    val hasBlockedAddToCart: Boolean = false,
    val isPreOrder: Boolean = false,
    /**
     * use pre draw only if need dynamic height of product card (ex: carousel)
     */
    val usePreDraw: Boolean = false
) {
    private companion object {
        private const val NORMAL_BRIGHTNESS = 1f
        private const val OOS_BRIGHTNESS = 0.5f
    }

    private fun getNewProductLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isNewProductLabelPosition() && it.isTextDarkOrangeColor() }
    private fun getBestSellerLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isBestSellerPosition() }
    private fun isBestSellerLabelAvailable(): Boolean = getBestSellerLabelGroup() != null

    fun getOosLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { (availableStock < minOrder || availableStock == Int.ZERO) && (it.isStatusPosition() && it.isTransparentBlackColor()) }
    fun getPreOrderLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { (isPreOrder && it.isStatusPosition() && it.isTransparentBlackColor()) }
    fun getAssignedValueLabelGroup(): LabelGroup? = if (isBestSellerLabelAvailable()) getBestSellerLabelGroup() else getNewProductLabelGroup()
    fun getPriceLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isPricePosition() && it.isLightGreenColor() }
    fun getWeightLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isWeightPosition() }
    fun getImageBrightness(): Float = if (isOos() || isPreOrder) OOS_BRIGHTNESS else NORMAL_BRIGHTNESS
    fun getNowUSPLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isNowUSPPosition() && it.isTextDarkGreyColor() }

    fun isOos() = getOosLabelGroup() != null
    fun isFlashSale() = progressBarLabel.isNotBlank() && !isOos()
    fun isNormal() = !isOos() && !isFlashSale() && !isPreOrder

    fun getPriceLong() = price.filter { it.isDigit() }.toLongOrZero()

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = ""
    ) {
        fun isStatusPosition() = position == LABEL_STATUS
        fun isBestSellerPosition() = position == LABEL_BEST_SELLER
        fun isNewProductLabelPosition() = position == LABEL_GIMMICK
        fun isPricePosition() = position == LABEL_PRICE
        fun isWeightPosition() = position == LABEL_WEIGHT
        fun isNowUSPPosition() = position == LABEL_NOW_USP

        fun isTransparentBlackColor() = type == TRANSPARENT_BLACK
        fun isTextDarkOrangeColor() = type == TEXT_DARK_ORANGE
        fun isLightGreenColor() = type == LIGHT_GREEN
        fun isTextDarkGreyColor() = type == TEXT_DARK_GREY
    }
}

/**
 * Position
 */
internal const val LABEL_STATUS = "status"
internal const val LABEL_PRICE = "price"
internal const val LABEL_GIMMICK = "gimmick"
internal const val LABEL_BEST_SELLER = "best_seller"
internal const val LABEL_WEIGHT = "weight"
internal const val LABEL_NOW_USP = "now_usp"

/**
 * Background Color
 */
internal const val TRANSPARENT_BLACK = "transparentBlack"

/**
 * Text Color
 */
internal const val TEXT_DARK_ORANGE = "textDarkOrange"
internal const val TEXT_DARK_GREY = "textDarkGrey"

/**
 * Label Type
 */
internal const val LIGHT_GREEN = "lightGreen"
internal const val LIGHT_RED = "lightRed"
