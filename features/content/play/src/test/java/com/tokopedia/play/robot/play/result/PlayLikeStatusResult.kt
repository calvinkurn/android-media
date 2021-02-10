package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayLikeStatusInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayLikeStatusResult(
        private val result: PlayLikeStatusInfoUiModel
) {

    fun isEqualTo(expected: PlayLikeStatusInfoUiModel): PlayLikeStatusResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}