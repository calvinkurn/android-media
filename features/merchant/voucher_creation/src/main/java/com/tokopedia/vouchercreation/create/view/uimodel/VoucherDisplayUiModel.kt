package com.tokopedia.vouchercreation.create.view.uimodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class VoucherDisplayUiModel(
        @StringRes val displayTextRes: Int,
        @DrawableRes val displayImageRes: Int
)