package com.tokopedia.play.broadcaster.data.socket

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.tokopedia.play.broadcaster.data.type.PlaySocketEnum
import com.tokopedia.play.broadcaster.domain.model.Banned
import com.tokopedia.play.broadcaster.domain.model.Chat
import com.tokopedia.play.broadcaster.domain.model.Freeze
import com.tokopedia.play.broadcaster.domain.model.LiveDuration
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.domain.model.NewMetricList
import com.tokopedia.play.broadcaster.domain.model.ProductTagging
import com.tokopedia.play.broadcaster.domain.model.TotalLike
import com.tokopedia.play.broadcaster.domain.model.TotalView
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.websocket.WebSocketResponse
import java.lang.reflect.Type


/**
 * Created by mzennis on 19/08/21.
 */
class PlayBroadcastWebSocketMapper(
    private val response: WebSocketResponse,
    private val gson: Gson,
) {

    private val newMetricListType: Type = object: TypeToken<List<NewMetricList.NewMetric>>(){}.type

    fun map(): Any? {
        if (response.type.isEmpty() || response.jsonElement == null)
            return null

        return when(response.type) {
            PlaySocketEnum.TotalView.value -> convertToModel(response.jsonObject, TotalView::class.java)
            PlaySocketEnum.TotalLike.value -> convertToModel(response.jsonObject, TotalLike::class.java)
            PlaySocketEnum.LiveDuration.value -> convertToModel(response.jsonObject, LiveDuration::class.java)
            PlaySocketEnum.LiveStats.value -> convertToModel(response.jsonObject, LiveStats::class.java)
            PlaySocketEnum.NewMetric.value -> NewMetricList(convertToModel(response.jsonArray, newMetricListType) ?: emptyList())
            PlaySocketEnum.ProductTag.value -> convertToModel(response.jsonObject, ProductTagging::class.java)
            PlaySocketEnum.Chat.value -> convertToModel(response.jsonObject, Chat::class.java)
            PlaySocketEnum.Freeze.value -> convertToModel(response.jsonObject, Freeze::class.java)
            PlaySocketEnum.Banned.value -> convertToModel(response.jsonObject, Banned::class.java)
            PlaySocketEnum.ChannelInteractive.value -> convertToModel(response.jsonObject, ChannelInteractive::class.java)
            PlaySocketEnum.PinnedMessage.value -> convertToModel(response.jsonObject, PinnedMessageSocketResponse::class.java)
            else -> null
        }
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, classOfT: Class<T>): T? {
        return try {
            return gson.fromJson(jsonElement, classOfT)
        } catch (e: Exception) {
            null
        }
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, typeOfT: Type): T? {
        return try {
            gson.fromJson(jsonElement, typeOfT)
        } catch (e: Exception) {
            null
        }
    }
}