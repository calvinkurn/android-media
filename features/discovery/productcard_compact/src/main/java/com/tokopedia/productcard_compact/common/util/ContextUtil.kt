package com.tokopedia.productcard_compact.common.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

internal object ContextUtil {
    fun Context.getActivityFromContext(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> this.baseContext.getActivityFromContext()
            else -> null
        }
    }
}
