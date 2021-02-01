package com.tokopedia.play.view.uimodel.mapper

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.player.PlayVideoManager

/**
 * Created by jegul on 21/01/21
 */
class PlayChannelDetailsWithRecomMapper(
        private val context: Context
) {

    private val exoPlayer: ExoPlayer
        get() = PlayVideoManager.getInstance(context).videoPlayer

    fun map(input: ChannelDetailsWithRecomResponse): List<PlayChannelData> {
        return input.channelDetails.dataList.map {
            PlayChannelData(
                    id = it.id,
                    partnerInfo = mapPartnerInfo(it.partner),
                    likeInfo = mapLikeInfo(it.config.feedLikeParam),
                    totalViewInfo = mapTotalViewInfo(),
                    shareInfo = mapShareInfo(it.share, it.config.active, it.config.freezed),
                    cartInfo = mapCartInfo(it.config),
                    pinnedInfo = mapPinnedInfo(it.pinnedMessage, it.partner, it.config),
                    quickReplyInfo = mapQuickReply(it.quickReplies),
                    videoMetaInfo = mapVideoMeta(it.video, it.config, it.isLive),
                    statusInfo = mapChannelStatusInfo(it.config, it.title)
//                    miscConfigInfo = mapMiscConfigInfo(it.config),
            )
        }
    }

    private fun mapPartnerInfo(partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPartnerInfoUiModel.Incomplete(
            basicInfo = PlayPartnerBasicInfoUiModel(
                    id = partnerResponse.id.toLongOrZero(),
                    name = partnerResponse.name,
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
                content = fullShareContent,
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
                    partnerId = partnerResponse.id.toLongOrZero()
            )
    )

    private fun mapPinnedMessage(pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage, partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPinnedUiModel.PinnedMessage(
            id = pinnedMessageResponse.id,
            applink = pinnedMessageResponse.redirectUrl,
            partnerName = partnerResponse.name,
            title = pinnedMessageResponse.title,
    )

    private fun mapPinnedProduct(configResponse: ChannelDetailsWithRecomResponse.Config, partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPinnedUiModel.PinnedProduct(
            partnerName = partnerResponse.name,
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
            configResponse: ChannelDetailsWithRecomResponse.Config,
            isLive: Boolean
    ) = PlayVideoMetaInfoUiModel(
            videoPlayer = mapVideoPlayer(videoResponse),
            videoStream = mapVideoStream(videoResponse, configResponse, isLive)
    )

    private fun mapVideoPlayer(videoResponse: ChannelDetailsWithRecomResponse.Video) = when (videoResponse.type) {
        "live", "vod" -> General(exoPlayer)
        "youtube" -> YouTube(videoResponse.streamSource)
        else -> Unknown
    }

    private fun mapVideoStream(
            videoResponse: ChannelDetailsWithRecomResponse.Video,
            configResponse: ChannelDetailsWithRecomResponse.Config,
            isLive: Boolean
    ) = VideoStreamUiModel(
            uriString = videoResponse.streamSource,
            channelType = if (isLive) PlayChannelType.Live else PlayChannelType.VOD,
            buffer = mapVideoBufferControl(videoResponse.bufferControl),
            orientation = VideoOrientation.getByValue(videoResponse.orientation),
            backgroundUrl = configResponse.roomBackground.imageUrl,
            isActive = configResponse.active,
            lastMillis = null
    )

    private fun mapChannelStatusInfo(
            configResponse: ChannelDetailsWithRecomResponse.Config,
            title: String
    ) = PlayStatusInfoUiModel(
            isBanned = false,
            isFreeze = !configResponse.active || configResponse.freezed,
            bannedModel = mapBannedModel(configResponse.bannedData),
            freezeModel = mapFreezeModel(configResponse.freezeData, title),
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

    private fun mapMiscConfigInfo(configResponse: ChannelDetailsWithRecomResponse.Config) = PlayMiscConfigUiModel(
            shouldShowCart = configResponse.showCart
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

    companion object {
        private const val MS_PER_SECOND = 1000
    }
}