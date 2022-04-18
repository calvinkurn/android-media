package com.tokopedia.reviewcommon.extension

import android.os.Bundle

@Suppress("UNCHECKED_CAST")
fun <T: Any> Bundle.getSavedState(key: String, defaultValue: T?): T? {
    return get(key) as? T ?: defaultValue
}