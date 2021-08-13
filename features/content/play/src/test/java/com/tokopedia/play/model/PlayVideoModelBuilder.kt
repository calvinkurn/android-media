package com.tokopedia.play.model

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play_common.model.PlayBufferControl
import io.mockk.mockk

/**
 * Created by jegul on 09/02/21
 */
class PlayVideoModelBuilder {

    fun buildVideoMeta(
            videoPlayer: PlayVideoPlayerUiModel = buildYouTubeVideoPlayer(),
            videoStream: PlayVideoStreamUiModel = buildVideoStream()
    ) = PlayVideoMetaInfoUiModel(
            videoPlayer = videoPlayer,
            videoStream = videoStream
    )

    fun buildVideoStream(
            id: String = "1",
            orientation: VideoOrientation = VideoOrientation.Vertical
    ) = PlayVideoStreamUiModel(
            id = id,
            orientation = orientation
    )

    fun buildYouTubeVideoPlayer(
            youtubeId: String = "aX124=12g"
    ) = PlayVideoPlayerUiModel.YouTube(youtubeId = youtubeId)

    fun buildGeneralVideoPlayerParams(
            videoUrl: String = "https://www.tokopedia.com",
            buffer: PlayBufferControl = PlayBufferControl(),
            lastMillis: Long? = null
    ) = PlayGeneralVideoPlayerParams(videoUrl = videoUrl, buffer = buffer, lastMillis = lastMillis)

    fun buildIncompleteGeneralVideoPlayer(
            params: PlayGeneralVideoPlayerParams = buildGeneralVideoPlayerParams()
    ) = PlayVideoPlayerUiModel.General.Incomplete(params = params)

    fun buildCompleteGeneralVideoPlayer(
            params: PlayGeneralVideoPlayerParams = buildGeneralVideoPlayerParams(),
            exoPlayer: ExoPlayer = mockk(relaxed = true),
    ) = PlayVideoPlayerUiModel.General.Complete(
            params = params,
            exoPlayer = exoPlayer,
    )
}