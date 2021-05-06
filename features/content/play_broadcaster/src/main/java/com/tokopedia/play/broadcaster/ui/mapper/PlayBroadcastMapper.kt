package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 21/09/20
 */
interface PlayBroadcastMapper {

    fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel>

    fun mapProductList(
            productsResponse: GetProductsByEtalaseResponse.GetProductListData,
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: (Boolean) -> SelectableState
    ): List<ProductContentUiModel>

    fun mapSearchSuggestionList(
            keyword: String,
            productsResponse: GetProductsByEtalaseResponse.GetProductListData
    ): List<SearchSuggestionUiModel>

    fun mapLiveFollowers(
            response: GetLiveFollowersResponse
    ): FollowerDataUiModel

    fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia): LiveStreamInfoUiModel

    fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel>

    fun mapTotalView(totalView: TotalView): TotalViewUiModel

    fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel

    fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel>

    fun mapProductTag(productTag: ProductTagging): List<ProductData>

    fun mapConfiguration(config: Config): ConfigurationUiModel

    fun mapChannelInfo(channel: GetChannelResponse.Channel): ChannelInfoUiModel

    fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>): List<ProductData>

    fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel

    fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String, coverTitle: String): PlayCoverUiModel

    fun mapShareInfo(channel: GetChannelResponse.Channel): ShareUiModel

    fun mapLiveDuration(duration: String): LiveDurationUiModel

    fun mapIncomingChat(chat: Chat): PlayChatUiModel

    fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel

    fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel
}