package com.tokopedia.play.robot

import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
interface RobotResult {

    fun Boolean.isTrue() {
        Assertions
                .assertThat(this)
                .isTrue
    }

    fun Boolean.isFalse() {
        Assertions
                .assertThat(this)
                .isFalse
    }

    fun <T: Any> T?.isEqualTo(expected: T) {
        Assertions
                .assertThat(this)
                .isEqualTo(expected)
    }
}