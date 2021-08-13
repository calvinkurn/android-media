package com.tokopedia.play.robot.parent.result

import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayParentUserIdResult(
        private val result: String
) {

    fun shouldBe(expected: String): PlayParentUserIdResult {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)
        return this
    }
}