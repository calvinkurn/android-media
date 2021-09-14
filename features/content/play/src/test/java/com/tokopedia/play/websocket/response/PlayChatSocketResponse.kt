package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayChatSocketResponse {
    const val type = "MESG"
    const val channelId = 1
    const val userId = 1
    const val userUserId = 1
    const val userName = "Jonathan"
    const val userImage = "https://tokopedia.com/test.png"
    const val timestamp = 1579064126000


    fun generateResponse(message: String = "Hello World!"): String =
        """
            {
                "type": "$type",
                "data": {
                    "channel_id": $channelId,
                    "user": {
                        "id": $userId,
                        "user_id": $userUserId,
                        "name": "$userName",
                        "image": "$userImage"
                    },
                    "message": "$message",
                    "timestamp": $timestamp
                }
            }
        """.trimIndent()
}