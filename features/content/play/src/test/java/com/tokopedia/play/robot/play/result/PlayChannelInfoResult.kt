package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayChannelInfoResult(
        private val result: PlayChannelInfoUiModel
) {

    fun isEqualTo(expected: PlayChannelInfoUiModel): PlayChannelInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}