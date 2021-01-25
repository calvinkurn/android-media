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
typealias PlayChannelResponseMapper = PlayViewerMapper<ChannelDetailsWithRecomResponse, List<PlayChannelData>>

class PlayChannelDetailsWithRecomMapper(
        private val context: Context
) : PlayChannelResponseMapper {

    private val exoPlayer: ExoPlayer
        get() = PlayVideoManager.getInstance(context).videoPlayer

    override fun map(input: ChannelDetailsWithRecomResponse): List<PlayChannelData> {
        return input.channelDetails.dataList.map {
            PlayChannelData(
                    id = it.id,
                    partnerInfo = mapPartnerInfo(it.partner),
                    likeInfo = mapLikeInfo(it.config.feedLikeParam),
                    shareInfo = mapShareInfo(it.share, it.config.active, it.config.freezed),
                    cartInfo = mapCartInfo(it.config),
                    pinnedInfo = mapPinnedInfo(it.pinnedMessage, it.partner, it.config),
                    quickReplyInfo = mapQuickReply(it.quickReplies),
                    videoMetaInfo = mapVideoMeta(it.video, it.config, it.isLive),
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

    private fun mapPinnedMessage(pinnedMessageResponse: ChannelDetailsWithRecomResponse.PinnedMessage, partnerResponse: ChannelDetailsWithRecomResponse.Partner): PinnedMessageUiModel? {
        return if (pinnedMessageResponse.id.isNotEmpty()
                && !pinnedMessageResponse.id.contentEquals("0")
                && pinnedMessageResponse.title.isNotEmpty()) {
            PinnedMessageUiModel(
                    applink = pinnedMessageResponse.redirectUrl,
                    partnerName = partnerResponse.name,
                    title = pinnedMessageResponse.title
            )
        } else null
    }

    private fun mapPinnedProduct(configResponse: ChannelDetailsWithRecomResponse.Config, partnerResponse: ChannelDetailsWithRecomResponse.Partner): PinnedProductUiModel? {
        return if (configResponse.showPinnedProduct)
            PinnedProductUiModel(
                    partnerName = partnerResponse.name,
                    title = configResponse.pinnedProductConfig.pinTitle,
                    hasPromo = configResponse.hasPromo
            ) else null
    }

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
            isActive = configResponse.active
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