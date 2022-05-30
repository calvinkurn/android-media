package com.tokopedia.createpost.util

import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
fun <T : Any> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

inline fun PagedState.isSuccess() {
    Assertions
        .assertThat(this)
        .isInstanceOf(PagedState.Success::class.java)
}

inline fun PagedState.isError() {
    Assertions
        .assertThat(this)
        .isInstanceOf(PagedState.Error::class.java)
}