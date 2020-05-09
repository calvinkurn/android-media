package com.tokopedia.vouchercreation.detail.model

/**
 * Created By @ilhamsuaib on 05/05/20
 */

data class SubInfoItemUiModel(
        val infoKey: String,
        val infoValue: String,
        val canCopy: Boolean = false
)