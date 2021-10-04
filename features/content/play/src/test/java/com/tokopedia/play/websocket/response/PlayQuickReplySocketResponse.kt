package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayQuickReplySocketResponse {
    const val type = "QUICK_REPLY"
    const val channelId = 1
    val quickReplyList = listOf("Hello", "World")

    val response = """
        {
            "type": "$type",
            "data": {
                "channel_id" : $channelId,
                "quick_reply" : $quickReplyList
            }
        }
    """.trimIndent()
}