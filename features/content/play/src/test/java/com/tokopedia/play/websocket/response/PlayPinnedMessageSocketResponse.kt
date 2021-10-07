package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
object PlayPinnedMessageSocketResponse {
    const val type = "PINNED_MESSAGE"
    const val channelId = 1
    const val pinnedMessageId = 1
    const val title = "Pinned Message Title"
    const val message = "Pinned Message"
    const val redirectUrl = "tokopedia://shop/123123"

    val response = """
        {
            "type": "$type",
            "data": {
            	"channel_id" : $channelId,
            	"pinned_message_id" : $pinnedMessageId,
            	"title" : "$title",
            	"message" : "$message",
            	"redirect_url" : "$redirectUrl"
            }
        }
    """.trimIndent()
}