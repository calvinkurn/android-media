package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayQuickReplyResult(
        private val result: PlayQuickReplyInfoUiModel
) {

    fun isEqualTo(expected: PlayQuickReplyInfoUiModel): PlayQuickReplyResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}