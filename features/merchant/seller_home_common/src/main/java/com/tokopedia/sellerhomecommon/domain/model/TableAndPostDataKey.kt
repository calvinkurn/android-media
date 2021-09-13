package com.tokopedia.sellerhomecommon.domain.model

/**
 * Created By @ilhamsuaib on 13/08/21
 */

data class TableAndPostDataKey(
    val dataKey: String,
    val filter: String,
    val maxData: Int,
    val maxDisplayPerPage: Int
)