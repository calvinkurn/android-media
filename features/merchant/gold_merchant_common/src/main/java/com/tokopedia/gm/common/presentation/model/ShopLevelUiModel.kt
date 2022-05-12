package com.tokopedia.gm.common.presentation.model

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

data class ShopLevelUiModel(
    val itemSold: Int = 0,
    val nextUpdate: String = "",
    val netItemValue: Int = 0,
    val period: String = "",
    val shopLevel: Int = 0
)