package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayShareInfoResult(
        private val result: PlayShareInfoUiModel
) {

    fun isEqualTo(expected: PlayShareInfoUiModel): PlayShareInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}