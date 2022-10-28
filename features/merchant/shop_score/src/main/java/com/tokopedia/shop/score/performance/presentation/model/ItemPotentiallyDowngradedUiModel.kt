package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

/**
 * Created by @ilhamsuaib on 06/06/22.
 */

data class ItemPotentiallyDowngradedUiModel(
    val imgUrl: String,
    @StringRes
    val titleRes: Int,
    @StringRes
    val desRes: Int
)