package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 02/06/20
 */
object PlayBroadcastUiMapper {

    fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel> = etalaseList.map {
        val type = EtalaseType.getByType(it.type, it.id)
        EtalaseContentUiModel(
                id = if (type is EtalaseType.Group) type.fMenu else it.id,
                name = it.name,
                productMap = mutableMapOf(),
                totalProduct = it.count,
                stillHasProduct = true
        )
    }

    fun mapProductList(
            productsResponse: GetProductsByEtalaseResponse.GetProductListData,
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: (Boolean) -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.id.toLong(),
                name = it.name,
                imageUrl = it.pictures.first().urlThumbnail,
                originalImageUrl = it.pictures.first().urlThumbnail,
                stock = it.stock,
                isSelectedHandler = isSelectedHandler,
                isSelectable = isSelectableHandler
        )
    }

    fun mapSearchSuggestionList(
            keyword: String,
            productsResponse: GetProductsByEtalaseResponse.GetProductListData
    ) = productsResponse.data.map {
        val fullSuggestedText = it.name
        val startIndex = fullSuggestedText.indexOf(keyword)
        val lastIndex = startIndex + keyword.length

        SearchSuggestionUiModel(
                queriedText = keyword,
                suggestedText = it.name,
                spannedSuggestion = SpannableStringBuilder(fullSuggestedText).apply {
                    if (startIndex >= 0) setSpan(StyleSpan(Typeface.BOLD), startIndex, lastIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    fun mapLiveFollowers(
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

    fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia) =
            LiveStreamInfoUiModel(
                    channelId = channelId,
                    ingestUrl = media.ingestUrl,
                    streamUrl = media.streamUrl)

    fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel> = mutableListOf(
                TrafficMetricUiModel(TrafficMetricsEnum.TotalViews, metrics.visitChannel),
                TrafficMetricUiModel(TrafficMetricsEnum.VideoLikes, metrics.likeChannel),
                TrafficMetricUiModel(TrafficMetricsEnum.ShopVisit, metrics.visitShop),
                TrafficMetricUiModel(TrafficMetricsEnum.ProductVisit, metrics.visitPdp),
                TrafficMetricUiModel(TrafficMetricsEnum.NumberOfAtc, metrics.addToCart),
                TrafficMetricUiModel(TrafficMetricsEnum.NumberOfPaidOrders, metrics.paymentVerified),
                TrafficMetricUiModel(TrafficMetricsEnum.NewFollowers, metrics.followShop)
        )

    fun mapTotalView(concurrentUser: ConcurrentUser): TotalViewUiModel = TotalViewUiModel(
            concurrentUser.totalUsers.toString()
    )

    fun mapTotalLike(stat: LiveStats): TotalLikeUiModel = TotalLikeUiModel(stat.totalLikeFmt)

    fun mapMetricList(metric: Metric): MutableList<PlayMetricUiModel> {
        val metricList = mutableListOf<PlayMetricUiModel>()
        if (metric.newParticipant.firstSentence.isNotEmpty())
            metricList.add(mapMetric(metric.newParticipant))
        if (metric.pdpVisitor.firstSentence.isNotEmpty())
            metricList.add(mapMetric(metric.pdpVisitor))
        if (metric.shopVisitor.firstSentence.isNotEmpty())
            metricList.add(mapMetric(metric.shopVisitor))
        return metricList
    }

    private fun mapMetric(data: Metric.MetricData): PlayMetricUiModel {
        val firstSentence = data.firstSentence
        val secondSentence = data.secondSentence
        val fullSentence = "$firstSentence $secondSentence"
        return PlayMetricUiModel(
                firstSentence = firstSentence,
                secondSentence = secondSentence,
                fullSentence = fullSentence,
                interval = data.interval
        )
    }

    fun mapConfiguration(config: Config): ConfigurationUiModel {
        val channelStatus = ChannelType.getChannelType(
                config.activeLiveChannel,
                config.pausedChannel,
                config.draftChannel
        )
        return ConfigurationUiModel(
                streamAllowed = config.streamAllowed,
                channelId = channelStatus.first,
                channelType =  channelStatus.second,
                durationConfig = DurationConfigUiModel(
                        duration = config.maxDuration,
                        pauseDuration = config.maxPauseDuration,
                        errorMessage = config.maxDurationDesc),
                productTagConfig = ProductTagConfigUiModel(
                        maxProduct = config.maxTaggedProduct,
                        minProduct = config.minTaggedProduct,
                        errorMessage = config.maxTaggedProductDesc
                ),
                countDown = config.countdownSec
        )
    }

    fun mapChannelInfo(channel: GetChannelResponse.Channel) = ChannelInfoUiModel(
            channelId = channel.basic.channelId,
            title = channel.basic.title,
            description = channel.basic.description,
            ingestUrl = channel.medias.firstOrNull()?.ingestUrl.orEmpty(),
//            ingestUrl = LOCAL_RTMP_URL, // TODO remove mock
            coverUrl = channel.basic.coverUrl,
            status = PlayChannelStatus.getByValue(channel.basic.status.id)
    )

    fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>) = productTags.map {
        ProductData(
                id = it.productID.toLongOrZero(),
                name = it.productName,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = it.quantity
        )
    }

    fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String, coverTitle: String): PlayCoverUiModel {
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
                title = coverTitle
        )
    }

    fun mapShareInfo(channel: GetChannelResponse.Channel) = ShareUiModel(
            id = channel.basic.channelId,
            title = channel.share.metaTitle,
            description = channel.share.metaDescription,
            imageUrl = channel.basic.coverUrl,
            textContent = channel.share.text,
            redirectUrl = channel.share.redirectURL,
            shortenUrl = channel.share.useShortURL
    )

    fun mapLiveDuration(duration: LiveDuration): DurationUiModel = DurationUiModel(
            duration = duration.duration,
            remaining = duration.remaining,
            maxDuration = duration.maxDuration
    )
}