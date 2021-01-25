package com.tokopedia.play.view.uimodel.mapper

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by mzennis on 2020-03-06.
 */
object PlayUiMapper {

    private const val MS_PER_SECOND = 1000

    fun createCompleteInfoModel(
            channel: Channel,
            isBanned: Boolean,
            exoPlayer: ExoPlayer
    ) = PlayCompleteInfoUiModel(
            channelInfo = mapChannelInfo(channel),
            videoStream = mapVideoStream(
                    channel.video,
                    channel.configuration,
                    channel.isLive),
            videoPlayer = mapVideoPlayer(
                    channel.video,
                    exoPlayer
            ),
            pinnedMessage = mapPinnedMessage(
                    channel.partner.name,
                    channel.pinnedMessage
            ),
            pinnedProduct = mapPinnedProduct(
                    channel.partner.name,
                    channel.configuration),
            quickReply = mapQuickReply(channel.quickReplies),
            totalView = mapTotalViews(channel.stats.view.formatted),
            event = mapEvent(channel, isBanned)
    )

    private fun mapEvent(channel: Channel, isBanned: Boolean) = EventUiModel(
            isBanned = isBanned,
            isFreeze = !channel.configuration.active || channel.configuration.freezed,
            bannedMessage = channel.configuration.channelBannedMessage.message,
            bannedTitle = channel.configuration.channelBannedMessage.title,
            bannedButtonTitle = channel.configuration.channelBannedMessage.buttonText,
            freezeMessage = channel.configuration.channelFreezeScreen.desc,
            freezeTitle = String.format(channel.configuration.channelFreezeScreen.title, channel.title),
            freezeButtonTitle = channel.configuration.channelFreezeScreen.btnTitle,
            freezeButtonUrl = channel.configuration.channelFreezeScreen.btnAppLink
    )

    private fun mapChannelInfo(channel: Channel) = ChannelInfoUiModel(
            id = channel.channelId,
            partnerInfo = mapPartnerInfo(channel.partner),
            feedInfo = mapFeedInfo(channel.configuration.feedsLikeParams),
            showCart = channel.configuration.showCart,
            showPinnedProduct = channel.configuration.showPinnedProduct,
            titleBottomSheet = channel.configuration.pinnedProduct.titleBottomSheet,
            shareInfo = mapShareInfo(channel.share, channel)
    )

    private fun mapPartnerInfo(partner: Channel.Partner) = PartnerInfoUiModel(
            id = partner.id.toLongOrZero(),
            name = partner.name,
            type = PartnerType.getTypeByValue(partner.type),
            isFollowed = true,
            isFollowable = false
    )

    private fun mapFeedInfo(feedInfo: Channel.FeedLikeParam) = FeedInfoUiModel(
            contentId = feedInfo.contentId,
            contentType = feedInfo.contentType,
            likeType = feedInfo.likeType
    )

    private fun mapShareInfo(shareInfo: Channel.Share, channel: Channel): ShareInfoUiModel {
        val fullShareContent = try {
            shareInfo.text.replace("${'$'}{url}", shareInfo.redirectUrl)
        } catch (e: Throwable) {
            "${shareInfo.text}/n${shareInfo.redirectUrl}"
        }

        return ShareInfoUiModel(
                content = fullShareContent,
                isShowButton = shareInfo.isShowButton
                        && shareInfo.redirectUrl.isNotBlank()
                        && channel.configuration.active
                        && !channel.configuration.freezed
        )
    }

    fun mapPinnedMessage(partnerName: String, pinnedMessage: PinnedMessage) = if (pinnedMessage.pinnedMessageId > 0 && pinnedMessage.title.isNotEmpty()) {
        PinnedMessageUiModel(
                applink = pinnedMessage.redirectUrl,
                partnerName = partnerName,
                title = pinnedMessage.title
        )
    } else null

    private fun mapPinnedMessage(partnerName: String, pinnedMessage: Channel.PinnedMessage) = if (pinnedMessage.id.isNotEmpty()
            && !pinnedMessage.id.contentEquals("0")
            && pinnedMessage.title.isNotEmpty()) {
        PinnedMessageUiModel(
                applink = pinnedMessage.redirectUrl,
                partnerName = partnerName,
                title = pinnedMessage.title
        )
    } else null

    private fun mapPinnedProduct(partnerName: String, configuration: Channel.Configuration) = if (configuration.showPinnedProduct)
        PinnedProductUiModel(
            partnerName = partnerName,
            title = configuration.pinnedProduct.title,
            hasPromo = configuration.hasPromo
    ) else null

