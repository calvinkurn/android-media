package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayPinnedMessageResult(
        private val result: PinnedMessageUiModel
) {

    fun isEqualTo(expected: PinnedMessageUiModel): PlayPinnedMessageResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}