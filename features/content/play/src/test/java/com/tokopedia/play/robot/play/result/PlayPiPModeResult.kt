package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.type.PiPMode
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 11/02/21
 */
class PlayPiPModeResult(
        private val result: PiPMode
) {

    fun isEqualTo(expected: PiPMode): PlayPiPModeResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}