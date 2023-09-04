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
) {

    fun labelBenefit(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_BENEFIT)?.takeIf { it.hasTitle() }

    fun labelCredibility(): LabelGroup? =
        labelGroup(LABEL_REIMAGINE_CREDIBILITY)?.takeIf { it.hasTitle() }

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    data class FreeShipping(
        val imageUrl: String = "",
    )

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = "",
    ) {
        fun hasTitle() = title.isNotEmpty()
    }

    data class ShopBadge(
        val imageUrl: String = "",
        val title: String = "",
    ) {

        fun hasImage() = imageUrl.isNotEmpty()

        fun hasTitle() = title.isNotEmpty()
    }
}
