package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.broadcaster.mediator.LivePusherConfig
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.interactive.GetInteractiveConfigResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.type.*
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_BROADCAST_SCHEDULE
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play.broadcaster.util.extension.toDateWithFormat
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by jegul on 02/06/20
 */
class PlayBroadcastUiMapper(
        private val textTransformer: HtmlTextTransformer
) : PlayBroadcastMapper {

    override fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel> = etalaseList.map {
        val type = EtalaseType.getByType(it.type, it.id)
        EtalaseContentUiModel(
                id = if (type is EtalaseType.Group) type.fMenu else it.id,
                name = it.name,
                productMap = mutableMapOf(),
                totalProduct = it.count,
                stillHasProduct = true
        )
    }

    override fun mapProductList(
            productsResponse: GetProductsByEtalaseResponse.GetProductListData,
            isSelectedHandler: (String) -> Boolean,
            isSelectableHandler: (Boolean) -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.id,
                name = it.name,
                imageUrl = it.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                originalImageUrl = it.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                stock = if (it.stock > 0) StockAvailable(it.stock) else OutOfStock,
                price = PriceUnknown,
                isSelectedHandler = isSelectedHandler,
                isSelectable = isSelectableHandler
        )
    }

    override fun mapSearchSuggestionList(
            keyword: String,
            productsResponse: GetProductsByEtalaseResponse.GetProductListData
    ) = productsResponse.data.map {
        val fullSuggestedText = it.name
        val startIndex = fullSuggestedText.indexOf(keyword)
        val lastIndex = startIndex + keyword.length

        SearchSuggestionUiModel(
                queriedText = keyword,
                suggestedId = it.id,
                suggestedText = it.name,
                spannedSuggestion = SpannableStringBuilder(fullSuggestedText).apply {
                    if (startIndex >= 0) setSpan(StyleSpan(Typeface.BOLD), startIndex, lastIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    override fun mapLiveFollowers(
            response: GetLiveFollowersResponse
    ) : FollowerDataUiModel {
        val totalRetrievedFollowers = response.shopFollowerList.data.size
        return FollowerDataUiModel(
                followersList = List(TOTAL_FOLLOWERS) {
                    if (it >= totalRetrievedFollowers) FollowerUiModel.Unknown.fromIndex(it)
                    else FollowerUiModel.User(response.shopFollowerList.data[it].photo)
                },
                totalFollowers = response.shopInfoById.result.firstOrNull()?.favoriteData?.totalFavorite ?: 0
        )
    }

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia) =
            LiveStreamInfoUiModel(
                    channelId = channelId,
                    ingestUrl = media.ingestUrl,
                    streamUrl = media.streamUrl)

    override fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel> = mutableListOf(
                TrafficMetricUiModel(TrafficMetricType.TotalViews, metrics.visitChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, metrics.likeChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.NewFollowers, metrics.followShopFmt),
                TrafficMetricUiModel(TrafficMetricType.ShopVisit, metrics.visitShopFmt),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, metrics.visitPdpFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, metrics.addToCartFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, metrics.paymentVerifiedFmt)
        )

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel = TotalViewUiModel(
            totalView.totalViewFmt
    )

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel = TotalLikeUiModel(totalLike.totalLikeFmt)

    override fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel> = metric.metricList.map {
        PlayMetricUiModel(
                iconUrl = it.icon,
                spannedSentence = textTransformer.transform(it.sentence),
                type = it.metricType,
                interval = it.interval
        )
    }

    override fun mapProductTag(productTag: ProductTagging): List<ProductData> = productTag.productList.map {
        ProductData(
                id = it.id.toString(),
                name = it.name,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock,
                price = if(it.discount != 0) {
                    DiscountedPrice(
                        originalPrice = it.originalPriceFormatted,
                        originalPriceNumber = it.originalPrice,
                        discountedPrice = it.priceFormatted,
                        discountedPriceNumber = it.price,
                        discountPercent = it.discount
                    )
                }
                else {
                    OriginalPrice(price = it.originalPriceFormatted,
                        priceNumber = it.originalPrice)
                }
        )
    }

    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        val channelStatus = ChannelType.getChannelType(
                config.activeLiveChannel,
                config.pausedChannel,
                config.draftChannel,
                config.completeDraft
        )

        val maxDuration = TimeUnit.SECONDS.toMillis(config.maxDuration)
        val remainingTime = when(channelStatus.second) {
            ChannelType.Active -> TimeUnit.SECONDS.toMillis(config.activeChannelRemainingDuration)
            ChannelType.Pause -> TimeUnit.SECONDS.toMillis(config.pausedChannelRemainingDuration)
            else -> maxDuration
        }

        return ConfigurationUiModel(
            streamAllowed = config.streamAllowed,
            channelId = channelStatus.first,
            channelType = channelStatus.second,
            remainingTime = remainingTime,
            durationConfig = DurationConfigUiModel(
                duration = maxDuration,
                maxDurationDesc = config.maxDurationDesc,
                pauseDuration = TimeUnit.SECONDS.toMillis(config.maxPauseDuration),
                errorMessage = config.maxDurationDesc
            ),
            productTagConfig = ProductTagConfigUiModel(
                maxProduct = config.maxTaggedProduct,
                minProduct = config.minTaggedProduct,
                maxProductDesc = config.maxTaggedProductDesc,
                errorMessage = config.maxTaggedProductDesc
            ),
            coverConfig = CoverConfigUiModel(
                maxChars = config.maxTitleLength
            ),
            countDown = config.countdownSec,
            scheduleConfig = BroadcastScheduleConfigUiModel(
                minimum = config.scheduledTime.minimum.toDateWithFormat(DATE_FORMAT_RFC3339),
                maximum = config.scheduledTime.maximum.toDateWithFormat(DATE_FORMAT_RFC3339),
                default = config.scheduledTime.default.toDateWithFormat(DATE_FORMAT_RFC3339)
            ),
            tnc = config.tnc.map {
                TermsAndConditionUiModel(desc = it.description)
            },
        )
    }

   override fun mapChannelInfo(channel: GetChannelResponse.Channel) = ChannelInfoUiModel(
            channelId = channel.basic.channelId,
            title = channel.basic.title,
            description = channel.basic.description,
            ingestUrl = channel.medias.firstOrNull { it.id == channel.basic.activeMediaID }?.ingestUrl.orEmpty(),
            coverUrl = channel.basic.coverUrl,
            status = PlayChannelStatusType.getByValue(channel.basic.status.id)
    )

    override fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>) = productTags.map {
        ProductData(
                id = it.productID,
                name = it.productName,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock,
                price = if(it.discount.toInt() != 0) {
                            DiscountedPrice(
                                originalPrice = it.originalPriceFmt,
                                originalPriceNumber = it.originalPrice.toDouble(),
                                discountedPrice = it.priceFmt,
                                discountedPriceNumber = it.price.toDouble(),
                                discountPercent = it.discount.toInt()
                            )
                        }
                        else {
                            OriginalPrice(price = it.originalPriceFmt,
                                priceNumber = it.originalPrice.toDouble())
                        }
        )
    }

    override fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel {
        return if (timestamp.publishedAt.isBlank()) BroadcastScheduleUiModel.NoSchedule
        else {
            val scheduleDate = timestamp.publishedAt.toDateWithFormat(DATE_FORMAT_RFC3339)
            BroadcastScheduleUiModel.Scheduled(
                    time = scheduleDate,
                    formattedTime = scheduleDate.toFormattedString(DATE_FORMAT_BROADCAST_SCHEDULE, Locale("id", "ID"))
            )
        }
    }

    override fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String): PlayCoverUiModel {
        val prevSource = when (val prevCover = setupCover?.croppedCover) {
            is CoverSetupState.Cropped -> prevCover.coverSource
            else -> null
        }

        return PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Uploaded(
                        localImage = null,
                        coverImage = Uri.parse(coverUrl),
                        coverSource = prevSource ?: CoverSource.None
                ),
                state = SetupDataState.Uploaded,
        )
    }

    override fun mapShareInfo(channel: GetChannelResponse.Channel) = ShareUiModel(
            id = channel.basic.channelId,
            title = channel.share.metaTitle,
            description = channel.share.metaDescription,
            imageUrl = channel.basic.coverUrl,
            textContent = textTransformer.transform(channel.share.text),
            redirectUrl = channel.share.redirectURL,
            shortenUrl = channel.share.useShortURL
    )

    override fun mapLiveDuration(duration: String): LiveDurationUiModel = LiveDurationUiModel(
            duration = duration
    )

    override fun mapIncomingChat(chat: Chat): PlayChatUiModel = PlayChatUiModel(
            messageId = chat.messageId,
            message = chat.message,
            userId = chat.user.id,
            name = chat.user.name,
            isSelfMessage = false
    )

    override fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel = EventUiModel(
            freeze = freezeEvent.isFreeze,
            banned = event?.banned?:false,
            title = event?.title.orEmpty(),
            message = event?.message.orEmpty(),
            buttonTitle = event?.buttonTitle.orEmpty()
    )

    override fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel = EventUiModel(
            freeze = event?.freeze?:false,
            banned = true,
            title = bannedEvent.title,
            message = bannedEvent.reason,
            buttonTitle = bannedEvent.btnText
    )

    override fun mapInteractiveConfig(response: GetInteractiveConfigResponse): InteractiveConfigUiModel {
        val interactiveDuration = response.interactiveConfig.config.interactiveDuration

        return InteractiveConfigUiModel(
            isActive = response.interactiveConfig.config.isActive,
            nameGuidelineHeader = response.interactiveConfig.config.interactiveNamingGuidelineHeader,
            nameGuidelineDetail = response.interactiveConfig.config.interactiveNamingGuidelineDetail,
            timeGuidelineHeader = response.interactiveConfig.config.interactiveTimeGuidelineHeader,
            timeGuidelineDetail = response.interactiveConfig.config.interactiveTimeGuidelineDetail
                .replace(FORMAT_INTERACTIVE_DURATION, interactiveDuration.toString()),
            durationInMs = TimeUnit.SECONDS.toMillis(interactiveDuration.toLong()),
            availableStartTimeInMs = response.interactiveConfig.config.countdownPickerTime.map {
                TimeUnit.SECONDS.toMillis(it.toLong())
            },
        )
    }

    override fun mapInteractiveSession(
        response: PostInteractiveCreateSessionResponse,
        title: String,
        durationInMs: Long
    ): InteractiveSessionUiModel {
        return InteractiveSessionUiModel(
            response.interactiveSellerCreateSession.data.interactiveId,
            title,
            durationInMs
        )
    }

    override fun mapLiveInfo(
        activeIngestUrl: String,
        config: LivePusherConfig
    ): PlayLiveLogState {
        return PlayLiveLogState.Init(
            activeIngestUrl,
            config.videoWidth,
            config.videoHeight,
            config.fps,
            config.videoBitrate
        )
    }

    override fun mapPinnedMessage(
        response: GetPinnedMessageResponse.Data
    ): List<PinnedMessageUiModel> {
        return response.pinnedMessages.map {
            PinnedMessageUiModel(
                id = it.id,
                message = it.message,
                isActive = it.status.id == 1,
                editStatus = PinnedMessageEditStatus.Nothing
            )
        }
    }

    override fun mapPinnedMessageSocket(response: PinnedMessageSocketResponse): PinnedMessageUiModel {
        return PinnedMessageUiModel(
            id = response.pinnedMessageId,
            message = response.title,
            isActive = true,
            editStatus = PinnedMessageEditStatus.Nothing,
        )
    }

    companion object {
        private const val FORMAT_INTERACTIVE_DURATION = "${'$'}{second}"

        private const val TOTAL_FOLLOWERS = 3
    }

}