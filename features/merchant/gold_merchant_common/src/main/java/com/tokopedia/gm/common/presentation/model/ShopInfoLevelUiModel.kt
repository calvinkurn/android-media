package com.tokopedia.gm.common.presentation.model

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

data class ShopInfoLevelUiModel(
    var shopIncome: String = "",
    var productSold: String = "",
    var periodDate: String = "",
    var nextUpdate: String = "",
    var cardTooltipLevelList: List<CardTooltipLevelUiModel> = listOf()
)