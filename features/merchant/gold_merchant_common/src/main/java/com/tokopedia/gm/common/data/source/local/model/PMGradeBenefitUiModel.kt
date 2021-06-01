package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeBenefitUiModel(
        val categoryName: String = "",
        val benefitName: String = "",
        val sequenceNum: Int = 0,
        val appLink: String? = null,
        val iconUrl: String? = null,
        val drawableResIcon: Int? = null
)