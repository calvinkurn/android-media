package com.tokopedia.chat_common.domain

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE

/**
 * @author by nisie on 19/12/18.
 */
object SendWebsocketParam {

    fun generateParamSendMessage(messageId : String, sendMessage: String):JsonObject{
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", sendMessage)
        data.addProperty("start_time", SendableViewModel.generateStartTime())
        json.add("data", data)
        return json
    }

}