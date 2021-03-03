package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayStatusInfoUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayStatusInfoResult(
        private val result: PlayStatusInfoUiModel
) {

    val statusType = StatusTypeResult(result.statusType)

    fun isEqualTo(expected: PlayStatusInfoUiModel): PlayStatusInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    class StatusTypeResult(private val result: PlayStatusType) {

        fun isEqualTo(expected: PlayStatusType): StatusTypeResult {
            Assertions
                    .assertThat(result)
                    .isEqualTo(expected)

            return this
        }
    }
}