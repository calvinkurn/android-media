package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.kotlin.extensions.toFormattedString
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
import com.tokopedia.play.broadcaster.type.*
import com.tokopedia.play.broadcaster.ui.model.*
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
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
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
                if (startIndex >= 0) setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    lastIndex,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        )
    }

    override fun mapLiveFollowers(
        response: GetLiveFollowersResponse
    ): FollowerDataUiModel {
        val totalRetrievedFollowers = response.shopFollowerList.data.size
        return FollowerDataUiModel(
            followersList = List(TOTAL_FOLLOWERS) {
                if (it >= totalRetrievedFollowers) FollowerUiModel.Unknown.fromIndex(it)
                else FollowerUiModel.User(response.shopFollowerList.data[it].photo)
            },
            totalFollowers = response.shopInfoById.result.firstOrNull()?.favoriteData?.totalFavorite
                ?: 0
        )
    }

    override fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia) =
            LiveStreamInfoUiModel(
                    ingestUrl = media.ingestUrl,

        )

    override fun mapToLiveTrafficUiMetrics(metrics: LiveStats): List<TrafficMetricUiModel> =
        mutableListOf(
            TrafficMetricUiModel(TrafficMetricType.TotalViews, metrics.visitChannelFmt),
            TrafficMetricUiModel(TrafficMetricType.VideoLikes, metrics.likeChannelFmt),
            TrafficMetricUiModel(TrafficMetricType.NewFollowers, metrics.followShopFmt),
            TrafficMetricUiModel(TrafficMetricType.ShopVisit, metrics.visitShopFmt),
            TrafficMetricUiModel(TrafficMetricType.ProductVisit, metrics.visitPdpFmt),
            TrafficMetricUiModel(TrafficMetricType.NumberOfAtc, metrics.addToCartFmt),
            TrafficMetricUiModel(TrafficMetricType.NumberOfPaidOrders, metrics.paymentVerifiedFmt)
        )

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

    override fun mapProductTag(productTag: ProductTagging): List<ProductData> =
        productTag.productList.map {
            ProductData(
                id = it.id.toString(),
                name = it.name,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock,
                price = if (it.discount != 0) {
                    DiscountedPrice(
                        originalPrice = it.originalPriceFormatted,
                        originalPriceNumber = it.originalPrice,
                        discountedPrice = it.priceFormatted,
                        discountedPriceNumber = it.price,
                        discountPercent = it.discount
                    )
                } else {
                    OriginalPrice(
                        price = it.originalPriceFormatted,
                        priceNumber = it.originalPrice
                    )
                }
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

    override fun mapChannelProductTags(productTags: List<GetChannelResponse.ProductTag>) =
        productTags.map {
            ProductData(
                id = it.productID,
                name = it.productName,
                imageUrl = it.imageUrl,
                originalImageUrl = it.imageUrl,
                stock = if (it.isAvailable) StockAvailable(it.quantity) else OutOfStock,
                price = if (it.discount.toInt() != 0) {
                    DiscountedPrice(
                        originalPrice = it.originalPriceFmt,
                        originalPriceNumber = it.originalPrice.toDouble(),
                        discountedPrice = it.priceFmt,
                        discountedPriceNumber = it.price.toDouble(),
                        discountPercent = it.discount.toInt()
                    )
                } else {
                    OriginalPrice(
                        price = it.originalPriceFmt,
                        priceNumber = it.originalPrice.toDouble()
                    )
                }
            )
        }

    override fun mapChannelSchedule(timestamp: GetChannelResponse.Timestamp): BroadcastScheduleUiModel {
        return if (timestamp.publishedAt.isBlank()) BroadcastScheduleUiModel.NoSchedule
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
        isEligiblePostVideo: Boolean
    ) = ChannelSummaryUiModel(
        title = title,
        coverUrl = coverUrl,
        date = date,
        duration = duration,
        isEligiblePostVideo = isEligiblePostVideo,
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

    override fun mapInteractiveConfig(response: GetInteractiveConfigResponse): InteractiveConfigUiModel {
        val interactiveDuration = response.interactiveConfig.tapTapConfig.interactiveDuration

        val quizDurationInMs = response.interactiveConfig.quizConfig.quizDurationsInSeconds.map {
            TimeUnit.SECONDS.toMillis(it.toLong())
        }

        return InteractiveConfigUiModel(
            giveawayConfig = GiveawayConfigUiModel(
                isActive = response.interactiveConfig.tapTapConfig.isActive,
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
                isActive = response.interactiveConfig.quizConfig.isActive,
                maxTitleLength = response.interactiveConfig.quizConfig.maxTitleLength,
                maxChoicesCount = response.interactiveConfig.quizConfig.maxChoicesCount,
                minChoicesCount = response.interactiveConfig.quizConfig.minChoicesCount,
                maxRewardLength = response.interactiveConfig.quizConfig.maxRewardLength,
                maxChoiceLength = response.interactiveConfig.quizConfig.maxChoiceLength,
                availableStartTimeInMs = quizDurationInMs,
                eligibleStartTimeInMs = quizDurationInMs,
                showPrizeCoachMark = true,
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

    override fun mapQuizDetail(response: GetInteractiveQuizDetailResponse, interactiveId: String): QuizDetailDataUiModel {
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

    override fun mapQuizDetailToLeaderBoard(dataUiModel: QuizDetailDataUiModel): PlayLeaderboardUiModel {
        return PlayLeaderboardUiModel(
            title = textTransformer.transform(dataUiModel.question),
            reward = textTransformer.transform(dataUiModel.reward),
            choices = dataUiModel.choices.mapIndexed { index, choice ->
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
            },
            endsIn = dataUiModel.countDownEnd,
            otherParticipant = 0,
            otherParticipantText = "",
            winners = emptyList(),
            leaderBoardType = LeadeboardType.Quiz,
            id = dataUiModel.interactiveId,
        )
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

    override fun mapLeaderBoardWithSlot(
        response: GetSellerLeaderboardSlotResponse,
        allowChat: Boolean
    ): List<PlayLeaderboardUiModel> {
        return response.data.slots.map { slot ->
            PlayLeaderboardUiModel(
                title = slot.getSlotTitle(),
                winners = slot.winner.mapIndexed { index, winner ->
                    PlayWinnerUiModel(
                        rank = index + 1,
                        id = winner.userID,
                        name = winner.userName,
                        imageUrl = winner.imageUrl,
                        allowChat = { allowChat },
                        topChatMessage =
                        if (getLeaderboardType(slot.type) == LeadeboardType.Giveaway)
                            response.data.config.topchatMessage
                                .replace(FORMAT_FIRST_NAME, winner.userName)
                                .replace(FORMAT_TITLE, slot.getSlotTitle())
                        else
                            response.data.config.topchatMessageQuiz
                                .replace(FORMAT_FIRST_NAME, winner.userName)
                                .replace(FORMAT_TITLE, slot.getSlotTitle())
                        ,
                    )
                },
                choices = slot.choices.mapIndexed { index, choice ->
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
                        interactiveId = slot.interactiveId,
                        interactiveTitle = slot.getSlotTitle()
                    )
                },
                otherParticipantText = slot.otherParticipantCountText,
                otherParticipant = slot.otherParticipantCount.toLong(),
                reward = textTransformer.transform(slot.reward),
                leaderBoardType = getLeaderboardType(slot.type),
                id = slot.interactiveId,
            )
        }
    }

    private fun GetSellerLeaderboardSlotResponse.SlotData.getSlotTitle() : String {
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

    companion object {
        private const val FORMAT_INTERACTIVE_DURATION = "${'$'}{second}"
        private const val FORMAT_FIRST_NAME = "{{first_name}}"
        private const val FORMAT_TITLE = "{{title}}"
        private const val TOTAL_FOLLOWERS = 3
    }

}