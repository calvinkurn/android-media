package com.tokopedia.feedplus.helper

import org.assertj.core.api.Assertions

/**
 * @author by astidhiyaa on 23/09/22
 */
fun <T : Any?> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}
