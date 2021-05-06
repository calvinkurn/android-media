package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayVideoMetaResult(
        private val result: PlayVideoMetaInfoUiModel
) {

    val videoStream = VideoStreamResult(result.videoStream)
    val videoPlayer = VideoPlayerResult(result.videoPlayer)
    val videoOrientation = VideoOrientationResult(result.videoStream.orientation)

    class VideoStreamResult(
            private val result: PlayVideoStreamUiModel
    ) {

        fun isEqualTo(expected: PlayVideoStreamUiModel): VideoStreamResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }

    class VideoPlayerResult(
            private val result: PlayVideoPlayerUiModel
    ) {
        fun isEqualTo(expected: PlayVideoPlayerUiModel): VideoPlayerResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }

    class VideoOrientationResult(
            private val result: VideoOrientation
    ) {
        fun isEqualTo(expected: VideoOrientation): VideoOrientationResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}