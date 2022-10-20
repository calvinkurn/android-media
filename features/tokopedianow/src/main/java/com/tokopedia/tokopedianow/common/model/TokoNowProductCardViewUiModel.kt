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
    fun getOosLabel(): LabelGroup? = labelGroupList.firstOrNull { stock.isZero() && it.isStatusPosition() && it.isTransparentBlackType() }
    fun getBestSellerLabel(): LabelGroup? = labelGroupList.firstOrNull { it.isBestSeller() }
    fun isOos() = getOosLabel() != null
    fun isFlashSale() = progressBarLabel.isNotBlank() && !isOos()
}

data class LabelGroup(
    val position: String = "",
    val title: String = "",
    val type: String = "",
    val imageUrl: String = ""
) {
    fun isStatusPosition() = position == LABEL_STATUS
    fun isBestSeller() = position == LABEL_BEST_SELLER
    fun isTransparentBlackType() = type == TRANSPARENT_BLACK
}

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
internal const val TRANSPARENT_BLACK = "transparentBlack"
