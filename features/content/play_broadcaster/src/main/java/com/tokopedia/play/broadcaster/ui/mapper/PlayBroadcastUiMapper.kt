package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.type.OutOfStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_BROADCAST_SCHEDULE
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play.broadcaster.util.extension.convertMillisToMinuteSecond
import com.tokopedia.play.broadcaster.util.extension.toDateWithFormat
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.util.*

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
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: (Boolean) -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.id.toLong(),
                name = it.name,
                imageUrl = it.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                originalImageUrl = it.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                stock = if (it.stock > 0) StockAvailable(it.stock) else OutOfStock,
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
                followersList = List(3) {
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
                TrafficMetricUiModel(TrafficMetricsEnum.TotalViews, metrics.visitChannelFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.VideoLikes, metrics.likeChannelFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.NewFollowers, metrics.followShopFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.ShopVisit, metrics.visitShopFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.ProductVisit, metrics.visitPdpFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.NumberOfAtc, metrics.addToCartFmt),
                TrafficMetricUiModel(TrafficMetricsEnum.NumberOfPaidOrders, metrics.paymentVerifiedFmt)
        )

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel = TotalViewUiModel(
            totalView.totalViewFmt
    )

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel = TotalLikeUiModel(totalLike.totalLikeFmt)

    override fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel> = metric.metricList.map {
        PlayMetricUiModel(
                iconUrl = it.icon,
                spannedSentence = MethodChecker.fromHtml(it.sentence),
                type = it.metricType,
                interval = it.interval
        )
    }

    override fun mapProductTag(productTag: ProductTagging): List<ProductData> = productTag.productList.map {
        ProductData(
                id = it.id,
                name = it.name,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock
        )
    }

    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        val channelStatus = ChannelType.getChannelType(
                config.activeLiveChannel,
                config.pausedChannel,
                config.draftChannel,
                config.completeDraft
        )

        val maxDuration = config.maxDuration * 1000
        val remainingTime = when(channelStatus.second) {
            ChannelType.Active -> config.activeChannelRemainingDuration*1000
            ChannelType.Pause -> config.pausedChannelRemainingDuration*1000
            else -> maxDuration
        }

        return ConfigurationUiModel(
                streamAllowed = config.streamAllowed,
                channelId = channelStatus.first,
                channelType =  channelStatus.second,
                remainingTime = remainingTime,
                timeElapsed = (maxDuration - remainingTime).convertMillisToMinuteSecond(),
                durationConfig = DurationConfigUiModel(
                        duration = maxDuration,
                        maxDurationDesc = config.maxDurationDesc,
                        pauseDuration = config.maxPauseDuration * 1000,
                        errorMessage = config.maxDurationDesc),
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
                )
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
                id = it.productID.toLongOrZero(),
                name = it.productName,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock
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
}