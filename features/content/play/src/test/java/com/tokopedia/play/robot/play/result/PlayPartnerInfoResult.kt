package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfoUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayPartnerInfoResult(
        private val result: PlayPartnerInfoUiModel
) {

    fun isEqualTo(expected: PlayPartnerInfoUiModel): PlayPartnerInfoResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }

    fun isNotEqualTo(expected: PlayPartnerInfoUiModel): PlayPartnerInfoResult {
        Assertions
                .assertThat(result)
                .isNotEqualTo(expected)

        return this
    }
}