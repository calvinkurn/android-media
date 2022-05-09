package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.broadcaster.mediator.LivePusherConfig
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.interactive.GetInteractiveConfigResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizDetailResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.type.PriceUnknown
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.*
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.util.*
import kotlin.random.Random

/**
 * Created by jegul on 21/09/20
 */
class PlayBroadcastMockMapper : PlayBroadcastMapper {

    override fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel> {
        return List(6) {
            EtalaseContentUiModel(
                    id = (it + 1L).toString(),
                    name = "Etalase ${it + 1}",
                    productMap = mutableMapOf(),
                    totalProduct = (it + 1) * 100,
                    stillHasProduct = false
            )
        }
    }

    override fun mapProductList(
        productsResponse: GetProductsByEtalaseResponse.GetProductListData,
        isSelectedHandler: (String) -> Boolean,
        isSelectableHandler: (Boolean) -> SelectableState
    ): List<ProductContentUiModel> {
        return List(6) {
            ProductContentUiModel(
                    id = (12345L + it).toString(),
                    name = "Product ${it + 1}",
                    imageUrl = when (it) {
                        1 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/oyhemtbkghuegy9gpo0i/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        2 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        3 -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/rofxpoxehp6wznvzb1jk/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        else -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/udglgfg9ozu3erd3fubg/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    },
                    originalImageUrl = when (it) {
                        1 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/oyhemtbkghuegy9gpo0i/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        2 -> "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        3 -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/rofxpoxehp6wznvzb1jk/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                        else -> "https://static.nike.com/a/images/t_PDP_864_v1/f_auto,b_rgb:f5f5f5,q_80/udglgfg9ozu3erd3fubg/joyride-run-flyknit-running-shoe-sqfqGQ.jpg"
                    },
                    isSelectedHandler = { false },
                    stock = StockAvailable((it % 2) * 10),
                    isSelectable = { Selectable },
                    price = PriceUnknown
            )
        }
    }

    override fun mapSearchSuggestionList(keyword: String, productsResponse: GetProductsByEtalaseResponse.GetProductListData): List<SearchSuggestionUiModel> {
        return List(keyword.length) {
            val suggestionText = " ${keyword.substring(0, it + 1)}"
            val fullText = "$keyword$suggestionText"
            SearchSuggestionUiModel(
                    queriedText = keyword,
                    suggestedId = "1",
                    suggestedText = fullText,
                    spannedSuggestion = SpannableStringBuilder(fullText).apply {
                        setSpan(StyleSpan(Typeface.BOLD), fullText.indexOf(suggestionText), fullText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    }
            )
        }
    }

    override fun mapLiveFollowers(response: GetLiveFollowersResponse): FollowerDataUiModel {
        return FollowerDataUiModel(
                List(3) {
                    FollowerUiModel.Unknown(when (it) {
                        0 -> com.tokopedia.unifyprinciples.R.color.Unify_Y500
                        1 -> com.tokopedia.unifyprinciples.R.color.Unify_B600
                        else -> com.tokopedia.unifyprinciples.R.color.Unify_Y300
                    })
                },
                3
        )
    }

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia): LiveStreamInfoUiModel {
        return LiveStreamInfoUiModel(
                "1234",
                ingestUrl = LOCAL_RTMP_URL,
                streamUrl = "rtmp://test"
        )
    }

