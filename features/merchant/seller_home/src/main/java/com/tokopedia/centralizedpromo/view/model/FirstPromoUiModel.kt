package com.tokopedia.centralizedpromo.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FirstPromoUiModel (
        @DrawableRes
        val iconDrawableRes: Int? = null,
        val iconUnifyAndColorResPair: Pair<Int, Int>? = null,
        @StringRes
        val titleRes: Int? = null,
        @StringRes
        val descriptionRes: Int
)