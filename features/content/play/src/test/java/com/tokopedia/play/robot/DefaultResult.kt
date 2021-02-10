package com.tokopedia.play.robot

import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
open class DefaultResult<T>(
        val result: T
) {

    fun isEqualTo(expected: T) {
        Assertions
                .assertThat(result)
                .isEqualTo(expected)
    }

    fun isNotEqualTo(expected: T) {
        Assertions
                .assertThat(result)
                .isNotEqualTo(expected)
    }
}