package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 31/01/22.
 */

data class LastUpdatedUiModel(
    var lastUpdatedInMillis: Long = 0L,
    var isTheLatest: Boolean = false,
    var shouldShow: Boolean = false
)