    override fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel> {
        return listOf(
                TrafficMetricUiModel(TrafficMetricType.GameParticipants, "2000"),
                TrafficMetricUiModel(TrafficMetricType.TotalViews, "2328"),
                TrafficMetricUiModel(TrafficMetricType.VideoLikes, "1800"),
                TrafficMetricUiModel(TrafficMetricType.ShopVisit, "1200"),
                TrafficMetricUiModel(TrafficMetricType.ProductVisit, "1042"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, "320"),
                TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, "200")
        )
    }

    override fun mapTotalView(totalView: TotalView): TotalViewUiModel {
        return TotalViewUiModel(totalView = "1234")
    }

    override fun mapTotalLike(totalLike: TotalLike): TotalLikeUiModel {
        return TotalLikeUiModel(totalLike = "1234")
    }

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

    override fun mapProductTag(productTag: ProductTagging): List<ProductData> {
        return emptyList()
    }

    override fun mapConfiguration(config: Config): ConfigurationUiModel {
        return ConfigurationUiModel(
            streamAllowed = true,
            channelType = ChannelType.Draft,
            channelId = "10008", // 10008 prod, 10012 stag (status: draft)
            remainingTime = (30 * 60 * 1000),
            durationConfig = DurationConfigUiModel(
                duration = (30 * 60 * 1000),
                maxDurationDesc = "Siaran 30 menit",
                pauseDuration = (1 * 60 * 1000),
                errorMessage = "Maks. siaran 30 menit"
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
                coverUrl = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
                ingestUrl = LOCAL_RTMP_URL,
                status = PlayChannelStatusType.Draft
        )
    }

    override fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>): List<ProductData> {
        return emptyList()
    }

    override fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel {
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
                imageUrl = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja1000.jpg",
                redirectUrl = "https://beta.tokopedia.com/play/channel/10140",
                textContent =  "\"testing\"\nYuk, nonton siaran dari MRG Audio di Tokopedia PLAY! Bakal seru banget lho!\n${'$'}{url}",
                shortenUrl = true
        )
    }

    override fun mapChannelSummary(title: String, coverUrl: String, date: String, duration: String, isEligiblePostVideo: Boolean): ChannelSummaryUiModel {
        return ChannelSummaryUiModel(title, coverUrl, date, duration, isEligiblePostVideo)
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

    override fun mapInteractiveConfig(response: GetInteractiveConfigResponse) = InteractiveConfigUiModel(
        giveawayConfig = GiveawayConfigUiModel(
            isActive = true,
            nameGuidelineHeader = "Mau kasih hadiah apa?",
            nameGuidelineDetail = "Contoh: Giveaway Sepatu, Tas Rp50 rb, Diskon 90%, Kupon Ongkir, HP Gratis, dll.",
            timeGuidelineHeader = "Kapan game-nya mulai?",
            timeGuidelineDetail = "Tentukan kapan game dimulai, dan game akan berlangsung selama 10 detik.",
            durationInMs = 10000L,
            availableStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
        ),
        quizConfig = QuizConfigUiModel(
            isActive = true,
            maxTitleLength = 30,
            maxChoicesCount = 3,
            minChoicesCount = 2,
            maxRewardLength = 30,
            maxChoiceLength = 35,
            availableStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
            eligibleStartTimeInMs = listOf(3 * 60 * 1000L, 5 * 60 * 1000L, 10 * 60 * 1000L).sorted(),
            showPrizeCoachMark = true,
        ),
    )

    override fun mapInteractiveSession(response: PostInteractiveCreateSessionResponse,
                                       title: String,
                                       durationInMs: Long): InteractiveSessionUiModel {
        return InteractiveSessionUiModel(
            "1",
            "Giveaway Tesla",
            60 * 1000L
        )
    }

    override fun mapLiveInfo(
        activeIngestUrl: String,
        config: LivePusherConfig
    ): PlayLiveLogState {
        return PlayLiveLogState.Init(
            "rtmp://tkpd.com",
            config.videoWidth,
            config.videoHeight,
            config.fps,
            config.videoBitrate
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

    override fun mapQuizDetail(response: GetInteractiveQuizDetailResponse): QuizDetailDataUiModel {
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
                }
            )
        }
    }

    override fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel): PlayLeaderboardUiModel {
        return PlayLeaderboardUiModel(
            title = dataUiModel.question,
            reward = dataUiModel.reward,
            choices = dataUiModel.choices.mapIndexed { index, choice ->
                QuizChoicesUiModel(
                    id = choice.id,
                    text = choice.text,
                    type = PlayQuizOptionState.Participant(
                        alphabet = generateAlphabet(index),
                        isCorrect = choice.isCorrectAnswer,
                        count = choice.participantCount
                    )
                )
            },
            endsIn = dataUiModel.countDownEnd,
            otherParticipant = 0,
            otherParticipantText = "",
            winners = emptyList(),
            leaderBoardType = LeadeboardType.Quiz
        )
    }

    private fun generateAlphabet(index: Int) : Char = arrayOfAlphabet[index]
    private val arrayOfAlphabet = ('A'..'Z').toMutableList()

    companion object {
        const val LOCAL_RTMP_URL: String = "rtmp://192.168.0.110:1935/stream/"
    }
}