package com.tokopedia.analyticsdebugger.sse.ui.uimodel

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
data class SSELogUiModel(
    val id: Long = 0,
    val generalInfo: SSELogGeneralInfoUiModel,
    val event: String,
    val message: String,
    val dateTime: String,
)