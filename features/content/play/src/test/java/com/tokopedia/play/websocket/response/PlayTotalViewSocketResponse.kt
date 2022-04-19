package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayTotalViewSocketResponse {

    const val type = "TOTAL_VIEW"
    const val totalView = 12000
    const val totalViewFormatted = "12k"

    val response = """
        {
            "type": "$type",
            "data": {
                "total_view": $totalView,
                "total_view_formatted": "$totalViewFormatted"
            }
        }
    """.trimIndent()
}