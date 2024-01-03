package com.tokopedia.productcard.reimagine

data class ProductCardModel(
    val imageUrl: String = "",
    val isAds: Boolean = false,
    val name: String = "",
    val price: String = "",
    val slashedPrice: String = "",
    val discountPercentage: Int = 0,
    val labelGroupList: List<LabelGroup> = listOf(),
    val rating: String = "",
    val shopBadge: ShopBadge = ShopBadge(),
    val freeShipping: FreeShipping = FreeShipping(),
    val hasMultilineName: Boolean = false,
    val hasAddToCart: Boolean = false,
    val videoUrl: String = "",
    val hasThreeDots: Boolean = false,
    val stockInfo: StockInfo = StockInfo(),
    val isSafeProduct: Boolean = false,
) {

    fun labelBenefit(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_BENEFIT)?.takeIf(LabelGroup::hasTitle)

    fun labelCredibility(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_CREDIBILITY)?.takeIf(LabelGroup::hasTitle)

    fun labelAssignedValue(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_ASSIGNED_VALUE)?.takeIf(LabelGroup::hasImage)

    fun labelProductOffer(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_PRODUCT_OFFER)?.takeIf(LabelGroup::hasTitle)

    fun labelNettPrice(): LabelGroup? =
        labelGroup(LABEL_NETT_PRICE)?.takeIf(LabelGroup::hasTitle)

    fun ribbon(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_RIBBON)?.takeIf(LabelGroup::hasTitle)

    fun hasRibbon() = ribbon() != null

    fun showVideoIdentifier() = videoUrl.isNotBlank() && !isSafeProduct

    fun labelGroupOverlayList() = labelGroupList
        .filter { it.position.startsWith(LABEL_OVERLAY_) }
        .sortedBy { it.position }

    fun stockInfo() : StockInfo? = stockInfo.takeIf { it.hasTitle() }

    fun showPrice() = price.isNotBlank() && labelNettPrice() == null

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    data class FreeShipping(val imageUrl: String = "")

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

        data class Style(val key: String = "", val value: String = "")
    }

    data class ShopBadge(
        val imageUrl: String = "",
        val title: String = "",
    ) {

        fun hasImage() = imageUrl.isNotEmpty()

        fun hasTitle() = title.isNotEmpty()
    }

    data class StockInfo(
        val percentage: Int = 0,
        val label: String = "",
        val labelColor: String = ""
    ) {
        fun hasTitle() = label.isNotEmpty()
    }
}
