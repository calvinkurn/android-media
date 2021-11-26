package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayBannedSocketResponse {
    const val type = "BANNED"

    fun generateResponse(channelId: String = "1", userId: String = "1", isBanned: Boolean = true): String =
        """
            {
                "type": "$type",
                "data": {
                  "channel_id": $channelId,
                  "is_banned": $isBanned,
                  "user_id": $userId
                }
            }
        """.trimIndent()
}