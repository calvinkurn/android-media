package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
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
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.*
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import java.util.*
import kotlin.random.Random

/**
 * Created by jegul on 21/09/20
 */
class PlayBroadcastMockMapper : PlayBroadcastMapper {

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

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia): LiveStreamInfoUiModel {
        return LiveStreamInfoUiModel(
                ingestUrl = LOCAL_RTMP_URL,
        )
    }

    override fun mapToLiveTrafficUiMetrics(authorType: String, metrics: LiveStats): List<TrafficMetricUiModel> {
        return if (authorType == TYPE_SHOP) {
            listOf(
                TrafficMetricUiModel(TrafficMetricType.GameParticipants, "2000"),
                TrafficMetricUiModel(TrafficMetricType.TotalViews, "2328"),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, "1800"),
                TrafficMetricUiModel(TrafficMetricType.ShopVisit, "1200"),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, "1042"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, "320"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, "200")
            )
        } else {
            listOf(
                TrafficMetricUiModel(TrafficMetricType.GameParticipants, "2000"),
                TrafficMetricUiModel(TrafficMetricType.TotalViews, "2328"),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, "1800"),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, "1042"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, "320"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, "200")
            )
        }
    }

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel {
        return TotalViewUiModel(totalView = "1234")
    }

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel {
        return TotalLikeUiModel(totalLike = "1234")
    }

    @Suppress("MagicNumber")
    override fun mapNewMetricList(metric: NewMetricList): List<PlayMetricUiModel> {
        return List(10) {
            PlayMetricUiModel(
                    iconUrl = "https://img.icons8.com/pastel-glyph/2x/shopping-cart--v2.png",
                    spannedSentence = MethodChecker.fromHtml("Kamu <b>membeli</b> produk ini yang keren banget itu loh waw yay astaga ini produk gila abis sih kerennya ampun dah"),
                    type = "new_participant",
                    interval = 3000
            )
        }
    }

    @Suppress("MagicNumber")
    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        return ConfigurationUiModel(
            streamAllowed = true,
            shortVideoAllowed = true,
            channelStatus = ChannelStatus.Draft,
            channelId = "10008", // 10008 prod, 10012 stag (status: draft)
            durationConfig = DurationConfigUiModel(
                remainingDuration = (30 * 60 * 1000),
                maxDuration = (30 * 60 * 1000),
                maxDurationDesc = "Siaran 30 menit",
                pauseDuration = (1 * 60 * 1000),
            ),
            productTagConfig = ProductTagConfigUiModel(
                maxProduct = 15,
                minProduct = 1,
                maxProductDesc = "Maks. Produk 15",
                errorMessage = "Oops, kamu sudah memilih 15 produk"
            ),
            coverConfig = CoverConfigUiModel(
                maxChars = 38
            ),
            countDown = 5,
            scheduleConfig = BroadcastScheduleConfigUiModel(
                minimum = Date(),
                maximum = Date(),
                default = Date()
            ),
            tnc = listOf(
                TermsAndConditionUiModel("Gak ada izin"),
                TermsAndConditionUiModel("Gak ada izin sama sekali"),
            )
        )
    }

    override fun mapChannelInfo(channel: GetChannelResponse.Channel): ChannelInfoUiModel {
        return ChannelInfoUiModel(
                channelId = "1234",
                title = "Klarifikasi Bisa Tebak Siapa?",
                description = "Yuk gabung sekarang di Play Klarifikasi Bisa Tebak siapa?",
                coverUrl = "https://images.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
                ingestUrl = LOCAL_RTMP_URL,
                status = ChannelStatus.Draft
        )
    }

    override fun mapChannelSchedule(
        timestamp: GetChannelResponse.Timestamp,
        status: GetChannelResponse.ChannelBasicStatus
    ): BroadcastScheduleUiModel {
        return BroadcastScheduleUiModel.NoSchedule
    }

    override fun mapCover(setupCover: PlayCoverUiModel?, coverUrl: String): PlayCoverUiModel {
        return PlayCoverUiModel.empty()
    }

    override fun mapShareInfo(channel: GetChannelResponse.Channel): ShareUiModel {
        return ShareUiModel(
                id = "1234",
                title = "Tokopedia PLAY seru!",
                description = "Nonton siaran seru di Tokopedia PLAY!",
                imageUrl = "https://images.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
                redirectUrl = "https://beta.tokopedia.com/play/channel/10140",
                textContent =  "\"testing\"\nYuk, nonton siaran dari MRG Audio di Tokopedia PLAY! Bakal seru banget lho!\n${'$'}{url}",
                shortenUrl = true
        )
    }

    override fun mapChannelSummary(title: String, coverUrl: String, date: String, duration: String, isEligiblePostVideo: Boolean, author: ContentAccountUiModel): ChannelSummaryUiModel {
        return ChannelSummaryUiModel(title, coverUrl, date, duration, isEligiblePostVideo, author)
    }

    override fun mapIncomingChat(chat: Chat): PlayChatUiModel {
        val name = listOf("Aku", "Kamu", "Dia", "Mereka").random()
        return PlayChatUiModel(
                messageId = System.currentTimeMillis().toString(),
                userId = Random.nextInt().toString(),
                name = name,
                message = listOf(":pepecry", ":pepelmao", ":lul").random(),
                isSelfMessage = false
        )
    }

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

    @Suppress("MagicNumber")
    override fun mapInteractiveConfig(authorType: String, response: GetInteractiveConfigResponse) = InteractiveConfigUiModel(
        giveawayConfig = GiveawayConfigUiModel(
            isActive = authorType == TYPE_SHOP,
            nameGuidelineHeader = "Mau kasih hadiah apa?",
            nameGuidelineDetail = "Contoh: Giveaway Sepatu, Tas Rp50 rb, Diskon 90%, Kupon Ongkir, HP Gratis, dll.",
            timeGuidelineHeader = "Kapan game-nya mulai?",
            timeGuidelineDetail = "Tentukan kapan game dimulai, dan game akan berlangsung selama 10 detik.",
            durationInMs = 10000L,
            availableStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
        ),
        quizConfig = QuizConfigUiModel(
            isActive = true,
            isGiftActive = authorType == TYPE_SHOP,
            maxTitleLength = 30,
            maxChoicesCount = 3,
            minChoicesCount = 2,
            maxChoiceLength = 35,
            availableStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
            eligibleStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
        ),
    )

    @Suppress("MagicNumber")
    override fun mapInteractiveSession(response: PostInteractiveCreateSessionResponse,
                                       title: String,
                                       durationInMs: Long): InteractiveSessionUiModel {
        return InteractiveSessionUiModel(
            "1",
            "Giveaway Tesla",
            60 * 1000L
        )
    }

    override fun mapPinnedMessage(
        response: GetPinnedMessageResponse.Data
    ): List<PinnedMessageUiModel> {
        return emptyList()
    }

    override fun mapPinnedMessageSocket(response: PinnedMessageSocketResponse): PinnedMessageUiModel {
        return PinnedMessageUiModel(
            id = "",
            message = "",
            isActive = false,
            editStatus = PinnedMessageEditStatus.Nothing,
        )
    }

    override fun mapQuizOptionToChoice(option: QuizFormDataUiModel.Option): PostInteractiveCreateQuizUseCase.Choice {
        return PostInteractiveCreateQuizUseCase.Choice(
            text = option.text,
            correct = option.isSelected
        )
    }

    override fun mapQuizDetail(response: GetInteractiveQuizDetailResponse, interactiveId: String): QuizDetailDataUiModel {
        return with(response.playInteractiveQuizDetail) {
            QuizDetailDataUiModel(
                question = question,
                reward = reward,
                countDownEnd = countdownEnd,
                choices = choices.map {
                    QuizDetailDataUiModel.Choice(
                        id = it.id,
                        text = it.text,
                        isCorrectAnswer = it.isCorrectAnswer,
                        participantCount = it.participantCount
                    )
                },
                interactiveId = interactiveId,
            )
        }
    }

    override fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel, endTime: Calendar?): List<LeaderboardGameUiModel> {
        return emptyList()
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
                    text = choice.text,
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
                        name =  it.firstName,
                        imageUrl = it.imageURL,
                        isWinner = true
                    )
                },
                participants = participants.map {
                    GameParticipantUiModel(
                        id = it.id,
                        name =  it.firstName,
                        imageUrl = it.imageURL,
                        isWinner = false
                    )
                },
            )
        }
    }

    override fun mapLeaderBoardWithSlot(
        response: GetSellerLeaderboardSlotResponse,
        allowChat: Boolean
    ): List<LeaderboardGameUiModel> {
        return emptyList()
    }

    /***
     * Change to typename to make sure
     */
    private fun getLeaderboardType(leaderboardsResponse: String): LeadeboardType {
        return when(leaderboardsResponse){
            "PlayInteractiveSellerLeaderboardGiveaway" -> LeadeboardType.Giveaway
            "PlayInteractiveSellerLeaderboardQuiz" -> LeadeboardType.Quiz
            else -> LeadeboardType.Unknown
        }
    }

    private fun generateAlphabetChoices(index: Int): Char = arrayOfChoices[index]
    private val arrayOfChoices = ('A'..'D').toList()

    private fun GetSellerLeaderboardSlotResponse.SlotData.getSlotTitle() : String {
        return if (getLeaderboardType(this.type) == LeadeboardType.Giveaway)
            this.title
        else
            this.question
    }
    override fun mapBroadcasterMetric(
        metric: BroadcasterMetric,
        authorId: String,
        channelId: String
    ) = PlayBroadcasterMetric(
        authorId = authorId,
        channelId = channelId,
        videoBitrate = 0,
        audioBitrate = 0,
        resolution = "",
        traffic = 0,
        bandwidth = 0,
        fps = 0.0,
        packetLossIncreased = false,
        videoBufferTimestamp = 0,
        audioBufferTimestamp = 0,
    )

    override fun mapAuthorList(response: GetCheckWhitelistResponse): List<ContentAccountUiModel> {
        return emptyList()
    }

    companion object {
        const val LOCAL_RTMP_URL: String = "rtmp://192.168.0.110:1935/stream/"
        private const val FORMAT_FIRST_NAME = "{{first_name}}"
        private const val FORMAT_TITLE = "{{title}}"
    }
}
