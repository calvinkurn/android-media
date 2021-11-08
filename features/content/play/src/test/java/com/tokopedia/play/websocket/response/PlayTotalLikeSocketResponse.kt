package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayTotalLikeSocketResponse {
    const val type = "TOTAL_LIKE"
    const val totalLike = 15000
    const val totalLikeFormatted = "15k"

    val response = """
        {
            "type": "$type",
            "data": {
                "total_like": $totalLike,
                "total_like_formatted": "$totalLikeFormatted"
            }
        }
    """.trimIndent()
}