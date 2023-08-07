package com.tokopedia.home_component.visitable

data class BestSellerChipProductDataModel(
    val chip: BestSellerChipDataModel = BestSellerChipDataModel(),
    val productModelList: List<BestSellerProductDataModel> = listOf(),
    val seeMoreApplink: String = "",
) {

    val title = chip.title
    val isActivated = chip.isActivated
    val value = chip.value
    val ncpRank = chip.ncpRank
    val chipPosition = chip.position
}
