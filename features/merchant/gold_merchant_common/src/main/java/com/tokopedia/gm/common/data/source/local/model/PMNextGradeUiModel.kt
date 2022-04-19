package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMNextGradeUiModel(
        val shopLevel: Int = 0,
        val shopScoreMin: Int = 0,
        val shopScoreMax: Int = 0,
        val gradeName: String = "",
        val imgBadgeUrl: String = "",
        val backgroundUrl: String? = null
)