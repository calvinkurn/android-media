package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMCurrentGradeUiModel(
        val shopLevelActual: Int = 0,
        val shopScoreActual: Int = 0,
        val gradeName: String = "",
        val imgBadgeUrl: String = "",
        val backgroundUrl: String = ""
)