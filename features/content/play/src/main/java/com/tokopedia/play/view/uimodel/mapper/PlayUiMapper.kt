package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.*


/**
 * Created by mzennis on 2020-03-06.
 */
object PlayUiMapper {

    fun mapChannelInfo(channel: Channel) = ChannelInfoUiModel(
            id = channel.channelId,
            title = channel.title,
            description = channel.description,
            channelType = if (channel.videoStream.isLive) PlayChannelType.Live else PlayChannelType.VOD,
            moderatorName = channel.moderatorName,
            partnerId = channel.partnerId,
            partnerType = PartnerType.getTypeByValue(channel.partnerType),
            contentId = channel.contentId,
            contentType = channel.contentType,
            likeType = channel.likeType,
            isShowCart = channel.isShowCart
    )

    fun mapPinnedMessage(partnerName: String, pinnedMessage: PinnedMessage) = if (pinnedMessage.pinnedMessageId > 0 && pinnedMessage.title.isNotEmpty()) {
        PinnedMessageUiModel(
                applink = pinnedMessage.redirectUrl,
                partnerName = partnerName,
                title = pinnedMessage.title
        )
    } else null

    fun mapPinnedProduct(partnerName: String, productCount: Int) = if (productCount > 0) PinnedProductUiModel(
            partnerName = partnerName,
            title = "",
            isPromo = true
    ) else null

    fun mapVideoStream(videoStream: VideoStream, isActive: Boolean) = VideoStreamUiModel(
            uriString = videoStream.config.streamUrl,
            channelType = if (videoStream.isLive
                    && videoStream.type.equals(PlayChannelType.Live.value, true))
                PlayChannelType.Live else PlayChannelType.VOD,
            isActive = isActive
    )

    fun mapQuickReply(quickReplyList: List<String>) = QuickReplyUiModel(quickReplyList.filterNot { quickReply -> quickReply.isEmpty() || quickReply.isBlank() } )
    fun mapQuickReply(quickReply: QuickReply) = mapQuickReply(quickReply.data)

    fun mapTotalLikes(totalLike: Int, totalLikeString: String) = TotalLikeUiModel(totalLike, totalLikeString)
    fun mapTotalLikes(totalLike: TotalLike) = mapTotalLikes(totalLike.totalLike, totalLike.totalLikeFormatted)

    fun mapTotalViews(totalViewString: String) = TotalViewUiModel(totalViewString)
    fun mapTotalViews(totalView: TotalView) = mapTotalViews(totalView.totalViewFormatted)

    fun mapPlayChat(userId: String, playChat: PlayChat) = PlayChatUiModel(
            messageId = playChat.messageId,
            userId = playChat.user.id,
            name = playChat.user.name,
            message = playChat.message,
            isSelfMessage = playChat.user.id == userId
    )

    fun mapPartnerInfoFromShop(shopId: String, shopInfo: ShopInfo) = PartnerInfoUiModel(
            id = shopInfo.shopCore.shopId.toLong(),
            name = shopInfo.shopCore.name,
            type = PartnerType.SHOP,
            isFollowed = shopInfo.favoriteData.alreadyFavorited == 1,
            isFollowable = shopId != shopInfo.shopCore.shopId
    )
}