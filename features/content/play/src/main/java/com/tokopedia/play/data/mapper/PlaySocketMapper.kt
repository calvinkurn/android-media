package com.tokopedia.play.data.mapper

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.data.*
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.data.multiplelikes.UpdateMultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.websocket.WebSocketResponse
import java.lang.reflect.Type

/**
 * Created by mzennis on 2019-12-10.
 */

class PlaySocketMapper(
        private val webSocketResponse: WebSocketResponse,
        private val gson: Gson,
) {

    private companion object {
        const val TAG = "PlaySocketMapper"
        val voucherListType: Type = object: TypeToken<List<Voucher>>(){}.type
    }

    fun mapping(): Any? {
        if (webSocketResponse.type.isEmpty() || webSocketResponse.jsonElement == null) return null

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
            PlaySocketType.ChannelInteractiveStatus.value -> {
                return mapToChannelInteractiveStatus()
            }
            PlaySocketType.ChannelInteractive.value -> {
                return mapToChannelInteractive()
            }
            PlaySocketType.RealTimeNotification.value -> {
                return mapToRealTimeNotification()
            }
            PlaySocketType.UpdateConfigMultipleLike.value -> {
                return mapToUpdateMultipleLikeConfig()
            }
            PlaySocketType.UserWinnerStatus.value -> {
                return mapToUserWinnerStatus()
            }
            PlaySocketType.ChannelInteractiveQuiz.value -> {
                return mapToChannelQuiz()
            }
            PlaySocketType.WidgetDominantCategory.value -> {
                return mapDominantCategory()
            }
        }
        return null
    }

    private fun mapToTotalClick(): TotalLike? {
        val totalLike = convertToModel(webSocketResponse.jsonObject, TotalLike::class.java)
        if (totalLike?.totalLikeFormatted.isNullOrEmpty() ||
                totalLike?.totalLikeFormatted.equals("Like", false))
            totalLike?.totalLikeFormatted = totalLike?.totalLike.toString()
        return totalLike
    }

    private fun mapToTotalView(): TotalView? {
        return convertToModel(webSocketResponse.jsonObject, TotalView::class.java)
    }

    private fun mapToIncomingChat(): PlayChat? {
        return convertToModel(webSocketResponse.jsonObject, PlayChat::class.java)
    }

    private fun mapToPinnedMessage(): PinnedMessage? {
        return convertToModel(webSocketResponse.jsonObject, PinnedMessage::class.java)
    }

    private fun mapToQuickReply(): QuickReply? {
        return convertToModel(webSocketResponse.jsonObject, QuickReply::class.java)
    }

    private fun mapToBannedFreeze(): BannedFreeze? {
        return convertToModel(webSocketResponse.jsonObject, BannedFreeze::class.java)
    }

    private fun mapToProductTag(): ProductSection? {
        return convertToModel(webSocketResponse.jsonObject, ProductSection::class.java)
    }

    private fun mapToMerchantVoucher(): List<Voucher> {
        return convertToModel(webSocketResponse.jsonArray, voucherListType)?: listOf()
    }

    private fun mapToChannelInteractiveStatus(): ChannelInteractiveStatus? {
        return convertToModel(webSocketResponse.jsonObject, ChannelInteractiveStatus::class.java)
    }

    private fun mapToChannelInteractive(): GiveawayResponse? {
        return convertToModel(webSocketResponse.jsonObject, GiveawayResponse::class.java)
    }

    private fun mapToRealTimeNotification(): RealTimeNotification? {
        return convertToModel(webSocketResponse.jsonObject, RealTimeNotification::class.java)
    }

    private fun mapToUpdateMultipleLikeConfig(): UpdateMultipleLikeConfig? {
        return convertToModel(webSocketResponse.jsonObject, UpdateMultipleLikeConfig::class.java)
    }

    private fun mapToUserWinnerStatus(): UserWinnerStatus? {
        return convertToModel(webSocketResponse.jsonObject, UserWinnerStatus::class.java)
    }

    private fun mapToChannelQuiz(): QuizResponse? = convertToModel(webSocketResponse.jsonObject, QuizResponse::class.java)

    private fun mapDominantCategory(): ChannelDetailsWithRecomResponse.ExploreWidgetConfig? = convertToModel(webSocketResponse.jsonObject, ChannelDetailsWithRecomResponse.ExploreWidgetConfig::class.java)

    private fun <T> convertToModel(jsonElement: JsonElement?, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(jsonElement, classOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log("E/${TAG}: ${e.localizedMessage}")
            }
        }
        return null
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, typeOfT: Type): T? {
        try {
            return gson.fromJson(jsonElement, typeOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log("E/${TAG}: ${e.localizedMessage}")
            }
        }
        return null
    }
}
