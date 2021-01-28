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
class PlayBroadcastConfigurableMapper(
        private val uiMapper: PlayBroadcastMapper,
        private val mockMapper: PlayBroadcastMapper
) : PlayBroadcastMapper {

    private val isMock = false

    override fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel> {
        return if (!isMock) uiMapper.mapEtalaseList(etalaseList)
        else mockMapper.mapEtalaseList(etalaseList)
    }

    override fun mapProductList(productsResponse: GetProductsByEtalaseResponse.GetProductListData, isSelectedHandler: (Long) -> Boolean, isSelectableHandler: (Boolean) -> SelectableState): List<ProductContentUiModel> {
        return if (!isMock) uiMapper.mapProductList(productsResponse, isSelectedHandler, isSelectableHandler)
        else mockMapper.mapProductList(productsResponse, isSelectedHandler, isSelectableHandler)
    }

    override fun mapSearchSuggestionList(keyword: String, productsResponse: GetProductsByEtalaseResponse.GetProductListData): List<SearchSuggestionUiModel> {
        return if (!isMock) uiMapper.mapSearchSuggestionList(keyword, productsResponse)
        else mockMapper.mapSearchSuggestionList(keyword, productsResponse)
    }

    override fun mapLiveFollowers(response: GetLiveFollowersResponse): FollowerDataUiModel {
        return if (!isMock) uiMapper.mapLiveFollowers(response)
        else mockMapper.mapLiveFollowers(response)
    }

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia): LiveStreamInfoUiModel {
        return if (!isMock) uiMapper.mapLiveStream(channelId, media)
        else mockMapper.mapLiveStream(channelId, media)
    }

    override fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel> {
        return if (!isMock) uiMapper.mapToLiveTrafficUiMetrics(metrics)
        else mockMapper.mapToLiveTrafficUiMetrics(metrics)
    }

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel {
        return if (!isMock) uiMapper.mapTotalView(totalView)
        else mockMapper.mapTotalView(totalView)
    }

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel {
        return if (!isMock) uiMapper.mapTotalLike(totalLike)
        else mockMapper.mapTotalLike(totalLike)
    }

    override fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel> {
        return if (!isMock) uiMapper.mapNewMetricList(metric)
        else mockMapper.mapNewMetricList(metric)
    }

    override fun mapProductTag(productTag: ProductTagging): List<ProductData> {
        return if (!isMock) uiMapper.mapProductTag(productTag)
        else mockMapper.mapProductTag(productTag)
    }

    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        return if (!isMock) uiMapper.mapConfiguration(config)
        else mockMapper.mapConfiguration(config)
    }

    override fun mapChannelInfo(channel: GetChannelResponse.Channel): ChannelInfoUiModel {
        return if (!isMock) uiMapper.mapChannelInfo(channel)
        else mockMapper.mapChannelInfo(channel)
    }

    override fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>): List<ProductData> {
        return if (!isMock) uiMapper.mapChannelProductTags(productTags)
        else mockMapper.mapChannelProductTags(productTags)
    }

    override fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel {
        return if (!isMock) uiMapper.mapChannelSchedule(timestamp)
        else mockMapper.mapChannelSchedule(timestamp)
    }

    override fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String, coverTitle: String): PlayCoverUiModel {
        return if (!isMock) uiMapper.mapCover(setupCover, coverUrl, coverTitle)
        else mockMapper.mapCover(setupCover, coverUrl, coverTitle)
    }

    override fun mapShareInfo(channel: GetChannelResponse.Channel): ShareUiModel {
        return if (!isMock) uiMapper.mapShareInfo(channel)
        else mockMapper.mapShareInfo(channel)
    }

    override fun mapLiveDuration(duration: String): LiveDurationUiModel {
        return if (!isMock) uiMapper.mapLiveDuration(duration)
        else mockMapper.mapLiveDuration(duration)
    }

    override fun mapIncomingChat(chat: Chat): PlayChatUiModel {
        return if (!isMock) uiMapper.mapIncomingChat(chat)
        else mockMapper.mapIncomingChat(chat)
    }

    override fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel {
        return if (!isMock) uiMapper.mapFreezeEvent(freezeEvent, event)
        else mockMapper.mapFreezeEvent(freezeEvent, event)
    }

    override fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel {
        return if (!isMock) uiMapper.mapBannedEvent(bannedEvent, event)
        else mockMapper.mapBannedEvent(bannedEvent, event)
    }
}