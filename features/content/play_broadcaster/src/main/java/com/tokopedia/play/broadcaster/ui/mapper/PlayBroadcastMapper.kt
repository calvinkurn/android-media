package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
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
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import java.util.*

/**
 * Created by jegul on 21/09/20
 */
interface PlayBroadcastMapper {

    fun mapLiveStream(
        channelId: String,
        media: CreateLiveStreamChannelResponse.GetMedia
    ): LiveStreamInfoUiModel

    fun mapToLiveTrafficUiMetrics(authorType: String, metrics: LiveStats): List<TrafficMetricUiModel>

    fun mapTotalView(totalView: TotalView): TotalViewUiModel

    fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel

    fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel>

    fun mapConfiguration(config: Config): ConfigurationUiModel

    fun mapChannelInfo(channel: GetChannelResponse.Channel): ChannelInfoUiModel

    fun mapChannelSchedule(
        timestamp: GetChannelResponse.Timestamp,
        status: GetChannelResponse.ChannelBasicStatus,
    ): BroadcastScheduleUiModel

    fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String): PlayCoverUiModel

    fun mapShareInfo(channel: GetChannelResponse.Channel): ShareUiModel

    fun mapChannelSummary(
        title: String,
        coverUrl: String,
        date: String,
        duration: String,
        isEligiblePostVideo: Boolean,
        author: ContentAccountUiModel,
    ): ChannelSummaryUiModel

    fun mapIncomingChat(chat: Chat): PlayChatUiModel

    fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel

    fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel

    fun mapInteractiveConfig(authorType: String, response: GetInteractiveConfigResponse): InteractiveConfigUiModel

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

    fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel, endTime: Calendar?): List<LeaderboardGameUiModel>

    fun mapChoiceDetail(
        response: GetInteractiveQuizChoiceDetailResponse,
        choiceIndex: Int,
        interactiveId: String,
        interactiveTitle: String,
    ): QuizChoiceDetailUiModel

    fun mapLeaderBoardWithSlot(
        response: GetSellerLeaderboardSlotResponse,
        allowChat: Boolean,
    ): List<LeaderboardGameUiModel>

    fun mapBroadcasterMetric(
        metric: BroadcasterMetric,
        authorId: String,
        channelId: String,
    ): PlayBroadcasterMetric

    fun mapAuthorList(response: GetCheckWhitelistResponse): List<ContentAccountUiModel>
}
