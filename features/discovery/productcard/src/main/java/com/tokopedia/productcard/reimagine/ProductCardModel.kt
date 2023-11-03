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
    val stockInfo: StockInfo = StockInfo()
) {

    fun labelBenefit(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_BENEFIT)?.takeIf { it.hasTitle() }

    fun labelCredibility(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_CREDIBILITY)?.takeIf { it.hasTitle() }

    fun labelPreventiveThematic(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_PREVENTIVE_THEMATIC)?.takeIf { it.hasTitle() }

    fun labelAssignedValue(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_ASSIGNED_VALUE)?.takeIf { it.hasTitle() }

    fun ribbon(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_RIBBON)?.takeIf { it.hasTitle() }

    fun hasRibbon() = ribbon() != null

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    fun stockInfo() : StockInfo? = stockInfo.takeIf { it.hasTitle() }

    data class FreeShipping(
        val imageUrl: String = "",
    )

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = "",
    ) {
        fun hasTitle() = title.isNotBlank()
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
