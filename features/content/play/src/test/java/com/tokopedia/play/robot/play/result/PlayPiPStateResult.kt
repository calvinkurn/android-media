package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import org.assertj.core.api.Assertions
import kotlin.math.exp

/**
 * Created by jegul on 11/02/21
 */
class PlayPiPStateResult(
        private val result: PiPState
) {

    val pipMode = PiPModeResult(result.mode)

    fun isEqualTo(expected: PiPState): PlayPiPStateResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    class PiPModeResult(
            private val result: PiPMode?
    ) {

        fun isEqualTo(expected: PiPMode?): PiPModeResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}