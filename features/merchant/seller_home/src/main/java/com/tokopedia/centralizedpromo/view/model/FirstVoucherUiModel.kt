package com.tokopedia.centralizedpromo.view.model

import androidx.annotation.DrawableRes

data class FirstVoucherUiModel (
        @DrawableRes
        val iconDrawableRes: Int,
        val title: String,
        val description: String
)