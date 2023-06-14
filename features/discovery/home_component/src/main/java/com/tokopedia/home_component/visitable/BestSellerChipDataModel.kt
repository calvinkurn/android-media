package com.tokopedia.home_component.visitable

import com.tokopedia.kotlin.model.ImpressHolder

data class BestSellerChipDataModel(
    val title: String = "",
    val value: String = "",
    val isActivated: Boolean = false,
    val ncpRank: Int = 0,
    val position: Int = 0,
    val impressHolder: ImpressHolder = ImpressHolder(),
) {

    fun activate(): BestSellerChipDataModel = copy(isActivated = true)

    fun deactivate(): BestSellerChipDataModel = copy(isActivated = false)
}
