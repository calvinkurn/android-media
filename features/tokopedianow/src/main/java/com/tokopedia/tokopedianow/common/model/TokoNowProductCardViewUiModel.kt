package com.tokopedia.tokopedianow.common.model

import com.tokopedia.kotlin.extensions.view.isZero

data class TokoNowProductCardViewUiModel(
    val imageUrl: String = "",
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val stock: Int = 0,
    val price: String = "",
    val discount: String = "",
    val slashPrice: String = "",
    val name: String = "",
    val rating: String = "",
    val progressBarLabel: String = "",
    val progressBarLabelColor: String = "",
    val progressBarPercentage: Int = 0,
    val status: String = "",
    val labelGroupList: List<LabelGroup> = listOf(),
) {
    private companion object {
        private const val NORMAL_BRIGHTNESS = 1f
        private const val OOS_BRIGHTNESS = 0.5f
    }

    private fun getNewProductLabel(): LabelGroup? = labelGroupList.firstOrNull { it.isNewProductLabelPosition() && it.isTextDarkOrangeColor() }
    private fun getBestSellerLabel(): LabelGroup? = labelGroupList.firstOrNull { it.isBestSellerPosition() }
    private fun isBestSellerLabelAvailable() = getBestSellerLabel() != null

    fun getOosLabel(): LabelGroup? = labelGroupList.firstOrNull { stock.isZero() && it.isStatusPosition() && it.isTransparentBlackColor() }
    fun getAssignedValueLabelGroup(): LabelGroup? = if (isBestSellerLabelAvailable()) getBestSellerLabel() else getNewProductLabel()
    fun getImageBrightness() = if (isOos()) OOS_BRIGHTNESS else NORMAL_BRIGHTNESS

    fun isOos() = getOosLabel() != null
    fun isFlashSale() = progressBarLabel.isNotBlank() && !isOos()
    fun isNormal() = !isOos() && !isFlashSale()
}

data class LabelGroup(
    val position: String = "",
    val title: String = "",
    val type: String = "",
    val imageUrl: String = ""
) {
    fun isStatusPosition() = position == LABEL_STATUS
    fun isBestSellerPosition() = position == LABEL_BEST_SELLER
    fun isNewProductLabelPosition() = position == LABEL_GIMMICK
    fun isTransparentBlackColor() = type == TRANSPARENT_BLACK
    fun isTextDarkOrangeColor() = type == TEXT_DARK_ORANGE
}

/**
 * Position
 */
internal const val LABEL_STATUS = "status"
internal const val LABEL_PRICE = "price"
internal const val LABEL_GIMMICK = "gimmick"
internal const val LABEL_INTEGRITY = "integrity"
internal const val LABEL_SHIPPING = "shipping"
internal const val LABEL_CAMPAIGN = "campaign"
internal const val LABEL_BEST_SELLER = "best_seller"
internal const val LABEL_ETA = "eta"
internal const val LABEL_FULFILLMENT = "fulfillment"
internal const val LABEL_CATEGORY = "category"
internal const val LABEL_COST_PER_UNIT = "costperunit"
internal const val LABEL_CATEGORY_SIDE = "category_side"
internal const val LABEL_CATEGORY_BOTTOM = "category_bottom"
internal const val LIGHT_GREY = "lightGrey"
internal const val LIGHT_BLUE = "lightBlue"
internal const val LIGHT_GREEN = "lightGreen"
internal const val LIGHT_RED = "lightRed"
internal const val LIGHT_ORANGE = "lightOrange"
internal const val DARK_GREY = "darkGrey"
internal const val DARK_BLUE = "darkBlue"
internal const val DARK_GREEN = "darkGreen"
internal const val DARK_RED = "darkRed"
internal const val DARK_ORANGE = "darkOrange"

/**
 * Background Color
 */
internal const val TRANSPARENT_BLACK = "transparentBlack"

/**
 * Text Color
 */
internal const val TEXT_DARK_ORANGE = "textDarkOrange"
