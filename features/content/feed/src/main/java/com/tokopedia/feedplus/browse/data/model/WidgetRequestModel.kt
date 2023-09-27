package com.tokopedia.feedplus.browse.data.model

/**
 * Created by meyta.taliti on 08/09/23.
 */
data class WidgetRequestModel(
    val group: String,
    val sourceType: String = "",
    val sourceId: String = ""
) {
    companion object {
        val Empty = WidgetRequestModel(
            "",
            "",
            ""
        )
    }
}
