package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayPinnedUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayPinnedResult(
        private val result: PlayPinnedUiModel
) {

    fun isEqualTo(expected: PlayPinnedUiModel): PlayPinnedResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}