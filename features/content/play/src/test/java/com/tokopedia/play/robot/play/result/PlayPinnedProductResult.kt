package com.tokopedia.play.robot.play.result

import com.tokopedia.play.view.uimodel.recom.PinnedProductUiModel
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayPinnedProductResult(
        private val result: PinnedProductUiModel
) {

    fun isEqualTo(expected: PinnedProductUiModel): PlayPinnedProductResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)

        return this
    }
}