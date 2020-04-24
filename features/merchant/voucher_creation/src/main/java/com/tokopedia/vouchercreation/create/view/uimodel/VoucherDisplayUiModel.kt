package com.tokopedia.vouchercreation.create.view.uimodel

import androidx.annotation.StringRes

data class VoucherDisplayUiModel(
        @StringRes val displayTextRes: Int,
        val imageUrl: String
)