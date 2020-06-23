package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.SelectableState
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
            productsResponse: GetProductsByEtalaseResponse.GetShopProductData,
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: (Boolean) -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.productId.toLong(),
                name = it.name,
                imageUrl = it.primaryImage.resize300,
                originalImageUrl = it.primaryImage.original,
                stock = 2,
//                stock = it.stock, // TODO("uncomment")
                isSelectedHandler = isSelectedHandler,
                isSelectable = isSelectableHandler
        )
    }

    fun mapSearchSuggestionList(
            keyword: String,
            productsResponse: GetProductsByEtalaseResponse.GetShopProductData
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
}