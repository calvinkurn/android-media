package com.tokopedia.home_component.visitable

data class BestSellerChipProductDataModel(
    val chip: BestSellerChipDataModel = BestSellerChipDataModel(),
    val productModelList: List<BestSellerProductDataModel> = listOf()
) {

    val title = chip.title
    val seeMoreApplink = chip.seeMoreApplink
    val isActivated = chip.isActivated
    val value = chip.value
}
