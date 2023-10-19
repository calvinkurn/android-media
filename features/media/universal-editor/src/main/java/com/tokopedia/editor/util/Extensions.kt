package com.tokopedia.editor.util

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallHelper
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

internal fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}

internal fun safeLoadNativeLibrary(context: Context, name: String) {
    try {
        SplitInstallHelper.loadLibrary(context, name)
    } catch (t: Throwable) {
        Timber.d("media-editor: $t")
    }
}
