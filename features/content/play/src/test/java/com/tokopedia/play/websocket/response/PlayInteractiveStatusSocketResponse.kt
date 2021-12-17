package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on October 07, 2021
 */
object PlayInteractiveStatusSocketResponse {
    const val type = "CHANNEL_INTERACTIVE_STATUS"

    fun generateResponse(id: Int = 1, exist: Boolean = true) = """
        {
          "type" : "$type",
          "data" : {
            "channel_id" : $id,
            "exist" : $exist
          }
        }
    """.trimIndent()
}