    private fun mapVideoStream(video: Video, config: Channel.Configuration, isLive: Boolean) = VideoStreamUiModel(
            uriString = video.streamSource,
            channelType = if (isLive) PlayChannelType.Live else PlayChannelType.VOD,
            orientation = VideoOrientation.getByValue(video.orientation),
            buffer = mapVideoBufferControl(video.bufferControl),
            backgroundUrl = config.roomBackground.imageUrl,
//            channelType = PlayChannelType.Live,
//            orientation = VideoOrientation.Horizontal(16, 9),
//            backgroundUrl = "https://i.pinimg.com/736x/d3/bb/7b/d3bb7b85f4e160d013f68fcde8d19844.jpg",
            isActive = config.active,
            lastMillis = null
    )

    private fun mapVideoPlayer(video: Video, exoPlayer: ExoPlayer) = when (video.type) {
        "live", "vod" -> General(exoPlayer)
        "youtube" -> YouTube(video.streamSource)
        else -> Unknown
    }

    private fun mapVideoBufferControl(bufferControl: Video.BufferControl?): PlayBufferControl {
        return if (bufferControl != null) {
            PlayBufferControl(
                    minBufferMs = bufferControl.minBufferingSecond * MS_PER_SECOND,
                    maxBufferMs = bufferControl.maxBufferingSecond * MS_PER_SECOND,
                    bufferForPlaybackMs = bufferControl.bufferForPlayback * MS_PER_SECOND,
                    bufferForPlaybackAfterRebufferMs = bufferControl.bufferForPlaybackAfterRebuffer * MS_PER_SECOND
            )
        } else PlayBufferControl()
    }

    fun mapQuickReply(quickReplyList: List<String>) = QuickReplyUiModel(quickReplyList.filterNot { quickReply -> quickReply.isEmpty() || quickReply.isBlank() } )
    fun mapQuickReply(quickReply: QuickReply) = mapQuickReply(quickReply.data)

    private fun mapTotalLikes(totalLike: Int, totalLikeString: String) = TotalLikeUiModel(totalLike, totalLikeString)
    fun mapTotalLikes(totalLike: TotalLike) = mapTotalLikes(totalLike.totalLike, totalLike.totalLikeFormatted)

    private fun mapTotalViews(totalViewString: String) = TotalViewUiModel(totalViewString)
    fun mapTotalViews(totalView: TotalView) = mapTotalViews(totalView.totalViewFormatted)

    fun mapPlayChat(userId: String, playChat: PlayChat) = PlayChatUiModel(
            messageId = playChat.messageId,
            userId = playChat.user.id,
            name = playChat.user.name,
            message = playChat.message,
            isSelfMessage = playChat.user.id == userId
    )

    fun mapProductSheet(title: String, partnerId: Long, productTagging: ProductTagging): ProductSheetUiModel {
        return ProductSheetUiModel(
                title = title,
                partnerId = partnerId,
                productList = mapItemProducts(productTagging.listOfProducts),
                voucherList = mapItemVouchers(productTagging.listOfVouchers)
        )
    }

    fun mapItemProducts(products: List<Product>): List<ProductLineUiModel> {
        return products.map { product ->
            mapItemProduct(product)
        }
    }

    fun mapItemProduct(product: Product) = ProductLineUiModel(
            id = product.id.toString(),
            shopId = product.shopId,
            imageUrl = product.image,
            title = product.name,
            price = if (product.discount != 0) {
                DiscountedPrice(
                        originalPrice = product.originalPriceFormatted,
                        discountedPrice = product.priceFormatted,
                        discountedPriceNumber = product.price,
                        discountPercent = product.discount
                )
            } else {
                OriginalPrice(price = product.originalPriceFormatted,
                        priceNumber = product.originalPrice)
            },
            isVariantAvailable = product.isVariant,
            stock = if (product.isAvailable) StockAvailable(product.quantity) else OutOfStock,
            minQty = product.minimumQuantity,
            isFreeShipping = product.isFreeShipping,
            applink = product.appLink
    )

    fun mapItemVouchers(vouchers: List<Voucher>): List<MerchantVoucherUiModel> {
        return vouchers.map {
            MerchantVoucherUiModel(
                    title = it.title,
                    description = it.subtitle,
                    type = when(it.voucherType) {
                        /**
                         * 1 -> Free Ongkir
                         * 2 -> **Not Used**
                         * 3 -> Cashback
                         */
                        1 -> MerchantVoucherType.Shipping
                        2,3 -> MerchantVoucherType.Discount
                        else -> MerchantVoucherType.Unknown
                    }
            )
        }
    }
}