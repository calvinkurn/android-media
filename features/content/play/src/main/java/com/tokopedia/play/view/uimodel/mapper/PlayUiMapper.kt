package com.tokopedia.play.view.uimodel.mapper

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by mzennis on 2020-03-06.
 */
object PlayUiMapper {

    fun createCompleteInfoModel(
            channel: Channel,
            partnerName: String,
            isBanned: Boolean,
            exoPlayer: ExoPlayer
    ) = PlayCompleteInfoUiModel(
            channelInfo = mapChannelInfo(channel),
            videoStream = mapVideoStream(
                    channel.videoStream,
                    channel.isActive,
                    channel.backgroundUrl),
            videoPlayer = mapVideoPlayer(
                    channel.videoStream,
                    exoPlayer
            ),
            pinnedMessage = mapPinnedMessage(
                    partnerName,
                    channel.pinnedMessage
            ),
            pinnedProduct = mapPinnedProduct(
                    partnerName,
                    channel.isShowProductTagging,
                    channel.pinnedProduct),
            quickReply = mapQuickReply(channel.quickReply),
            totalView = mapTotalViews(channel.totalViews),
            event = mapEvent(channel, isBanned)
    )

    private fun mapEvent(channel: Channel, isBanned: Boolean) = EventUiModel(
            isBanned = isBanned,
            isFreeze = !channel.isActive || channel.isFreeze,
            bannedMessage = channel.banned.message,
            bannedTitle = channel.banned.title,
            bannedButtonTitle = channel.banned.buttonTitle,
            freezeMessage = channel.freezeChannelState.desc,
            freezeTitle = channel.freezeChannelState.title,
            freezeButtonTitle = channel.freezeChannelState.btnTitle,
            freezeButtonUrl = channel.freezeChannelState.btnAppLink
    )

    fun mapChannelInfo(channel: Channel) = ChannelInfoUiModel(
            id = channel.channelId,
            title = channel.title,
            description = channel.description,
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

    fun mapPinnedProduct(partnerName: String, isShowProductTagging: Boolean, pinnedProduct: PinnedProduct) = if (isShowProductTagging)
        PinnedProductUiModel(
            partnerName = partnerName,
            title = pinnedProduct.title,
            isPromo = pinnedProduct.isShowDiscount
    ) else null

    // TODO("testing")
    fun mapVideoStream(videoStream: VideoStream, isActive: Boolean, backgroundUrl: String) = VideoStreamUiModel(
            uriString = videoStream.config.streamUrl,
            channelType = if (videoStream.isLive) PlayChannelType.Live else PlayChannelType.VOD,
            orientation = VideoOrientation.getByValue(videoStream.orientation),
            backgroundUrl = backgroundUrl,
//            channelType = PlayChannelType.Live,
//            orientation = VideoOrientation.Vertical,
//            backgroundUrl = "https://i.pinimg.com/736x/d3/bb/7b/d3bb7b85f4e160d013f68fcde8d19844.jpg",
            isActive = isActive
    )

    private fun mapVideoPlayer(videoStream: VideoStream, exoPlayer: ExoPlayer) = when (videoStream.type) {
        "live", "vod" -> General(exoPlayer)
//        "live", "vod" -> YouTube("HrjRj4uQQ1o")
        "youtube" -> YouTube(videoStream.config.youtubeId)
        else -> Unknown
    }

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
            type = PartnerType.Shop,
            isFollowed = shopInfo.favoriteData.alreadyFavorited == 1,
            isFollowable = shopId != shopInfo.shopCore.shopId
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