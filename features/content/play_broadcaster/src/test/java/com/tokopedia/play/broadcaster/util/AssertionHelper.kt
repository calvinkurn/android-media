package com.tokopedia.play.broadcaster.util

import org.assertj.core.api.Assertions

/**
 * Created by jegul on 11/05/21
 */
fun <T: Any> T.isEqualTo(expected: T) {
    Assertions
            .assertThat(this)
            .isEqualTo(expected)
}