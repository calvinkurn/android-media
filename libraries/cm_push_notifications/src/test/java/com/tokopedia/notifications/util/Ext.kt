package com.tokopedia.notifications.util

import org.assertj.core.api.Assertions

infix fun Any.assertIsEqualsTo(type: Any) {
    Assertions.assertThat(this).isEqualTo(type)
}