package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips

import androidx.annotation.StringRes
import com.tokopedia.kotlin.model.ImpressHolder

data class VoucherDisplayUiModel(
        @StringRes val displayTextRes: Int,
        val imageUrl: String,
        val impressHolder: ImpressHolder = ImpressHolder()
)