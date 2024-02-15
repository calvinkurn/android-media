package com.tokopedia.editor.util

import kotlinx.coroutines.flow.MutableStateFlow

internal fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}
