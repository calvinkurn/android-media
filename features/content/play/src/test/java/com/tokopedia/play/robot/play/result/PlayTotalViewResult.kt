package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayTotalViewUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayTotalViewResult(
        private val result: PlayTotalViewUiModel
) {

    fun isEqualTo(expected: PlayTotalViewUiModel): PlayTotalViewResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}