package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayCartInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayCartInfoResult(
        private val result: PlayCartInfoUiModel
) {

    fun isEqualTo(expected: PlayCartInfoUiModel): PlayCartInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    fun isNotEqualTo(expected: PlayCartInfoUiModel): PlayCartInfoResult {
        Assertions
                .assertThat(result)
                .isNotEqualTo(expected)

        return this
    }
}