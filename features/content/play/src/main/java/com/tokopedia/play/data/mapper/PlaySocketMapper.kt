package com.tokopedia.play.data.mapper

import com.google.gson.Gson
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.websocket.WebSocketResponse

/**
 * Created by mzennis on 2019-12-10.
 */

class PlaySocketMapper(private val webSocketResponse: WebSocketResponse) {

    private val gson = Gson()

    fun mapping(): Any? {
        if (webSocketResponse.type.isEmpty()) return null

        when(webSocketResponse.type) {
            PlaySocketType.TotalLike.value -> {
                return mapToTotalClick()
            }
            PlaySocketType.TotalView.value -> {
                return mapToTotalView()
            }
            PlaySocketType.ChatPeople.value -> {
                return mapToIncomingChat()
            }
            PlaySocketType.PinnedMessage.value -> {
                //TODO("check if the default behaviour is really to be called twice (one for remove, one for new)")
                return mapToPinnedMessage()
            }
            PlaySocketType.QuickReply.value -> {
                return mapToQuickReply()
            }
            PlaySocketType.EventBanned.value,
            PlaySocketType.EventFreeze.value -> {
                return mapToBannedFreeze()
            }
        }
        return null
    }

    private fun mapToTotalClick(): TotalLike {
        return gson.fromJson(webSocketResponse.jsonObject, TotalLike::class.java)
    }

    private fun mapToTotalView(): TotalView {
        return gson.fromJson(webSocketResponse.jsonObject, TotalView::class.java)
    }

    private fun mapToIncomingChat(): PlayChat {
        return gson.fromJson(webSocketResponse.jsonObject, PlayChat::class.java)
    }

    private fun mapToPinnedMessage(): PinnedMessage {
        return gson.fromJson(webSocketResponse.jsonObject, PinnedMessage::class.java)
    }

    private fun mapToQuickReply(): QuickReply {
        return gson.fromJson(webSocketResponse.jsonObject, QuickReply::class.java)
    }

    private fun mapToBannedFreeze(): BannedFreeze {
        return gson.fromJson(webSocketResponse.jsonObject, BannedFreeze::class.java)
    }
}