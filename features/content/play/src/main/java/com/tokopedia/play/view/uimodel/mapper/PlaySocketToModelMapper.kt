package com.tokopedia.play.view.uimodel.mapper

import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.*
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
@PlayScope
class PlaySocketToModelMapper @Inject constructor(
    private val productTagMapper: PlayProductTagUiMapper,
    private val merchantVoucherMapper: PlayMerchantVoucherUiMapper,
    private val chatMapper: PlayChatUiMapper,
    private val channelStatusMapper: PlayChannelStatusMapper,
    private val channelInteractiveMapper: PlayChannelInteractiveMapper,
    private val realTimeNotificationMapper: PlayRealTimeNotificationMapper,
    private val multipleLikesMapper: PlayMultipleLikesMapper,
) {

    fun mapTotalLike(input: TotalLike): Pair<Long, String> {
        return input.totalLike to input.totalLikeFormatted
    }

    fun mapTotalView(input: TotalView): String {
        return if (input.totalViewFormatted.isBlank()) "0" else input.totalViewFormatted
    }

    fun mapPinnedMessage(input: PinnedMessage): PinnedMessageUiModel {
        return PinnedMessageUiModel(
                id = input.pinnedMessageId,
                applink = input.redirectUrl,
                partnerName = "", /**Skip**/
                title = input.title,
        )
    }

    fun mapQuickReplies(input: QuickReply): PlayQuickReplyInfoUiModel {
        return PlayQuickReplyInfoUiModel(input.data)
    }

    fun mapProductTag(input: ProductTag): Pair<List<PlayProductUiModel>, Boolean> {
        return input.listOfProducts.map(productTagMapper::mapProductTag) to input.isShowProductTagging
    }

    fun mapMerchantVoucher(input: MerchantVoucher): List<MerchantVoucherUiModel> {
        return input.listOfVouchers.map(merchantVoucherMapper::mapMerchantVoucher)
    }

    fun mapChat(input: PlayChat): PlayChatUiModel {
        return chatMapper.mapChat(input)
    }

    fun mapStatus(isBanned: Boolean): PlayStatusType {
        return channelStatusMapper.mapStatusBanned(isBanned)
    }

    fun mapInteractive(input: ChannelInteractive): PlayCurrentInteractiveModel {
        return channelInteractiveMapper.mapInteractive(input)
    }

    fun mapRealTimeNotification(input: RealTimeNotification): RealTimeNotificationUiModel {
        return realTimeNotificationMapper.mapRealTimeNotification(input)
    }

    fun mapMultipleLikeConfig(
        configs: List<MultipleLikeConfig>
    ) : PlayLikeBubbleConfig {
        return multipleLikesMapper.mapMultipleLikeConfig(configs)
    }

    /**
     * Send
     */
    fun mapSendChat(message: String, channelId: String): String {
        return parseSendMessage(message, channelId)
    }

    fun mapSendLike(channelId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toLongOrZero())

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_MULTIPLE_LIKE)
        bundle.add(PARAM_SEND_DATA, param)

        return bundle.toString()
    }

    private fun parseSendMessage(message: String, channelId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toLongOrZero())
        param.addProperty(PARAM_SEND_MESSAGE, message)

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_SEND)
        bundle.add(PARAM_SEND_DATA, param)

        return bundle.toString()
    }

    companion object {

        private const val PARAM_SEND_TYPE = "type"
        private const val PARAM_SEND_DATA = "data"
        private const val PARAM_SEND_TYPE_SEND = "SEND_MESG"
        private const val PARAM_SEND_TYPE_MULTIPLE_LIKE = "MULTIPLE_LIKE"
        private const val PARAM_SEND_CHANNEL_ID = "channel_id"
        private const val PARAM_SEND_MESSAGE = "message"
    }
}