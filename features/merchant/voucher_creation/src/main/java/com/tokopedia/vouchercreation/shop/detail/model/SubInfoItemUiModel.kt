package com.tokopedia.vouchercreation.shop.detail.model

import androidx.annotation.StringRes

/**
 * Created By @ilhamsuaib on 05/05/20
 */

data class SubInfoItemUiModel(
        @StringRes val infoKey: Int,
        val infoValue: String,
        val canCopy: Boolean = false,
        var isWarning: Boolean = false
)