package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import javax.inject.Inject

/**
 * Created by jegul on 21/01/21
 */
@PlayScope
class PlayChannelDetailsWithRecomMapper @Inject constructor(
        private val htmlTextTransformer: HtmlTextTransformer
) {

    fun map(input: ChannelDetailsWithRecomResponse, extraParams: ExtraParams): List<PlayChannelData> {
        return input.channelDetails.dataList.map {
            PlayChannelData(
                    id = it.id,
                    channelInfo = mapChannelInfo(it.isLive, it.config),
                    partnerInfo = mapPartnerInfo(it.partner),
                    likeInfo = mapLikeInfo(it.config.feedLikeParam),
                    totalViewInfo = mapTotalViewInfo(),
                    shareInfo = mapShareInfo(it.share, it.config.active, it.config.freezed),
                    cartInfo = mapCartInfo(it.config),
                    pinnedInfo = mapPinnedInfo(it.pinnedMessage, it.partner, it.config),
                    quickReplyInfo = mapQuickReply(it.quickReplies),
                    videoMetaInfo = mapVideoMeta(it.video, it.id, extraParams),
                    statusInfo = mapChannelStatusInfo(it.config, it.title)
            )
        }
    }

    private fun mapChannelInfo(
            isLive: Boolean,
            configResponse: ChannelDetailsWithRecomResponse.Config,
    ) = PlayChannelInfoUiModel(
            channelType = if (isLive) PlayChannelType.Live else PlayChannelType.VOD,
            backgroundUrl = configResponse.roomBackground.imageUrl
    )

    private fun mapPartnerInfo(partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPartnerInfoUiModel.Incomplete(
            basicInfo = PlayPartnerBasicInfoUiModel(
                    id = partnerResponse.id.toLongOrZero(),
                    name = htmlTextTransformer.transform(partnerResponse.name),
                    type = PartnerType.getTypeByValue(partnerResponse.type),
            )
    )

    private fun mapLikeInfo(feedLikeParamResponse: ChannelDetailsWithRecomResponse.FeedLikeParam) = PlayLikeInfoUiModel.Incomplete(
            param = PlayLikeParamInfoUiModel(
                    contentId = feedLikeParamResponse.contentId,
                    contentType = feedLikeParamResponse.contentType,
                    likeType = feedLikeParamResponse.likeType
            )
    )

    private fun mapTotalViewInfo() = PlayTotalViewUiModel.Incomplete

    private fun mapShareInfo(shareResponse: ChannelDetailsWithRecomResponse.Share, isActive: Boolean, isFreezed: Boolean): PlayShareInfoUiModel {
        val fullShareContent = try {
            shareResponse.text.replace("${'$'}{url}", shareResponse.redirectUrl)
        } catch (e: Throwable) {
            "${shareResponse.text}/n${shareResponse.redirectUrl}"
        }

        return PlayShareInfoUiModel(
                content = htmlTextTransformer.transform(fullShareContent),
                shouldShow = shareResponse.isShowButton
                        && shareResponse.redirectUrl.isNotBlank()
                        && isActive
                        && !isFreezed
        )
    }

    private fun mapCartInfo(configResponse: ChannelDetailsWithRecomResponse.Config) = PlayCartInfoUiModel.Incomplete(
            shouldShow = configResponse.showCart
    )

    private fun mapPinnedInfo(
            pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage,
            partnerResponse: ChannelDetailsWithRecomResponse.Partner,
            configResponse: ChannelDetailsWithRecomResponse.Config
    ) = PlayPinnedInfoUiModel(
            pinnedMessage = mapPinnedMessage(pinnedMessageResponse, partnerResponse),
            pinnedProduct = mapPinnedProduct(configResponse, partnerResponse)
    )

    private fun mapProductTagsInfo(
            partnerResponse: ChannelDetailsWithRecomResponse.Partner,
            configResponse: ChannelDetailsWithRecomResponse.Config,
    ) = PlayProductTagsUiModel.Incomplete(
            basicInfo = PlayProductTagsBasicInfoUiModel(
                    bottomSheetTitle = configResponse.pinnedProductConfig.bottomSheetTitle,
                    partnerId = partnerResponse.id.toLongOrZero(),
                    maxFeaturedProducts = DEFAULT_MAX_FEATURED_PRODUCT
            )
    )

    private fun mapPinnedMessage(pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage, partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPinnedUiModel.PinnedMessage(
            id = pinnedMessageResponse.id,
            applink = pinnedMessageResponse.redirectUrl,
            partnerName = htmlTextTransformer.transform(partnerResponse.name),
            title = pinnedMessageResponse.title,
    )

    private fun mapPinnedProduct(configResponse: ChannelDetailsWithRecomResponse.Config, partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPinnedUiModel.PinnedProduct(
            partnerName = htmlTextTransformer.transform(partnerResponse.name),
            title = configResponse.pinnedProductConfig.pinTitle,
            hasPromo = configResponse.hasPromo,
            shouldShow = configResponse.showPinnedProduct,
            productTags = mapProductTagsInfo(partnerResponse, configResponse)
    )

    private fun mapQuickReply(quickRepliesResponse: List<String>) = PlayQuickReplyInfoUiModel(
            quickReplyList = quickRepliesResponse.filterNot { quickReply -> quickReply.isEmpty() || quickReply.isBlank() }
    )

    private fun mapVideoMeta(
            videoResponse: ChannelDetailsWithRecomResponse.Video,
            channelId: String,
            extraParams: ExtraParams
    ) = PlayVideoMetaInfoUiModel(
            videoPlayer = mapVideoPlayer(videoResponse, channelId, extraParams),
            videoStream = mapVideoStream(videoResponse)
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
    ) = PlayVideoStreamUiModel(
            id = videoResponse.id,
            orientation = VideoOrientation.getByValue(videoResponse.orientation),
    )

    private fun mapChannelStatusInfo(
            configResponse: ChannelDetailsWithRecomResponse.Config,
            title: String
    ) = PlayStatusInfoUiModel(
            statusType = mapStatusType(!configResponse.active || configResponse.freezed),
            bannedModel = mapBannedModel(configResponse.bannedData),
            freezeModel = mapFreezeModel(configResponse.freezeData, title),
            shouldAutoSwipeOnFreeze = true
    )

    private fun mapBannedModel(
            bannedDataResponse: ChannelDetailsWithRecomResponse.BannedData
    ) = PlayBannedUiModel(
            title = bannedDataResponse.title,
            message = bannedDataResponse.message,
            btnTitle = bannedDataResponse.buttonText
    )

    private fun mapFreezeModel(
            freezeDataResponse: ChannelDetailsWithRecomResponse.FreezeData,
            title: String
    ) = PlayFreezeUiModel(
            title = String.format(freezeDataResponse.title, title),
            message = freezeDataResponse.desc,
            btnTitle = freezeDataResponse.buttonText,
            btnUrl = freezeDataResponse.buttonAppLink
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

    companion object {
        private const val MS_PER_SECOND = 1000
        private const val DEFAULT_MAX_FEATURED_PRODUCT = 5
    }

    data class ExtraParams(
            val channelId: String?,
            val videoStartMillis: Long?
    )
}