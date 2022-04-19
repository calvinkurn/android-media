package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 10/01/22.
 */

data class MissionProgressUiModel(
    val description: String = "",
    val percentage: Int = 0,
    val completed: String = "",
    val target: String = ""
)