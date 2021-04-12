package com.tokopedia.play.view.uimodel.mapper

import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.data.*
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketCache
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.websocket.WebSocketException
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlaySocketToModelMapper @Inject constructor(
        private val productTagMapper: PlayProductTagUiMapper,
        private val merchantVoucherMapper: PlayMerchantVoucherUiMapper,
        private val chatMapper: PlayChatUiMapper,
        private val channelStatusMapper: PlayChannelStatusMapper,
) {

    fun mapTotalLike(input: TotalLike): PlayLikeStatusInfoUiModel {
        return PlayLikeStatusInfoUiModel(
                totalLike = input.totalLike,
                totalLikeFormatted = input.totalLikeFormatted,
                isLiked = false, /**Skip**/
                source = LikeSource.Network
        )
    }

    fun mapTotalView(input: TotalView): String {
        return if (input.totalViewFormatted.isBlank()) "0" else input.totalViewFormatted
    }

    fun mapPinnedMessage(input: PinnedMessage): PlayPinnedUiModel.PinnedMessage {
        return PlayPinnedUiModel.PinnedMessage(
                id = input.pinnedMessageId.toString(),
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

    fun mapSendChat(message: String, channelId: String): String {
        return parseSendMessage(message, channelId)
    }

    private fun parseSendMessage(message: String, channelId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toIntOrZero())
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
        private const val PARAM_SEND_CHANNEL_ID = "channel_id"
        private const val PARAM_SEND_MESSAGE = "message"
    }
}