package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.interactive.GetInteractiveConfigResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizChoiceDetailResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizDetailResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 21/09/20
 */
interface PlayBroadcastMapper {

    fun mapSearchSuggestionList(
        keyword: String,
        productsResponse: GetProductsByEtalaseResponse.GetProductListData
    ): List<SearchSuggestionUiModel>

    fun mapLiveFollowers(
        response: GetLiveFollowersResponse
    ): FollowerDataUiModel

    fun mapLiveStream(
        channelId: String,
        media: CreateLiveStreamChannelResponse.GetMedia
    ): LiveStreamInfoUiModel

    fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel>

    fun mapTotalView(totalView: TotalView): TotalViewUiModel

    fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel

    fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel>

    fun mapProductTag(productTag: ProductTagging): List<ProductData>

    fun mapConfiguration(config: Config): ConfigurationUiModel

    fun mapChannelInfo(channel: GetChannelResponse.Channel): ChannelInfoUiModel

    fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>): List<ProductData>

    fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel

    fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String): PlayCoverUiModel

    fun mapShareInfo(channel: GetChannelResponse.Channel): ShareUiModel

    fun mapChannelSummary(
        title: String,
        coverUrl: String,
        date: String,
        duration: String,
        isEligiblePostVideo: Boolean
    ): ChannelSummaryUiModel

    fun mapIncomingChat(chat: Chat): PlayChatUiModel

    fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel

    fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel

    fun mapInteractiveConfig(response: GetInteractiveConfigResponse): InteractiveConfigUiModel

    fun mapInteractiveSession(
        response: PostInteractiveCreateSessionResponse,
        title: String,
        durationInMs: Long
    ): InteractiveSessionUiModel

    fun mapPinnedMessage(
        response: GetPinnedMessageResponse.Data
    ): List<PinnedMessageUiModel>

    fun mapPinnedMessageSocket(
        response: PinnedMessageSocketResponse
    ): PinnedMessageUiModel

    fun mapQuizOptionToChoice(
        option: QuizFormDataUiModel.Option
    ): PostInteractiveCreateQuizUseCase.Choice

    fun mapQuizDetail(
        response: GetInteractiveQuizDetailResponse,
        interactiveId: String,
    ): QuizDetailDataUiModel

    fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel): PlayLeaderboardUiModel

    fun mapChoiceDetail(
        response: GetInteractiveQuizChoiceDetailResponse,
        choiceIndex: Int,
        interactiveId: String,
        interactiveTitle: String,
    ): QuizChoiceDetailUiModel

    fun mapLeaderBoardWithSlot(
        response: GetSellerLeaderboardSlotResponse,
        allowChat: Boolean,
    ): List<PlayLeaderboardUiModel>

    fun mapBroadcasterMetric(
        metric: BroadcasterMetric,
        authorId: String,
        channelId: String,
    ): PlayBroadcasterMetric

    fun mapAuthorList(response: WhitelistQuery): List<ContentAccountUiModel>
}