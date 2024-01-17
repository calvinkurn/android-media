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
    val isInBackground: Boolean = false,
) {

    fun labelBenefit(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_BENEFIT)?.takeIf(LabelGroup::hasTitle)

    fun labelCredibility(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_CREDIBILITY)?.takeIf(LabelGroup::hasTitle)

    fun labelPreventiveThematic(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_PREVENTIVE_THEMATIC)?.takeIf(LabelGroup::hasTitle)

    fun labelAssignedValue(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_ASSIGNED_VALUE)?.takeIf(LabelGroup::hasTitle)

    fun ribbon(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_RIBBON)?.takeIf(LabelGroup::hasTitle)

    fun labelProductOffer(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_PRODUCT_OFFER)?.takeIf(LabelGroup::hasTitle)

    fun hasRibbon() = ribbon() != null

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    fun stockInfo() : StockInfo? = stockInfo.takeIf { it.hasTitle() }

    data class FreeShipping(val imageUrl: String = "")

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = "",
    ) {
        private val style = style()

        private fun style() =
            type.split(STYLE_SEPARATOR)
                .associate {
                    val keyValue = it.split(STYLE_VALUE_SEPARATOR)
                    keyValue.first() to keyValue.last()
                }

        fun hasTitle() = title.isNotBlank()
        fun backgroundColor() = style[LabelGroupStyle.BACKGROUND_COLOR]
        fun backgroundOpacity() = style[LabelGroupStyle.BACKGROUND_OPACITY]?.toFloatOrNull()
        fun textColor() = style[LabelGroupStyle.TEXT_COLOR]
        fun outlineColor(): String? = style[LabelGroupStyle.OUTLINE_COLOR]

        companion object {
            private const val STYLE_SEPARATOR = "&"
            private const val STYLE_VALUE_SEPARATOR = "="
        }
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
