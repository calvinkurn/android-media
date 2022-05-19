package com.tokopedia.gm.common.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

data class ShopLevelUiModel(
    val itemSold: Int = Int.ZERO,
    val nextUpdate: String = String.EMPTY,
    val netItemValue: Int = Int.ZERO,
    val period: String = String.EMPTY,
    val shopLevel: Int = Int.ZERO
)