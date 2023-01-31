package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.config.GetBroadcastingConfigurationResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetInteractiveConfigResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizChoiceDetailResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizDetailResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.type.*
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.GiveawayConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_BROADCAST_SCHEDULE
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play.broadcaster.util.extension.toDateWithFormat
import com.tokopedia.play.broadcaster.util.helper.UriParser
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 02/06/20
 */
class PlayBroadcastUiMapper @Inject constructor(
    private val textTransformer: HtmlTextTransformer,
    private val uriParser: UriParser,
) : PlayBroadcastMapper {

    override fun mapBroadcastingConfig(response: GetBroadcastingConfigurationResponse): BroadcastingConfigUIModel {
        return BroadcastingConfigUIModel(
            authorID = response.broadcasterGetBroadcastingConfig.authorID,
            authorType = response.broadcasterGetBroadcastingConfig.authorType,
            config = BroadcastingConfigUIModel.Config(
                audioRate = response.broadcasterGetBroadcastingConfig.config.audioRate,
                bitrateMode = response.broadcasterGetBroadcastingConfig.config.bitrateMode,
                fps = response.broadcasterGetBroadcastingConfig.config.fps,
                maxRetry = response.broadcasterGetBroadcastingConfig.config.maxRetry,
                reconnectDelay = response.broadcasterGetBroadcastingConfig.config.reconnectDelay,
                videoBitrate = response.broadcasterGetBroadcastingConfig.config.videoBitrate,
                videoHeight = response.broadcasterGetBroadcastingConfig.config.videoHeight,
                videoWidth = response.broadcasterGetBroadcastingConfig.config.videoWidth,
            )
        )
    }

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia) =
        LiveStreamInfoUiModel(
            ingestUrl = media.ingestUrl,

            )

    override fun mapToLiveTrafficUiMetrics(authorType: String, metrics: LiveStats): List<TrafficMetricUiModel> =
        if (authorType == TYPE_SHOP) {
            mutableListOf(
                TrafficMetricUiModel(TrafficMetricType.TotalViews, metrics.visitChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, metrics.likeChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.NewFollowers, metrics.followShopFmt),
                TrafficMetricUiModel(TrafficMetricType.ShopVisit, metrics.visitShopFmt),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, metrics.visitPdpFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, metrics.addToCartFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, metrics.paymentVerifiedFmt)
            )
        } else {
            mutableListOf(
                TrafficMetricUiModel(TrafficMetricType.TotalViews, metrics.visitChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, metrics.likeChannelFmt),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, metrics.visitPdpFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, metrics.addToCartFmt),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, metrics.paymentVerifiedFmt)
            )
        }

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel = TotalViewUiModel(
        totalView.totalViewFmt
    )

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel =
        TotalLikeUiModel(totalLike.totalLikeFmt)

    override fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel> =
        metric.metricList.map {
            PlayMetricUiModel(
                iconUrl = it.icon,
                spannedSentence = textTransformer.transform(it.sentence),
                type = it.metricType,
                interval = it.interval
            )
        }

    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        val channelStatus = ChannelStatus.getChannelType(
            config.activeLiveChannel,
            config.pausedChannel,
            config.draftChannel,
            config.completeDraft
        )

        val remainingDuration = when (channelStatus.second) {
            ChannelStatus.Live -> config.maxDuration - config.activeChannelRemainingDuration
            ChannelStatus.Pause -> config.maxDuration - config.pausedChannelRemainingDuration
            else -> 0
        }

        return ConfigurationUiModel(
            streamAllowed = config.streamAllowed,
            shortVideoAllowed = config.shortVideoAllowed,
            channelId = channelStatus.first,
            channelStatus = channelStatus.second,
            durationConfig = DurationConfigUiModel(
                remainingDuration = TimeUnit.SECONDS.toMillis(remainingDuration),
                maxDuration = TimeUnit.SECONDS.toMillis(config.maxDuration),
                maxDurationDesc = config.maxDurationDesc,
                pauseDuration = TimeUnit.SECONDS.toMillis(config.maxPauseDuration),
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
        status = ChannelStatus.getByValue(channel.basic.status.id)
    )

    override fun mapChannelSchedule(
        timestamp: GetChannelResponse.Timestamp,
        status: GetChannelResponse.ChannelBasicStatus
    ): BroadcastScheduleUiModel {
        return if (timestamp.publishedAt.isBlank() || ChannelStatus.getByValue(status.id) == ChannelStatus.Live) BroadcastScheduleUiModel.NoSchedule
        else {
            val scheduleDate = timestamp.publishedAt.toDateWithFormat(DATE_FORMAT_RFC3339)
            BroadcastScheduleUiModel.Scheduled(
                time = scheduleDate,
                formattedTime = scheduleDate.toFormattedString(
                    DATE_FORMAT_BROADCAST_SCHEDULE,
                    Locale("id", "ID")
                )
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
                coverImage = uriParser.parse(coverUrl),
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

    override fun mapChannelSummary(
        title: String,
        coverUrl: String,
        date: String,
        duration: String,
        isEligiblePostVideo: Boolean,
        author: ContentAccountUiModel,
    ) = ChannelSummaryUiModel(
        title = title,
        coverUrl = coverUrl,
        date = date,
        duration = duration,
        isEligiblePostVideo = isEligiblePostVideo,
        author = author,
    )

    override fun mapIncomingChat(chat: Chat): PlayChatUiModel = PlayChatUiModel(
        messageId = chat.messageId,
        message = chat.message,
        userId = chat.user.id,
        name = chat.user.name,
        isSelfMessage = false
    )

    override fun mapFreezeEvent(freezeEvent: Freeze, event: EventUiModel?): EventUiModel =
        EventUiModel(
            freeze = freezeEvent.isFreeze,
            banned = event?.banned ?: false,
            title = event?.title.orEmpty(),
            message = event?.message.orEmpty(),
            buttonTitle = event?.buttonTitle.orEmpty()
        )

    override fun mapBannedEvent(bannedEvent: Banned, event: EventUiModel?): EventUiModel =
        EventUiModel(
            freeze = event?.freeze ?: false,
            banned = true,
            title = bannedEvent.title,
            message = bannedEvent.reason,
            buttonTitle = bannedEvent.btnText
        )

    override fun mapInteractiveConfig(authorType: String, response: GetInteractiveConfigResponse): InteractiveConfigUiModel {
        val interactiveDuration = response.interactiveConfig.tapTapConfig.interactiveDuration

        val quizDurationInMs = response.interactiveConfig.quizConfig.quizDurationsInSeconds.map {
            TimeUnit.SECONDS.toMillis(it.toLong())
        }

        return InteractiveConfigUiModel(
            giveawayConfig = GiveawayConfigUiModel(
                isActive = if (authorType == TYPE_SHOP) response.interactiveConfig.tapTapConfig.isActive else false,
                nameGuidelineHeader = response.interactiveConfig.tapTapConfig.interactiveNamingGuidelineHeader,
                nameGuidelineDetail = response.interactiveConfig.tapTapConfig.interactiveNamingGuidelineDetail,
                timeGuidelineHeader = response.interactiveConfig.tapTapConfig.interactiveTimeGuidelineHeader,
                timeGuidelineDetail = response.interactiveConfig.tapTapConfig.interactiveTimeGuidelineDetail
                    .replace(FORMAT_INTERACTIVE_DURATION, interactiveDuration.toString()),
                durationInMs = TimeUnit.SECONDS.toMillis(interactiveDuration.toLong()),
                availableStartTimeInMs = response.interactiveConfig.tapTapConfig.countdownPickerTime.map {
                    TimeUnit.SECONDS.toMillis(it.toLong())
                },
            ),
            quizConfig = QuizConfigUiModel(
                isActive = if (authorType == TYPE_SHOP) response.interactiveConfig.tapTapConfig.isActive else false,
                isGiftActive = authorType == TYPE_SHOP,
                maxTitleLength = response.interactiveConfig.quizConfig.maxTitleLength,
                maxChoicesCount = response.interactiveConfig.quizConfig.maxChoicesCount,
                minChoicesCount = response.interactiveConfig.quizConfig.minChoicesCount,
                maxChoiceLength = response.interactiveConfig.quizConfig.maxChoiceLength,
                availableStartTimeInMs = quizDurationInMs,
                eligibleStartTimeInMs = quizDurationInMs,
            ),
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

    override fun mapQuizOptionToChoice(option: QuizFormDataUiModel.Option): PostInteractiveCreateQuizUseCase.Choice {
        return PostInteractiveCreateQuizUseCase.Choice(
            text = option.text,
            correct = option.isSelected
        )
    }

    override fun mapQuizDetail(
        response: GetInteractiveQuizDetailResponse,
        interactiveId: String
    ): QuizDetailDataUiModel {
        return with(response.playInteractiveQuizDetail) {
            QuizDetailDataUiModel(
                question = textTransformer.transform(question),
                reward = textTransformer.transform(reward),
                countDownEnd = countdownEnd,
                choices = choices.map {
                    QuizDetailDataUiModel.Choice(
                        id = it.id,
                        text = textTransformer.transform(it.text),
                        isCorrectAnswer = it.isCorrectAnswer,
                        participantCount = it.participantCount
                    )
                },
                interactiveId = interactiveId,
            )
        }
    }

    override fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel, endTime: Calendar?): List<LeaderboardGameUiModel> {
        val choices = dataUiModel.choices.mapIndexed { index, choice ->
            LeaderboardGameUiModel.QuizOption(
                QuizChoicesUiModel(
                    index = index,
                    id = choice.id,
                    text = textTransformer.transform(choice.text),
                    type = PlayQuizOptionState.Participant(
                        alphabet = generateAlphabetChoices(index),
                        isCorrect = choice.isCorrectAnswer,
                        count = choice.participantCount.toString(),
                        showArrow = true
                    ),
                    interactiveId = dataUiModel.interactiveId,
                    interactiveTitle = textTransformer.transform(dataUiModel.question),
                )
            )
        }
        return mutableListOf<LeaderboardGameUiModel>().apply {
            add(LeaderboardGameUiModel.Header(title = dataUiModel.question, endsIn = endTime, leaderBoardType = LeadeboardType.Quiz,id = dataUiModel.interactiveId))
            addAll(choices)
        }
    }

    override fun mapChoiceDetail(
        response: GetInteractiveQuizChoiceDetailResponse,
        choiceIndex: Int,
        interactiveId: String,
        interactiveTitle: String,
    ): QuizChoiceDetailUiModel {
        return with(response.playInteractiveQuizChoiceDetail) {
            QuizChoiceDetailUiModel(
                choice = QuizChoicesUiModel(
                    index = choiceIndex,
                    id = choice.id,
                    text = textTransformer.transform(choice.text),
                    type = PlayQuizOptionState.Participant(
                        generateAlphabetChoices(choiceIndex),
                        choice.isCorrectAnswer,
                        "${choice.participantCount} Respon",
                        false,
                    ),
                    interactiveId = interactiveId,
                    interactiveTitle = interactiveTitle,
                ),
                cursor = cursor,
                winners = winners.map {
                    GameParticipantUiModel(
                        id = it.id,
                        name = it.firstName,
                        imageUrl = it.imageURL,
                        isWinner = true
                    )
                },
                participants = participants.map {
                    GameParticipantUiModel(
                        id = it.id,
                        name = it.firstName,
                        imageUrl = it.imageURL,
                        isWinner = false
                    )
                },
            )
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun mapLeaderBoardWithSlot(
        response: GetSellerLeaderboardSlotResponse,
        allowChat: Boolean
    ): List<LeaderboardGameUiModel> {
        return buildList {
            response.data.slots.forEach {
                //Header
                add(
                    LeaderboardGameUiModel.Header(
                        id = it.interactiveId,
                        reward = if (getLeaderboardType(it.type) == LeadeboardType.Quiz) "" else textTransformer.transform(it.reward),
                        leaderBoardType = getLeaderboardType(it.type),
                        title = it.getSlotTitle()
                    )
                )

                // Quiz if any
                if (it.choices.isNotEmpty()) addAll(it.choices.mapIndexed { index, choice ->
                    LeaderboardGameUiModel.QuizOption(
                        QuizChoicesUiModel(
                            index = index,
                            id = choice.id,
                            text = textTransformer.transform(choice.text),
                            type = PlayQuizOptionState.Participant(
                                alphabet = generateAlphabetChoices(index),
                                isCorrect = choice.isCorrectAnswer,
                                count = choice.participantCount.toString(),
                                showArrow = true
                            ),
                            interactiveId = it.interactiveId,
                            interactiveTitle = it.getSlotTitle()
                        )
                    )
                })

                //Winner if any
                if (it.winner.isNotEmpty()) addAll(
                    it.winner.mapIndexed { index, winner ->
                        LeaderboardGameUiModel.Winner(
                            rank = index + 1,
                            id = winner.userID,
                            name = winner.userName,
                            imageUrl = winner.imageUrl,
                            allowChat = { allowChat },
                            topChatMessage =
                            if (getLeaderboardType(it.type) == LeadeboardType.Giveaway)
                                response.data.config.topchatMessage
                                    .replace(FORMAT_FIRST_NAME, winner.userName)
                                    .replace(FORMAT_TITLE, it.getSlotTitle())
                            else
                                response.data.config.topchatMessageQuiz
                                    .replace(FORMAT_FIRST_NAME, winner.userName)
                                    .replace(FORMAT_TITLE, it.getSlotTitle()),
                        )
                    }
                )
                // need to add topChat

                //Footer
                add(
                    LeaderboardGameUiModel.Footer(
                        otherParticipantText = it.otherParticipantCountText,
                        otherParticipant = it.otherParticipantCount.toLong(),
                        leaderBoardType = getLeaderboardType(it.type),
                        totalParticipant = it.winner.size.toLong(),
                        emptyLeaderBoardCopyText = it.otherParticipantCountText,
                        id = it.interactiveId,
                    )
                )
            }
        }
    }

    private fun GetSellerLeaderboardSlotResponse.SlotData.getSlotTitle(): String {
        return if (getLeaderboardType(this.type) == LeadeboardType.Giveaway)
            this.title
        else textTransformer.transform(
            this.question
        )
    }

    private fun generateAlphabetChoices(index: Int): Char = arrayOfChoices[index]
    private val arrayOfChoices = ('A'..'D').toList()

    /***
     * Change to typename to make sure
     */
    private fun getLeaderboardType(leaderboardsResponse: String): LeadeboardType {
        return when (leaderboardsResponse) {
            "PlayInteractiveSellerLeaderboardGiveaway" -> LeadeboardType.Giveaway
            "PlayInteractiveSellerLeaderboardQuiz" -> LeadeboardType.Quiz
            else -> LeadeboardType.Unknown
        }
    }

    override fun mapBroadcasterMetric(
        metric: BroadcasterMetric,
        authorId: String,
        channelId: String
    ) = PlayBroadcasterMetric(
        authorId = authorId,
        channelId = channelId,
        videoBitrate = metric.videoBitrate,
        audioBitrate = metric.audioBitrate,
        resolution = "${metric.resolutionWidth}x${metric.resolutionHeight}",
        traffic = metric.traffic,
        bandwidth = metric.bandwidth,
        fps = metric.fps,
        packetLossIncreased = metric.packetLossIncreased,
        videoBufferTimestamp = metric.videoBufferTimestamp,
        audioBufferTimestamp = metric.audioBufferTimestamp,
    )

    override fun mapAuthorList(response: GetCheckWhitelistResponse): List<ContentAccountUiModel> {
        return response.whitelist.authors.map {
            ContentAccountUiModel(
                id = it.id,
                name = it.name,
                iconUrl = it.thumbnail,
                badge = it.badge,
                type = it.type,
                hasUsername = it.livestream.hasUsername,
                enable = it.livestream.enable,
            )
        }
    }

    companion object {
        private const val FORMAT_INTERACTIVE_DURATION = "${'$'}{second}"
        private const val FORMAT_FIRST_NAME = "{{first_name}}"
        private const val FORMAT_TITLE = "{{title}}"
        private const val TOTAL_FOLLOWERS = 3
    }

}
