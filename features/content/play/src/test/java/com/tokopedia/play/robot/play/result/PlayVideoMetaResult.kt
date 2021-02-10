package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayVideoMetaResult(
        private val result: PlayVideoMetaInfoUiModel
) {

    val videoStream = PlayVideoStreamResult(result.videoStream)

    class PlayVideoStreamResult(
            private val result: PlayVideoStreamUiModel
    ) {

        fun isEqualTo(expected: PlayVideoStreamUiModel): PlayVideoStreamResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}