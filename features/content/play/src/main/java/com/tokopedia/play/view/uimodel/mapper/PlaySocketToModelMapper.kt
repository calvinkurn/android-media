package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.ui.PlayChatUiModel
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
}