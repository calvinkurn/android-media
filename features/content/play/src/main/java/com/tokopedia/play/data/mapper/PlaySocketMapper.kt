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
            PlaySocketType.TOTAL_CLICK.value -> {
                return mapToTotalClick()
            }
            PlaySocketType.TOTAL_VIEW.value -> {
                return mapToTotalView()
            }
            PlaySocketType.CHAT_PEOPLE.value -> {
                return mapToIncomingChat()
            }
            PlaySocketType.PINNED_MESSAGE.value -> {
                //TODO("check if the default behaviour is really to be called twice (one for remove, one for new)")
                return mapToPinnedMessage()
            }
            PlaySocketType.QUICK_REPLY.value -> {
                return mapToQuickReply()
            }
            PlaySocketType.EVENT_BANNED.value,
            PlaySocketType.EVENT_FREEZE.value -> {
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