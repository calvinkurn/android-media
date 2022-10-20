package com.tokopedia.tokopedianow.common.model

data class TokoNowProductCardViewUiModel(
    val imageUrl: String = "",
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val price: String = "",
    val discount: String = "",
    val slashPrice: String = "",
    val name: String = "",
    val rating: String = "",
    val progressBarLabel: String = "",
    val progressBarLabelColor: String = "",
    val progressBarPercentage: Int = 0,
    val labelGroupList: List<LabelGroup> = listOf(),
)

data class LabelGroup(
    val position: String = "",
    val title: String = "",
    val type: String = "",
    val imageUrl: String = ""
) {
    fun isPromo() = position == LABEL_PRICE
    fun isAssignedValue() = position == LABEL_BEST_SELLER
}

internal const val LABEL_PRODUCT_STATUS = "status"
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
