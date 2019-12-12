package com.tokopedia.play.data.mapper

import com.google.gson.Gson
import com.tokopedia.play.data.Like
import com.tokopedia.play.data.View
import com.tokopedia.websocket.WebSocketResponse
import java.util.*


/**
 * Created by mzennis on 2019-12-10.
 */

class PlaySocketMapper(private val webSocketResponse: WebSocketResponse) {

    private val gson = Gson()

    fun mapping(): Any? {
        if (webSocketResponse.type.isEmpty()) return null

        // Todo, complete this mapper
        when(webSocketResponse.type.toLowerCase(Locale.ENGLISH)) {
            PlaySocketType.VIDEO.value -> {

            }
            PlaySocketType.VIDEO_STREAM.value -> {

            }
            PlaySocketType.TOTAL_CLICK.value -> {
                return mapToTotalClick()
            }
            PlaySocketType.TOTAL_VIEW.value -> {
                return mapToTotalView()
            }
            PlaySocketType.CHAT_ADMIN.value -> {

            }
            PlaySocketType.CHAT_GENERATED_MESSAGE.value -> {

            }
        }
        return null
    }

    private fun mapToTotalClick(): Like {
        return gson.fromJson(webSocketResponse.jsonObject, Like::class.java)
    }

    private fun mapToTotalView(): View {
        return gson.fromJson(webSocketResponse.jsonObject, View::class.java)
    }
}