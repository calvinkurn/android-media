package com.tokopedia.play.model

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.tokopedia.play.data.mapper.PlaySocketType
import com.tokopedia.websocket.WebSocketResponse

/**
 * Created by jegul on 15/07/21
 */
class PlaySocketResponseBuilder {

    private val gson = Gson()

    private val channelInteractiveExistStatus = """
        {
          "type" : "CHANNEL_INTERACTIVE_STATUS",
          "data" : {
            "channel_id" : 67990,
            "exist" : true 
          }
        }
    """.trimIndent()

    private val channelInteractiveNotExistStatus = """
        {
          "type" : "CHANNEL_INTERACTIVE_STATUS",
          "data" : {
            "channel_id" : 67990,
            "exist" : false 
          }
        }
    """.trimIndent()

    fun buildChannelInteractiveResponse(isExist: Boolean = true): WebSocketResponse = gson.fromJson(
            if (isExist) channelInteractiveExistStatus else channelInteractiveNotExistStatus,
            WebSocketResponse::class.java
    )
}