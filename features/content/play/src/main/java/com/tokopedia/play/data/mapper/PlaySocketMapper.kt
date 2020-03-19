package com.tokopedia.play.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.websocket.WebSocketResponse
import java.lang.reflect.Type

/**
 * Created by mzennis on 2019-12-10.
 */

class PlaySocketMapper(
        private val webSocketResponse: WebSocketResponse
) {

    private companion object {
        val voucherListType: Type = object: TypeToken<List<Voucher>>(){}.type
    }

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
            PlaySocketType.ProductTag.value -> {
                return mapToProductTag()
            }
            PlaySocketType.MerchantVoucher.value -> {
                return MerchantVoucher(mapToMerchantVoucher())
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

    private fun mapToProductTag(): ProductTag {
        return gson.fromJson(webSocketResponse.jsonObject, ProductTag::class.java)
    }

    private fun mapToMerchantVoucher(): List<Voucher> {
        return gson.fromJson(webSocketResponse.jsonArray, voucherListType)
    }
}