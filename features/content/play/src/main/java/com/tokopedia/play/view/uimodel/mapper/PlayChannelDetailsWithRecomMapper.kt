package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.BannedUiModel
import com.tokopedia.play.view.uimodel.recom.CategoryWidgetConfig
import com.tokopedia.play.view.uimodel.recom.ExploreWidgetConfig
import com.tokopedia.play.view.uimodel.recom.FreezeUiModel
import com.tokopedia.play.view.uimodel.recom.LikeSource
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.view.uimodel.recom.PlayChannelReportUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayEmptyBottomSheetInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPopUpConfigUiModel
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusConfig
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoConfigUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.realtimenotif.PlayRealTimeNotificationConfig
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.ArchivedUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 21/01/21
 */
@PlayScope
class PlayChannelDetailsWithRecomMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    private val htmlTextTransformer: HtmlTextTransformer,
    private val realTimeNotificationMapper: PlayRealTimeNotificationMapper,
    private val multipleLikesMapper: PlayMultipleLikesMapper,
) {

    fun map(input: ChannelDetailsWithRecomResponse, extraParams: ExtraParams): List<PlayChannelData> {
        return input.channelDetails.dataList.map {
            val partnerInfo = mapPartnerInfo(it.partner, it.config.hasFollowButton)
            val channelType = getChannelType(it.isLive, it.airTime)

            PlayChannelData(
                id = it.id,
                channelDetail = PlayChannelDetailUiModel(
                    channelInfo = mapChannelInfo(it),
                    shareInfo = mapShareInfo(it.share),
                    rtnConfigInfo = mapRealTimeNotificationConfig(
                        it.config.welcomeFormat,
                        it.config.realTimeNotif
                    ),
                    videoInfo = mapVideoInfo(it.video),
                    emptyBottomSheetInfo = mapEmptyBottomSheet(it),
                    popupConfig = mapPopUp(it),
                    channelRecomConfig = mapChannelRecomConfig(it.config.categoryWidgetConfig, it.config.exploreWidgetConfig),
                    showCart = it.config.showCart,
                ),
                partnerInfo = partnerInfo,
                likeInfo = mapLikeInfo(it.config.feedLikeParam, it.config.multipleLikeConfig),
                channelReportInfo = mapChannelReportInfo(it.id, partnerInfo, channelType, it.performanceSummaryPageLink, extraParams),
                pinnedInfo = mapPinnedInfo(it.pinnedMessage),
                quickReplyInfo = mapQuickReply(it.quickReplies),
                videoMetaInfo = if(it.airTime == PlayUpcomingUiModel.COMING_SOON) emptyVideoMetaInfo() else mapVideoMeta(it.video, it.id, it.title, extraParams),
                leaderboard = mapLeaderboardInfo(),
                upcomingInfo = mapUpcoming(it.title, it.airTime, it.config.reminder.isSet, it.coverUrl, it.startTime, it.description),
                tagItems = mapTagItems(it.config),
                status = mapStatus(it.config, it.title),
            )
        }
    }

    private fun mapChannelInfo(data: ChannelDetailsWithRecomResponse.Data) = PlayChannelInfoUiModel(
            id = data.id,
            channelType = getChannelType(data.isLive, data.airTime),
            backgroundUrl = data.config.roomBackground.imageUrl,
            title = data.title,
            coverUrl = data.coverUrl,
            startTime = data.startTime
    )

    private fun getChannelType(isLive: Boolean, airTime: String): PlayChannelType {
        return if (isLive) PlayChannelType.Live
        else if(airTime == PlayUpcomingUiModel.COMING_SOON) PlayChannelType.Upcoming
        else PlayChannelType.VOD
    }

    private fun mapPartnerInfo(partnerResponse: ChannelDetailsWithRecomResponse.Partner, isFollowBtnShown: Boolean) = PlayPartnerInfo(
        id = partnerResponse.id.toLongOrZero(),
        name = htmlTextTransformer.transform(partnerResponse.name),
        type = PartnerType.getTypeByValue(partnerResponse.type),
        status = getFollowStatus(isFollowBtnShown),
        iconUrl = partnerResponse.thumbnailUrl,
        badgeUrl = partnerResponse.badgeUrl,
        appLink = partnerResponse.appLink
    )

    private fun getFollowStatus(isFollowBtnShown: Boolean) = if (isFollowBtnShown) PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.Unknown) else PlayPartnerFollowStatus.NotFollowable

    private fun mapLikeInfo(
        feedLikeParamResponse: ChannelDetailsWithRecomResponse.FeedLikeParam,
        configs: List<MultipleLikeConfig>,
    ) = PlayLikeInfoUiModel(
        contentId = feedLikeParamResponse.contentId,
        contentType = feedLikeParamResponse.contentType,
        likeType = feedLikeParamResponse.likeType,
        status = PlayLikeStatus.Unknown,
        source = LikeSource.Network,
        likeBubbleConfig = mapMultipleLikeConfig(configs),
    )

    private fun mapChannelReportInfo(
        channelId: String,
        partnerInfo: PlayPartnerInfo,
        channelType: PlayChannelType,
        performanceSummaryPageLink: String,
        extraParams: ExtraParams
    ) = PlayChannelReportUiModel(
        shouldTrack = if(channelId == extraParams.channelId) extraParams.shouldTrack else true,
        sourceType = extraParams.sourceType,
        performanceSummaryPageLink = mapPerformanceSummaryPageLink(partnerInfo, channelType, performanceSummaryPageLink),
    )

    private fun mapShareInfo(shareResponse: ChannelDetailsWithRecomResponse.Share): PlayShareInfoUiModel {
        val fullShareContent = try {
            shareResponse.text.replace("${'$'}{url}", shareResponse.redirectUrl)
        } catch (e: Throwable) {
            "${shareResponse.text}\n${shareResponse.redirectUrl}"
        }

        return PlayShareInfoUiModel(
                content = htmlTextTransformer.transform(fullShareContent),
                shouldShow = shareResponse.isShowButton
                        && shareResponse.redirectUrl.isNotBlank(),
                textDescription = shareResponse.text,
                redirectUrl = shareResponse.redirectUrl,
                metaTitle = shareResponse.metaTitle,
                metaDescription = shareResponse.metaDescription,
        )
    }

    private fun mapRealTimeNotificationConfig(
            welcomeFormatResponse: RealTimeNotification,
            config: ChannelDetailsWithRecomResponse.RealTimeNotificationConfig
    ) = PlayRealTimeNotificationConfig(
            welcomeNotification = realTimeNotificationMapper.mapWelcomeFormat(
                    welcomeFormatResponse
            ),
            lifespan = if (config.lifespan <= 0) DEFAULT_LIFESPAN_IN_MS else config.lifespan,
    )

    private fun mapVideoInfo(
        videoResponse: ChannelDetailsWithRecomResponse.Video
    ) = PlayVideoConfigUiModel(
        id = videoResponse.id,
        orientation = VideoOrientation.getByValue(videoResponse.orientation),
    )

    private fun mapMultipleLikeConfig(
        configs: List<MultipleLikeConfig>
    ) : PlayLikeBubbleConfig {
        return multipleLikesMapper.mapMultipleLikeConfig(configs)
    }

    private fun mapPinnedInfo(
            pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage,
    ) = PlayPinnedInfoUiModel(
            pinnedMessage = mapPinnedMessage(pinnedMessageResponse),
    )

    private fun mapPinnedMessage(pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage) = PinnedMessageUiModel(
            id = pinnedMessageResponse.id,
            appLink = pinnedMessageResponse.redirectUrl,
            title = pinnedMessageResponse.title,
    )

    private fun mapTagItems(configResponse: ChannelDetailsWithRecomResponse.Config) = TagItemUiModel(
        product = mapProduct(configResponse),
        voucher = VoucherUiModel.Empty,
        maxFeatured = 0,
        resultState = ResultState.Loading,
        bottomSheetTitle = "",
    )

    private fun mapProduct(configResponse: ChannelDetailsWithRecomResponse.Config) = ProductUiModel.Empty.copy(
        canShow = configResponse.showPinnedProduct
    )

    private fun mapQuickReply(quickRepliesResponse: List<String>) = PlayQuickReplyInfoUiModel(
        quickReplyList = quickRepliesResponse.filterNot { quickReply -> quickReply.isEmpty() || quickReply.isBlank() }
    )

    private fun mapVideoMeta(
            videoResponse: ChannelDetailsWithRecomResponse.Video,
            channelId: String,
            title: String,
            extraParams: ExtraParams
    ) = PlayVideoMetaInfoUiModel(
            videoPlayer = mapVideoPlayer(videoResponse, channelId, extraParams),
            videoStream = mapVideoStream(videoResponse, title)
    )

    private fun mapVideoPlayer(videoResponse: ChannelDetailsWithRecomResponse.Video, channelId: String, extraParams: ExtraParams) = when (videoResponse.type) {
        "live", "vod" -> PlayVideoPlayerUiModel.General.Incomplete(
            params = PlayGeneralVideoPlayerParams(
                videoUrl = videoResponse.streamSource,
                buffer = mapVideoBufferControl(videoResponse.bufferControl),
                lastMillis = if (channelId == extraParams.channelId && videoResponse.type == "vod") extraParams.videoStartMillis else null
            )
        )
        "youtube" -> PlayVideoPlayerUiModel.YouTube(videoResponse.streamSource)
        else -> PlayVideoPlayerUiModel.Unknown
    }

    private fun mapVideoStream(
            videoResponse: ChannelDetailsWithRecomResponse.Video,
            title: String
    ) = PlayVideoStreamUiModel(
            id = videoResponse.id,
            orientation = VideoOrientation.getByValue(videoResponse.orientation),
            title = title
    )

    private fun mapBannedModel(
            bannedDataResponse: ChannelDetailsWithRecomResponse.BannedData
    ) = BannedUiModel(
            title = bannedDataResponse.title,
            message = bannedDataResponse.message,
            btnTitle = bannedDataResponse.buttonText
    )

    private fun mapVideoBufferControl(bufferControl: ChannelDetailsWithRecomResponse.BufferControl?): PlayBufferControl {
        return if (bufferControl != null) {
            PlayBufferControl(
                    minBufferMs = bufferControl.minBufferingSecond * MS_PER_SECOND,
                    maxBufferMs = bufferControl.maxBufferingSecond * MS_PER_SECOND,
                    bufferForPlaybackMs = bufferControl.bufferForPlayback * MS_PER_SECOND,
                    bufferForPlaybackAfterRebufferMs = bufferControl.bufferForPlaybackAfterRebuffer * MS_PER_SECOND
            )
        } else PlayBufferControl()
    }

    private fun mapStatusType(isFreezed: Boolean): PlayStatusType {
        return if (isFreezed) PlayStatusType.Freeze
        else PlayStatusType.Active
    }

    private fun mapLeaderboardInfo() = LeaderboardUiModel.Empty

    private fun mapUpcoming(title: String, airTime: String, isReminderSet: Boolean, coverUrl: String, startTime: String, description: String) =
        PlayUpcomingUiModel(
            title = title,
            isUpcoming = airTime == PlayUpcomingUiModel.COMING_SOON,
            isReminderSet = isReminderSet,
            coverUrl = coverUrl,
            startTime = startTime,
            isAlreadyLive = false,
            description = description,
        )

    private fun emptyVideoMetaInfo() = PlayVideoMetaInfoUiModel(
        videoPlayer = PlayVideoPlayerUiModel.Unknown,
        videoStream = PlayVideoStreamUiModel(
            id = "",
            orientation = VideoOrientation.Unknown,
            title = ""
        )
    )

    private fun mapStatus(
        configResponse: ChannelDetailsWithRecomResponse.Config,
        title: String
    ): PlayStatusUiModel {
        val statusType = PlayStatusType.getByValue(
            configResponse.status
        )
        return PlayStatusUiModel(
            channelStatus = PlayChannelStatus(
                statusType = statusType,
                statusSource = PlayStatusSource.Network,
                waitingDuration = 0,
            ),
            config = PlayStatusConfig(
                bannedModel = mapBannedModel(configResponse.bannedData),
                freezeModel = mapFreezeUiModel(configResponse.freezeData, title),
                archivedModel = mapArchived(configResponse.archiveConfig),
            ),
        )
    }

    private fun mapFreezeUiModel(
        freezeDataResponse: ChannelDetailsWithRecomResponse.FreezeData,
        title: String
    ) = FreezeUiModel(
        title = String.format(freezeDataResponse.title, title),
    )

    private fun mapEmptyBottomSheet(data: ChannelDetailsWithRecomResponse.Data) = with(data.config.emptyBottomSheet){
        PlayEmptyBottomSheetInfoUiModel(header = headerText, body = bodyText, button = redirectButtonText, partnerAppLink = data.partner.appLink, imageUrl = imageUrl)
    }

    private fun mapExploreWidgetConfig(
        config: ChannelDetailsWithRecomResponse.ExploreWidgetConfig,
    ) = ExploreWidgetConfig(
       group = config.group,
        sourceType = config.sourceType,
        sourceId = config.sourceId,
    )

    private fun mapCategoryWidgetConfig(
        categoryConfig: ChannelDetailsWithRecomResponse.ExploreWidgetConfig
    ) = CategoryWidgetConfig(
        categoryGroup = categoryConfig.group,
        hasCategory = categoryConfig.hasCategory,
        categoryName = categoryConfig.categoryName.ifBlank { "Eksplor" },
        categorySourceType = categoryConfig.sourceType,
        categorySourceId = categoryConfig.sourceId,
        categoryLevel = categoryConfig.categoryLvl,
        categoryId = categoryConfig.categoryId,
    )

    private fun mapChannelRecomConfig(
        categoryConfig: ChannelDetailsWithRecomResponse.ExploreWidgetConfig,
        exploreConfig: ChannelDetailsWithRecomResponse.ExploreWidgetConfig,
    ) = PlayChannelRecommendationConfig(
        categoryWidgetConfig = mapCategoryWidgetConfig(categoryConfig),
        exploreWidgetConfig = mapExploreWidgetConfig(exploreConfig)
    )

    private fun mapArchived(archiveData: ChannelDetailsWithRecomResponse.ArchivedData) = with(archiveData) {
        ArchivedUiModel(
            title = title,
            description = description,
            btnTitle = buttonText,
            appLink = appLink,
        )
    }

    private fun mapPopUp(data: ChannelDetailsWithRecomResponse.Data) = with(data.config.popupConfig){
        PlayPopUpConfigUiModel(isEnabled = isEnabled, text = copyText, duration = TimeUnit.SECONDS.toMillis(duration))
    }

    private fun mapPerformanceSummaryPageLink(
        partnerInfo: PlayPartnerInfo,
        channelType: PlayChannelType,
        performanceSummaryPageLink: String
    ): String {
        return if(partnerInfo.type == PartnerType.Buyer &&
            partnerInfo.id.toString() == userSession.userId &&
            channelType == PlayChannelType.VOD
        ) performanceSummaryPageLink
        else ""
    }

    companion object {
        private const val MS_PER_SECOND = 1000
        private const val DEFAULT_MAX_FEATURED_PRODUCT = 5

        private const val DEFAULT_LIFESPAN_IN_MS = 1000L
    }

    data class ExtraParams(
            val channelId: String?,
            val videoStartMillis: Long?,
            val shouldTrack: Boolean,
            val sourceType: String,
    )
}
