package com.tokopedia.play.data.mapper

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.websocket.WebSocketResponse

/**
 * Created by mzennis on 2019-12-10.
 */

class PlaySocketMapper(
        private val webSocketResponse: WebSocketResponse,
        private val amountStringStepArray: Array<String>
) {

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
        val totalLike = gson.fromJson(webSocketResponse.jsonObject, TotalLike::class.java)
        if (totalLike.totalLikeFormatted.isNullOrEmpty() ||
                totalLike.totalLikeFormatted.equals("Like", false))
            totalLike.totalLikeFormatted = totalLike.totalLike.toString()
        return totalLike
    }

    private fun mapToTotalView(): TotalView {
        val totalView = gson.fromJson(webSocketResponse.jsonObject, TotalView::class.java)
        if (totalView.totalViewFormatted.isNullOrEmpty())
            totalView.totalViewFormatted = totalView.totalView.toAmountString(amountStringStepArray, separator = ".")
        return totalView
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