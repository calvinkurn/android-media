package com.tokopedia.centralizedpromo.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FirstVoucherUiModel (
        @DrawableRes
        val iconDrawableRes: Int,
        @StringRes
        val titleRes: Int,
        @StringRes
        val descriptionRes: Int
)